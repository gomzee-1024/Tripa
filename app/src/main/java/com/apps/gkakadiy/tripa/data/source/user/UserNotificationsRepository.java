package com.apps.gkakadiy.tripa.data.source.user;

import androidx.annotation.NonNull;
import android.util.Log;

import com.apps.gkakadiy.tripa.MyApplication;
import com.apps.gkakadiy.tripa.data.Notification;
import com.apps.gkakadiy.tripa.data.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class UserNotificationsRepository implements UserNotificationsDataSource {

    private static UserNotificationsRepository INSTANCE = null;
    private DatabaseReference mDatabaseNotifications;
    private DatabaseReference mDatabaseRequests;

    private UserNotificationsRepository(){
        mDatabaseNotifications = MyApplication.getInstance().getmDatabaseReference().child("user_notifications");
        mDatabaseRequests = MyApplication.getInstance().getmDatabaseReference().child("requests");
    }

    public static UserNotificationsRepository getInstance(){
        if(INSTANCE!=null){
            return INSTANCE;
        }else{
            INSTANCE = new UserNotificationsRepository();
            return INSTANCE;
        }
    }

    @Override
    public Flowable<Notification> getAllNotifications(String userId) {
        return Flowable.create(new FlowableOnSubscribe<Notification>() {
            @Override
            public void subscribe(FlowableEmitter<Notification> emitter) throws Exception {
                mDatabaseNotifications.orderByChild("date_user_id").endAt(userId).limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Log.d("UserNotifications:","datasnapshot exist");
                            for(DataSnapshot pdataSnapshot : dataSnapshot.getChildren()){
                                Log.d("UserNotifications:","datasnapshot exist "+dataSnapshot.getChildren());
                                Notification notification = pdataSnapshot.getValue(Notification.class);
                                Log.d("UserNotifications:",notification.toString());
                                emitter.onNext(notification);
                            }
                        }
                        emitter.onComplete();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable addNotification(String userToId,Notification notification) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                String key = mDatabaseNotifications.push().getKey();
                notification.setNotification_id(key);
                mDatabaseNotifications.child(key).setValue(notification);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable readNotification(String notificationID,String userId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mDatabaseNotifications.child(notificationID).child("seen").setValue(new Boolean(true));
            }
        });
    }

    @Override
    public Completable deleteNotification(String userId, String notificationId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mDatabaseNotifications.child(notificationId).removeValue();
                emitter.onComplete();
            }
        });
    }

    @Override
    public Flowable<Request> getRequest(String requestId,String currentUserId) {
        return Flowable.create(new FlowableOnSubscribe<Request>() {
            @Override
            public void subscribe(FlowableEmitter<Request> emitter) throws Exception {
                mDatabaseRequests.child(currentUserId).child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Request request = dataSnapshot.getValue(Request.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }
}
