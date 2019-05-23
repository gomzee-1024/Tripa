package com.apps.gkakadiy.tripa.data.source.user;

import com.apps.gkakadiy.tripa.data.Notification;
import com.apps.gkakadiy.tripa.data.Request;
import com.apps.gkakadiy.tripa.data.UserPublic;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface UserNotificationsDataSource {
    Flowable<Notification> getAllNotifications(String userId);
    Completable addNotification(String userToId,Notification notification);
    Completable readNotification(String notificationID,String userId);
    Completable deleteNotification(String userId,String notificationId);
    Flowable<Request> getRequest(String requestId,String userId);
}
