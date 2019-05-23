package com.apps.gkakadiy.tripa.userprofile;

import android.util.Log;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.data.FriendStatusType;
import com.apps.gkakadiy.tripa.data.Notification;
import com.apps.gkakadiy.tripa.data.NotificationType;
import com.apps.gkakadiy.tripa.data.Request;
import com.apps.gkakadiy.tripa.data.RequestStatus;
import com.apps.gkakadiy.tripa.data.RequestType;
import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.apps.gkakadiy.tripa.data.source.user.UserFriendsRepository;
import com.apps.gkakadiy.tripa.data.source.user.UserNotificationsRepository;
import com.apps.gkakadiy.tripa.data.source.user.UserRepository;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserProfilePresenter implements UserProfileContract.UserProfileBasePresenter {

    private UserRepository mUserRepository;
    private UserFriendsRepository mUserFriendRepository;
    private UserNotificationsRepository mUserNotificationsRepository;
    private UserProfileContract.UserProfileBaseView mUserProfileView;
    private UserPublic mUser;
    private CompositeDisposable mCompositeDisposable;

    public UserProfilePresenter(UserRepository userRepository, UserFriendsRepository userFriendsRepository,UserNotificationsRepository userNotificationsRepository, UserProfileContract.UserProfileBaseView profileBaseView) {
        mUserProfileView = profileBaseView;
        mUserRepository = userRepository;
        mUserFriendRepository = userFriendsRepository;
        mUserNotificationsRepository = userNotificationsRepository;
        mCompositeDisposable = new CompositeDisposable();

    }

    @Override
    public void loadUser(String userId) {
        Log.d("UserProfilePresenter:","loadUser "+userId);
        mUserProfileView.setFriendStatus(FriendStatusType.NOT_FRIENDS);
        Disposable d = mUserRepository.getUserLive(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    Log.d("UserProfilePresenter:", "loadUser " + user.toString());
                    mUser = user;
                    setUI();
                    getUserFriendCount();
                }, throwable -> {
                    Log.d("UserProfilePresenter:", "loadUser " + throwable.getMessage());
                });
        mCompositeDisposable.add(d);
    }

    private void liveFriendRequestedStatus(){
        Disposable d2 = mUserFriendRepository.liveFriendRequestedStatus(mUser.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(request -> {
                    if(request.getStatus().toLowerCase().equalsIgnoreCase(RequestStatus.REQUESTED.toString())){
                        Log.d("UserProfileActivity:","friend requested");
                        mUserProfileView.setFriendRequestIcon(R.drawable.round_person_add_black_24);
                        mUserProfileView.setFriendRequestText(Constants.FRIEND_REQUEST_BUTTON_SENT);
                        mUserProfileView.setFriendStatus(FriendStatusType.REQUESTED);
                    }else{
                        Log.d("UserProfileActivity:","friend not requested");
                        if(mUserProfileView.getFriendStatusType()==FriendStatusType.NOT_FRIENDS) {
                            mUserProfileView.setFriendRequestIcon(R.drawable.outline_person_add_black_24);
                            mUserProfileView.setFriendStatus(FriendStatusType.NOT_FRIENDS);
                            mUserProfileView.setFriendRequestText(Constants.FRIEND_REQUEST_BUTTON_ADD);
                        }
                    }
                },throwable -> {

                });
        mCompositeDisposable.add(d2);
    }

    private void liveFriendStatus(){
        Disposable d1 = mUserFriendRepository.liveFriendStatus(mUser.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if(result==true){
                        Log.d("UserProfileActivity:","friends");
                        mUserProfileView.setFriendRequestIcon(R.drawable.round_people_outline_black_24);
                        mUserProfileView.setFriendRequestText(Constants.FRIEND_REQUEST_BUTTON_FRIENDS);
                        mUserProfileView.setFriendStatus(FriendStatusType.FRIENDS);
                    }else{
                        Log.d("UserProfileActivity:","not friends");
                        liveFriendRequestedStatus();
                    }
                },throwable -> {

                });
        mCompositeDisposable.add(d1);
    }

    private void setUI() {
        liveFriendStatus();

        if(mUser.getUser_about()!=null) {
            mUserProfileView.setAboutText(mUser.getUser_about());
        }
        if(mUser.getUser_cover_pic_url()!=null) {
            mUserProfileView.setCoverImage(mUser.getUser_cover_pic_url());
        }
        if(mUser.getUser_profile_pic_url()!=null) {
            mUserProfileView.setProfileImage(mUser.getUser_profile_pic_url());
        }
        mUserProfileView.setDisplayName(mUser.getUser_name());

    }

    private void getUserFriendCount() {
        Disposable d = mUserFriendRepository.getUserFriendCount(mUser.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(countString -> {
                    Log.d("UserProfilePresenter:",countString);
                    if(countString!=null) {
                        String[] split = countString.split(":");
                        mUserProfileView.setFriendCount(split[0]);
                        if (split[1].equals("1")) {
                            mUserProfileView.setFriendRequestIcon(R.drawable.round_people_outline_black_24);
                            mUserProfileView.setFriendRequestText(Constants.FRIEND_REQUEST_BUTTON_FRIENDS);
                            mUserProfileView.setFriendStatus(FriendStatusType.FRIENDS);
                        }
                        mUserProfileView.setFriendCount(split[0]);
                    }else{
                        mUserProfileView.setFriendCount("0");
                    }
                }, throwable -> {
                    Log.d("UserProfilePresenter:", "getUserFriendCount " + throwable.getMessage());
                });
        mCompositeDisposable.add(d);
    }

    @Override
    public void isUserFriend(String userId) {

    }

    @Override
    public void sendFriendRequest() {
        User user = mUserRepository.getCurrentUserDirect();
        UserPublic userPublic = new UserPublic();
        userPublic.copyUserPublicData(user);
        Request request = new Request();
        request.setContext(RequestType.FRIEND_REQUEST.toString());
        request.setSender_id(userPublic.getUser_id());
        request.setUser_id(mUser.getUser_id());
        LocalDate date = new LocalDate(DateTimeZone.UTC);
        request.setCreation_date(date.toString());
        request.setStatus(RequestStatus.REQUESTED.toString());
        request.setUserid_senderid_context(mUser.getUser_id()+"_"+userPublic.getUser_id()+"_"+RequestType.FRIEND_REQUEST.toString());
        request.setText1(userPublic.getUser_profile_pic_url());
        request.setText2(userPublic.getUser_name()+" sent you a freind request.");

        Notification notification = new Notification();
        notification.setNotification_context(NotificationType.FRIEND_REQUEST.toString());
        notification.setUser_id(mUser.getUser_id());
        notification.setSender_id(userPublic.getUser_id());
        LocalDate creationDate = new LocalDate(DateTimeZone.UTC);
        notification.setDate_user_id(creationDate.toString()+"_"+notification.getUser_id());
        notification.setCreation_date(creationDate.toString());
        notification.setText1(userPublic.getUser_profile_pic_url());
        notification.setText2(userPublic.getUser_name()+" sent you a freind request.");
        notification.setSeen(false);

        Disposable d = mUserFriendRepository.addFriendRequest(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe( requestId -> {
                    //mUserProfileView.setFriendRequestIcon(R.drawable.round_person_add_black_24);
                    //mUserProfileView.setFriendRequestText("Sent");
                    if(requestId!=null) {
                        notification.setContext_id(requestId);
                        addFriendRequestSentNotification(mUser, notification);
                    }
                });
        mCompositeDisposable.add(d);
    }

    private void addFriendRequestSentNotification(UserPublic userTo,Notification notification){
        Disposable d1 = mUserNotificationsRepository.addNotification(userTo.getUser_id(),notification)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(() -> {

                });
        mCompositeDisposable.add(d1);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mUserFriendRepository.removeliveFriendRequestedListener(mUser.getUser_id());
        mUserFriendRepository.removeliveFriendStatusListener(mUser.getUser_id());
        mUserNotificationsRepository = null;
        mUserProfileView = null;
        mUserFriendRepository = null;
        mUserRepository = null;
    }

}
