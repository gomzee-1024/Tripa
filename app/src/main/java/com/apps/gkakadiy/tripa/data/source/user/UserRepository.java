package com.apps.gkakadiy.tripa.data.source.user;

import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.apps.gkakadiy.tripa.MyApplication;
import com.apps.gkakadiy.tripa.data.LoginType;
import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserRepository implements UserDataSource{

    private FirebaseAuth mAuth=null;
    private FirebaseUser mCurrentFirebaseUser = null;
    private User mCurrentUser;
    private static UserRepository INSTANCE = null;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseUsersPublic;
    private ValueEventListener mUserValueEventListener;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ValueEventListener mUserLiveValueEventListener;
    private DatabaseReference mLiveUserDatabaseRef;
    private ValueEventListener mLiveConnectionStateListener;
    private DatabaseReference mDatabaseLiveConnection;
    private String mUniqueAppId;
    public UserRepository(){
       mAuth = FirebaseAuth.getInstance();
       mCurrentFirebaseUser = mAuth.getCurrentUser();
       mDatabaseUsers = MyApplication.getInstance().getmDatabaseReference().child("users");
       mDatabaseUsersPublic = MyApplication.getInstance().getmDatabaseReference().child("users_public");
       mDatabaseLiveConnection = FirebaseDatabase.getInstance().getReference(".info/connected");
       mAuthStateListener = new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mAuth = FirebaseAuth.getInstance();
                mCurrentFirebaseUser = mAuth.getCurrentUser();
           }
       };
       mAuth.addAuthStateListener(mAuthStateListener);
       mUserValueEventListener = new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   mCurrentUser = dataSnapshot.getValue(User.class);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       };
        mUniqueAppId = FirebaseInstanceId.getInstance().getId();
    }

    public static UserRepository getInstance() {
        if(INSTANCE==null){
            INSTANCE = new UserRepository();
            return INSTANCE;
        }else{
            return INSTANCE;
        }
    }

    @Override
    public Flowable<User> getUser(String userId) {
        Log.d("UserRepository:","getUser");
        final DatabaseReference ref = mDatabaseUsers.child(userId);
        return Flowable.create(new FlowableOnSubscribe<User>() {
            @Override
            public void subscribe(final FlowableEmitter<User> emitter) throws Exception {
                try {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                Log.d("UserRepository:","User exist");
                                User user = dataSnapshot.getValue(User.class);
                                mCurrentUser = user;
                                emitter.onNext(user);
                                emitter.onComplete();
                            }else{
                                Log.d("UserRepository:","User does not exist");
                                User user = new User();
                                emitter.onNext(user);
                                emitter.onComplete();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }catch (Exception e){
                    Log.d("UserRepository:","getUser Exception");
                    emitter.onError(e);
                }

            }

        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<UserPublic> getUserPublic(String userId) {
        return Flowable.create(new FlowableOnSubscribe<UserPublic>() {
            @Override
            public void subscribe(FlowableEmitter<UserPublic> emitter) throws Exception {
                mDatabaseUsersPublic.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            UserPublic user = dataSnapshot.getValue(UserPublic.class);
                            emitter.onNext(user);
                            emitter.onComplete();
                        }else{
                            //Error handling.
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
    public Flowable<User> getCurrentUser() {
        if(mCurrentUser!=null){
            return Flowable.just(mCurrentUser);
        }else{
            if(mCurrentFirebaseUser!=null){
                return getUser(mCurrentFirebaseUser.getUid());
            }else{
                mCurrentFirebaseUser = mAuth.getCurrentUser();
                return getUser(mCurrentFirebaseUser.getUid());
            }
        }
    }

    @Override
    public Flowable<Boolean> liveConnectionState() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                mLiveConnectionStateListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean connected = dataSnapshot.getValue(Boolean.class);
                        emitter.onNext(connected);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable setOnDisconnect() {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mDatabaseUsers.child(mCurrentUser.getUser_id()).child("user_online").onDisconnect().setValue(new Boolean(false));
                mDatabaseUsersPublic.child(mCurrentUser.getUser_id()).child("user_online").onDisconnect().setValue(new Boolean(false));
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable setFCMtoken(String appId,String fcmToken) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                Log.d("UserRepository:","setFCMToken");
                if(mCurrentFirebaseUser!=null) {
                    mDatabaseUsers.child(mCurrentFirebaseUser.getUid()).child("user_app_ids").child(appId).child("fcm_token").setValue(fcmToken);
                    mDatabaseUsersPublic.child(mCurrentFirebaseUser.getUid()).child("user_app_ids").child(appId).child("fcm_token").setValue(fcmToken);
                }else{
                    Log.d("UserRepository:","mCurrentUser null");
                }
                emitter.onComplete();
            }
        });
    }


    @Override
    public Completable setOnlineStatus() {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                if(mCurrentUser!=null) {
                    mDatabaseUsers.child(mCurrentUser.getUser_id()).child("user_online").setValue(new Boolean(true));
                    mDatabaseUsersPublic.child(mCurrentUser.getUser_id()).child("user_online").setValue(new Boolean(true));
                }
                emitter.onComplete();
            }
        });
    }

    @Override
    public User getCurrentUserDirect() {
        if(mCurrentUser!=null){
            return mCurrentUser;
        }
        return null;
    }

    @Override
    public Flowable<UserPublic> getUserLive(String userId) {
        Log.d("UserRepository:","getUserLive "+ userId);
        return Flowable.create(new FlowableOnSubscribe<UserPublic>() {
            @Override
            public void subscribe(FlowableEmitter<UserPublic> emitter) throws Exception {
                mUserLiveValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            UserPublic user = dataSnapshot.getValue(UserPublic.class);
                            if (user != null) {
                                emitter.onNext(user);
                            }else{
                                Log.d("UserRepository:","getLiveUser user null");
                            }
                        }else{
                            Log.d("UserRepository:","getLiveUser datasnapshot do not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                mLiveUserDatabaseRef = mDatabaseUsersPublic.child(userId);
                mLiveUserDatabaseRef.addValueEventListener(mUserLiveValueEventListener);
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public void removeLiveUserValueListener() {
        mLiveUserDatabaseRef.removeEventListener(mUserLiveValueEventListener);
    }

    @Override
    public FirebaseUser getCurrentFirebaseUser() {
        if(mCurrentFirebaseUser!=null){
            return mCurrentFirebaseUser;
        }else{
            mCurrentFirebaseUser = mAuth.getCurrentUser();
            return mCurrentFirebaseUser;
        }
    }

    @Override
    public Flowable<UserPublic> createUser(final User user, final String password, LoginType loginType) {
        return Flowable.create(new FlowableOnSubscribe<User>() {
            @Override
            public void subscribe(final FlowableEmitter<User> emitter) throws Exception {
                mAuth.createUserWithEmailAndPassword(user.getUser_email(),password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    user.setUser_id(mAuth.getCurrentUser().getUid());
                                    emitter.onNext(user);
                                }else{
                                    emitter.onError(task.getException());
                                }
                            }
                        });
            }
        },BackpressureStrategy.BUFFER)
                .flatMap(new Function<User, Publisher<User>>() {
                    @Override
                    public Publisher<User> apply(User user) throws Exception {
                        return addUserToDB(user);
                    }
                })
                .flatMap(new Function<User, Publisher<UserPublic>>() {
                    @Override
                    public Publisher<UserPublic> apply(User user) throws Exception {
                        UserPublic userPublic = new UserPublic();
                        userPublic.copyUserPublicData(user);
                        return addUserPublicToDB(userPublic);
                    }
                });
    }

    @Override
    public Flowable<User> addUserToDB(final User user) {
        return Flowable.create(new FlowableOnSubscribe<User>() {
            @Override
            public void subscribe(final FlowableEmitter<User> emitter) throws Exception {
                mDatabaseUsers.child(user.getUser_id()).setValue(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    emitter.onNext(user);
                                    mCurrentUser = user;
                                    emitter.onComplete();
                                }else{
                                    emitter.onError(task.getException());
                                }
                            }
                        });
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<UserPublic> addUserPublicToDB(UserPublic userPublic) {
        return Flowable.create(new FlowableOnSubscribe<UserPublic>() {
            @Override
            public void subscribe(FlowableEmitter<UserPublic> emitter) throws Exception {
                mDatabaseUsersPublic.child(userPublic.getUser_id()).setValue(userPublic)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    emitter.onNext(userPublic);
                                    emitter.onComplete();
                                }else{
                                    emitter.onError(task.getException());
                                }
                            }
                        });
            }
        },BackpressureStrategy.BUFFER);
    }


    @Override
    public void userAbout(User user, String text) {

    }

    @Override
    public void userProfilePic(User user, Uri profilePicUri) {

    }

    @Override
    public void userShareType(User user, String shareType) {

    }

    @Override
    public void userShareMode(User user, String shareMode) {

    }

    @Override
    public Flowable<Boolean> loginStatus() {
        if(mAuth.getCurrentUser()!=null){
            return Flowable.just(true);
        }else{
            return Flowable.just(false);
        }
    }

    @Override
    public Flowable<Boolean> login(String userEmail, String password) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                mAuth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            emitter.onNext(new Boolean(true));
                            emitter.onComplete();
                        }else{
                            emitter.onError(task.getException());
                        }
                    }
                })
                /*.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        emitter.onError(e);
                    }
                })*/;
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> loginWithCredentials(AuthCredential credential) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("UserRepository:","Credentials sign in success");
                            emitter.onNext(new Boolean(true));
                            emitter.onComplete();
                        }else{
                            Log.d("UserRepository:","Credentials sign in failed " + task.getException().getMessage());
                            emitter.onError(task.getException());
                        }
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public void logout() {
        mAuth.signOut();
    }

    @Override
    public Flowable<Boolean> liveLoginState() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                FirebaseAuth.AuthStateListener authStateListener;
                authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getInstance().getCurrentUser()==null){
                            emitter.onNext(false);
                        }else{
                            emitter.onNext(true);
                        }
                    }
                };
                mAuth.addAuthStateListener(authStateListener);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        mAuth.removeAuthStateListener(authStateListener);
                        emitter.onComplete();
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> sendVerificationEmail(String emailId) {
        if(mCurrentUser.getUser_email().equalsIgnoreCase(emailId)){
            return sendVerificationEmail();
        }else{
            return updateEmail(emailId)
                    .flatMap(new Function<Boolean, Publisher<Boolean>>() {
                        @Override
                        public Publisher<Boolean> apply(Boolean aBoolean) throws Exception {
                            if(aBoolean==true){
                                return sendVerificationEmail();
                            }else{
                                return Flowable.just(new Boolean(false));
                            }
                        }
                    });
        }
    }

    @Override
    public Flowable<Boolean> sendVerificationEmail() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                if(mCurrentFirebaseUser!=null){
                    mCurrentFirebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                emitter.onNext(new Boolean(true));
                                emitter.onComplete();
                            }else{
                                emitter.onError(task.getException());
                            }
                        }
                    });
                }
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> updateEmail(String emailId) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                mCurrentFirebaseUser.updateEmail(emailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            emitter.onNext(new Boolean(true));
                            emitter.onComplete();
                        }else{
                            emitter.onError(task.getException());
                        }
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> currentUserReload() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                mCurrentFirebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            emitter.onNext(new Boolean(true));
                            emitter.onComplete();
                        }else{
                            emitter.onError(task.getException());
                        }
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }


    @Override
    public void syncCurrentUser() {
        mDatabaseUsers.child(mCurrentFirebaseUser.getUid()).addValueEventListener(mUserValueEventListener);
    }

    @Override
    public Flowable<UserPublic> getAllUsersStartingName(String name) {
        return Flowable.create(new FlowableOnSubscribe<UserPublic>() {
            @Override
            public void subscribe(FlowableEmitter<UserPublic> emitter) throws Exception {
                mDatabaseUsersPublic.orderByChild("user_name_lower").startAt(name).endAt(name+"~").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot pDataSnapshot : dataSnapshot.getChildren()){
                                UserPublic user = pDataSnapshot.getValue(UserPublic.class);
                                if(!user.getUser_id().equals(mCurrentUser.getUser_id())) {
                                    emitter.onNext(user);
                                }
                            }
                            emitter.onComplete();
                        }else {
                            emitter.onComplete();
                            Log.d("UserRepository:","Sear User Datasnapshot empty");
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
    public Flowable<String> setFCMAppIdToken() {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("UserRepository", "getInstanceId success");
                            String appId = task.getResult().getId();
                            String token = task.getResult().getToken();
                            String appIdToken = new String(token);
                            mUniqueAppId = appId;
                            Disposable d = setFCMtoken(appId,token)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(()->{
                                        emitter.onComplete();
                                    });
                            emitter.onNext(appIdToken);
                        }else{
                            Log.d("UserRepository", "getInstanceId failed", task.getException());
                            emitter.onError(task.getException());
                        }
                    }
                });
            }
        },BackpressureStrategy.BUFFER);
    }

    @Override
    public String getAppId() {
        return mUniqueAppId;
    }

    @Override
    public void cleanUp() {
        mAuth.removeAuthStateListener(mAuthStateListener);
        mDatabaseUsers.child(mCurrentFirebaseUser.getUid()).removeEventListener(mUserValueEventListener);
    }

}
