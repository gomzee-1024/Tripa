package com.apps.gkakadiy.tripa.data.source.user;

import android.net.Uri;

import com.apps.gkakadiy.tripa.data.LoginType;
import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface UserDataSource {
    Flowable<User> getUser(String userId);
    Flowable<UserPublic> getUserPublic(String userId);
    Flowable<User> getCurrentUser();
    Flowable<Boolean> liveConnectionState();
    Completable setOnDisconnect();
    Completable setFCMtoken(String appId,String fcmToken);
    Completable setOnlineStatus();
    User getCurrentUserDirect();
    Flowable<UserPublic> getUserLive(String userId);
    void removeLiveUserValueListener();
    FirebaseUser getCurrentFirebaseUser();
    Flowable<UserPublic> createUser(User user, String password, LoginType loginType);
    Flowable<User> addUserToDB(User user);
    Flowable<UserPublic> addUserPublicToDB(UserPublic userPublic);
    void userAbout(User user,String text);
    void userProfilePic(User user , Uri profilePicUri);
    void userShareType(User user , String shareType);
    void userShareMode(User user, String shareMode);
    Flowable<Boolean> loginStatus();
    Flowable<Boolean> login(String userEmail , String password);
    Flowable<Boolean> loginWithCredentials(AuthCredential credential);
    void logout();
    Flowable<Boolean> liveLoginState();
    Flowable<Boolean> sendVerificationEmail(String emailId);
    Flowable<Boolean> sendVerificationEmail();
    Flowable<Boolean> updateEmail(String emailId);
    Flowable<Boolean> currentUserReload();
    void syncCurrentUser();
    Flowable<UserPublic> getAllUsersStartingName(String name);
    Flowable<String> setFCMAppIdToken();
    String getAppId();
    void cleanUp();
}
