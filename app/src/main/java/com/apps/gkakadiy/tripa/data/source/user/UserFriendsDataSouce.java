package com.apps.gkakadiy.tripa.data.source.user;

import com.apps.gkakadiy.tripa.data.Request;
import com.apps.gkakadiy.tripa.data.UserPublic;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface UserFriendsDataSouce {
    Flowable<String> getUserFriendCount(String userId);
    Flowable<String> addFriendRequest(Request request);
    Flowable<Request> getRequest(String requestId);
    Completable rejectFriendRequest(String userId,String requestId);
    Completable addFriend(String userId , UserPublic userPublic);
    Completable addFriendMutually(UserPublic user,UserPublic sender);
    Completable acceptFriendRequest(String userId,String senderId,String requestId);
    Completable deleteRequest(Request request);
    Flowable<Boolean> liveFriendStatus(String userId);
    Flowable<Request> liveFriendRequestedStatus(String userId);
    void removeliveFriendStatusListener(String userId);
    void removeliveFriendRequestedListener(String userId);
}
