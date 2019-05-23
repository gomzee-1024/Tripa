package com.apps.gkakadiy.tripa.notifications;

import android.util.Log;

import com.apps.gkakadiy.tripa.data.Notification;
import com.apps.gkakadiy.tripa.data.NotificationType;
import com.apps.gkakadiy.tripa.data.RequestStatus;
import com.apps.gkakadiy.tripa.data.Request;
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

public class NotificationsPresenter implements NotficationsContract.NotificationsBasePresenter {

    private UserRepository mUserRepository;
    private UserFriendsRepository mUserFriendsRepository;
    private UserNotificationsRepository mUserNotificationsRepository;
    private NotficationsContract.NotificationsBaseView mNotificationsView;
    private CompositeDisposable mCompositeDisposable;
    private User mCurrentUser;
    private String mCurrentUpdateNotificationId;

    public NotificationsPresenter(UserRepository userRepository, UserFriendsRepository userFriendsRepository, UserNotificationsRepository userNotificationsRepository, NotficationsContract.NotificationsBaseView notificationsBaseView) {
        mUserRepository = userRepository;
        mUserFriendsRepository = userFriendsRepository;
        mUserNotificationsRepository = userNotificationsRepository;
        mNotificationsView = notificationsBaseView;
        mCompositeDisposable = new CompositeDisposable();
        mCurrentUser = mUserRepository.getCurrentUserDirect();
    }

    @Override
    public void loadAllNotifications() {
        mNotificationsView.clearNotifications();
        Disposable d = mUserNotificationsRepository.getAllNotifications(mUserRepository.getCurrentUserDirect().getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notification -> {
                    if(notification!=null) {
                        Log.d("NotificationsPresenter:", "notification not null");
                    }
                    setNotificationRead(notification.getNotification_id(), mCurrentUser.getUser_id());
                    mNotificationsView.addNotification(notification);
                }, throwable -> {
                    //error handling.
                });
        mCompositeDisposable.add(d);
    }

    @Override
    public void acceptFriendRequest(Notification notification,int position) {
        mCurrentUpdateNotificationId = notification.getNotification_id();

        Disposable d1 = mUserFriendsRepository.getRequest(notification.getContext_id())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(request -> {
                           if(request.getStatus().equalsIgnoreCase(RequestStatus.REQUESTED.toString())){
                               getUserToAcceptRequest(request);
                           }
                        });
        mCompositeDisposable.add(d1);

    }

    @Override
    public void rejectFriendRequest(Notification notification, int position) {
        mCurrentUpdateNotificationId = notification.getNotification_id();
        Disposable d1 = mUserFriendsRepository.getRequest(notification.getContext_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(request -> {
                    if(request.getStatus().equalsIgnoreCase(RequestStatus.REQUESTED.toString())){
                        getUserToRejectRequest(request);
                    }
                });
        mCompositeDisposable.add(d1);
    }

    private void getUserToAcceptRequest(Request request) {
        Disposable d = mUserRepository.getUserPublic(request.getSender_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(userPublic -> {
                    if (userPublic != null) {
                        addFriendMutualy(userPublic,request);
                    }
                }, throwable -> {

                });
        mCompositeDisposable.add(d);
    }

    private void getUserToRejectRequest(Request request) {
        UserPublic currentUserPublic = new UserPublic();
        currentUserPublic.copyUserPublicData(mCurrentUser);
        Disposable d = mUserRepository.getUserPublic(request.getSender_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(userPublic -> {
                    if (userPublic != null) {
                        addNotificationFriendRequestRejected(userPublic,currentUserPublic);
                        deleteFriendRequest(request);
                        deleteNotification(mCurrentUpdateNotificationId);
                    }
                }, throwable -> {

                });
        mCompositeDisposable.add(d);
    }

    @Override
    public void deleteNotification(String notificationId) {
        Disposable d = mUserNotificationsRepository.deleteNotification(mCurrentUser.getUser_id(), notificationId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(() -> {

                }, throwable -> {

                });
        mCompositeDisposable.add(d);
    }

    @Override
    public void deleteFriendRequest(Request request) {
        Disposable d = mUserFriendsRepository.deleteRequest(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> {

                });
        mCompositeDisposable.add(d);
    }


    private void addFriendMutualy(UserPublic senderPublic,Request request) {

        UserPublic currentUserPublic = new UserPublic();
        currentUserPublic.copyUserPublicData(mCurrentUser);
        Disposable d = mUserFriendsRepository.addFriendMutually(currentUserPublic, senderPublic)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(() -> {
                    addNotificationFriendRequestAccepted(senderPublic,currentUserPublic);
                    deleteFriendRequest(request);
                    deleteNotification(mCurrentUpdateNotificationId);
                }, throwable -> {

                });
        mCompositeDisposable.add(d);

    }

    private void addNotificationFriendRequestAccepted(UserPublic senderPublic,UserPublic currentUserPublic){
        Notification notification = new Notification();
        notification.setNotification_context(NotificationType.FRIEND_REQUEST_STATUS.toString());
        notification.setSeen(false);
        notification.setSender_id(currentUserPublic.getUser_id());
        notification.setUser_id(senderPublic.getUser_id());
        LocalDate creationDate = new LocalDate(DateTimeZone.UTC);
        notification.setDate_user_id(creationDate.toString()+"_"+notification.getUser_id());
        notification.setCreation_date(creationDate.toString());
        notification.setText1(currentUserPublic.getUser_profile_pic_url());
        notification.setText2(currentUserPublic.getUser_name() + " accepted your friend request.");
        Disposable d = mUserNotificationsRepository.addNotification(senderPublic.getUser_id(),notification)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(()->{

                },throwable -> {

                });
        mCompositeDisposable.add(d);
    }

    private void addNotificationFriendRequestRejected(UserPublic senderPublic,UserPublic currentUserPublic){
        Notification notification = new Notification();
        notification.setNotification_context(NotificationType.FRIEND_REQUEST_STATUS.toString());
        notification.setSeen(false);
        notification.setUser_id(senderPublic.getUser_id());
        notification.setSender_id(currentUserPublic.getUser_id());
        LocalDate creationDate = new LocalDate(DateTimeZone.UTC);
        notification.setDate_user_id(creationDate.toString()+"_"+notification.getUser_id());
        notification.setCreation_date(creationDate.toString());
        notification.setText1(currentUserPublic.getUser_profile_pic_url());
        notification.setText2(currentUserPublic.getUser_name() + " rejected your friend request.");
        Disposable d = mUserNotificationsRepository.addNotification(senderPublic.getUser_id(),notification)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(()->{

                },throwable -> {

                });
        mCompositeDisposable.add(d);
    }

    private void setNotificationRead(String notificationId, String userId) {
        Disposable d = mUserNotificationsRepository.readNotification(notificationId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(() -> {
                    //OnComplete
                }, throwable -> {

                });
        mCompositeDisposable.add(d);
    }

    public void unsubscribe() {
        mCompositeDisposable.clear();
        mUserNotificationsRepository = null;
        mUserRepository = null;
        mUserFriendsRepository = null;
        mNotificationsView = null;
    }
}
