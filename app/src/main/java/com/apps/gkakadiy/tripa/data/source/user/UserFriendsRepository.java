package com.apps.gkakadiy.tripa.data.source.user;

import androidx.annotation.NonNull;
import android.util.Log;

import com.apps.gkakadiy.tripa.MyApplication;
import com.apps.gkakadiy.tripa.data.Request;
import com.apps.gkakadiy.tripa.data.RequestType;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class UserFriendsRepository implements UserFriendsDataSouce {

    private DatabaseReference mDatabaseUserFriends;
    private FirebaseUser mCurrentFirebaseUser;
    private DatabaseReference mDatabaseNotifications;
    private DatabaseReference mDatabaseFriendRequest;
    private SimpleDateFormat sdf ;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mLiveFriendsListener;
    private ValueEventListener mLiveFriendRequestedListener;
    private Query mLiveFriendRequestedQuery,mLiveFriendStatusQuery;
    private static UserFriendsRepository INSTANCE = null;

    private UserFriendsRepository(){
        mDatabaseReference = MyApplication.getInstance().getmDatabaseReference();
        mDatabaseUserFriends = mDatabaseReference.child("user_friends");
        mDatabaseNotifications = mDatabaseReference.child("user_notifications");
        mDatabaseFriendRequest = mDatabaseReference.child("requests");
        mCurrentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public static UserFriendsRepository getInstance(){
        if(INSTANCE!=null){
            return INSTANCE;
        }else{
            INSTANCE = new UserFriendsRepository();
            return INSTANCE;
        }
    }

    @Override
    public Flowable<String> getUserFriendCount(String userId) {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                mDatabaseUserFriends.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            StringBuilder countString = new StringBuilder();
                            countString.append(dataSnapshot.getChildrenCount()+":");
                            boolean isFriend =false;
                            for(DataSnapshot pDataSnapshot : dataSnapshot.getChildren()){
                                String id =  pDataSnapshot.getKey();
                                if(id.equalsIgnoreCase(mCurrentFirebaseUser.getUid())){
                                    countString.append("1");
                                    isFriend=true;
                                    break;
                                }
                            }
                            if(!isFriend){
                                countString.append("0");
                            }
                            emitter.onNext(countString.toString());
                            emitter.onComplete();
                        }else{
                            emitter.onNext("0:0");
                            emitter.onComplete();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<String> addFriendRequest(Request request) {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                String key = mDatabaseFriendRequest.push().getKey();
                request.setRequest_id(key);
                //request.setStatus(RequestStatus.REQUESTED.toString());
                mDatabaseFriendRequest.child(key).setValue(request);
                emitter.onNext(key);
                emitter.onComplete();
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Request> getRequest(String requestId) {
        return Flowable.create(new FlowableOnSubscribe<Request>() {
            @Override
            public void subscribe(FlowableEmitter<Request> emitter) throws Exception {
                mDatabaseFriendRequest.child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            Request request = dataSnapshot.getValue(Request.class);
                            emitter.onNext(request);
                            emitter.onComplete();
                        }else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable rejectFriendRequest(String userId, String requestId) {
        return null;
    }

    @Override
    public Completable addFriend(String userId, UserPublic sender) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mDatabaseUserFriends.child(userId).child(sender.getUser_id()).setValue(sender);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable addFriendMutually(UserPublic user, UserPublic sender) {
        return addFriend(user.getUser_id(),sender)
                .andThen(addFriend(sender.getUser_id(),user));
    }

    @Override
    public Completable acceptFriendRequest(String userId, String senderId, String requestId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mDatabaseFriendRequest.child(userId).child(senderId).child(requestId).child("status").setValue("accepted");
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable deleteRequest(Request request) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mDatabaseFriendRequest.child(request.getRequest_id()).removeValue();
                Log.d("UserFriendsRepository:","deleteFriendRequest");
                emitter.onComplete();
            }
        });
    }

    @Override
    public Flowable<Boolean> liveFriendStatus(String userId) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                    mLiveFriendsListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                emitter.onNext(new Boolean(true));
                            } else {
                                emitter.onNext(new Boolean(false));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                mLiveFriendStatusQuery = mDatabaseUserFriends.child(mCurrentFirebaseUser.getUid()).child(userId);
                mLiveFriendStatusQuery.addValueEventListener(mLiveFriendsListener);
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Request> liveFriendRequestedStatus(String userId) {
        return Flowable.create(new FlowableOnSubscribe<Request>() {
            @Override
            public void subscribe(FlowableEmitter<Request> emitter) throws Exception {
                    mLiveFriendRequestedListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("UserFriendsRepository:","liveFriendRequestedStatus");
                            if(dataSnapshot.exists()){
                                for(DataSnapshot pdataSnapshot : dataSnapshot.getChildren()) {
                                    Request request = pdataSnapshot.getValue(Request.class);
                                    if (request == null) {
                                        Log.d("UserFriendsRepository:", "liveRequest is null");
                                    }
                                    emitter.onNext(request);
                                }
                                //emitter.onComplete();
                            }else{
                                //emitter.onComplete();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            emitter.onError(databaseError.toException());
                        }
                    };
                mLiveFriendRequestedQuery =  mDatabaseFriendRequest.orderByChild("userid_senderid_context").equalTo(userId+"_"+mCurrentFirebaseUser.getUid()+"_"+RequestType.FRIEND_REQUEST.toString());
                mLiveFriendRequestedQuery.addValueEventListener(mLiveFriendRequestedListener);
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public void removeliveFriendStatusListener(String userId) {
        if(mLiveFriendStatusQuery!=null) {
            mLiveFriendStatusQuery.removeEventListener(mLiveFriendsListener);
        }
        //mLiveFriendsListener=null;
    }

    @Override
    public void removeliveFriendRequestedListener(String userId) {
        if(mLiveFriendRequestedQuery!=null) {
            mLiveFriendRequestedQuery.removeEventListener(mLiveFriendRequestedListener);
        }
        //mLiveFriendRequestedListener=null;
    }

}
