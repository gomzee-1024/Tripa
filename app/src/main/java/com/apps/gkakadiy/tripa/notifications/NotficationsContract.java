package com.apps.gkakadiy.tripa.notifications;

import com.apps.gkakadiy.tripa.data.Notification;
import com.apps.gkakadiy.tripa.data.Request;

public interface NotficationsContract {
    interface NotificationsBaseView {
        void addNotification(Notification notification);
        void clearNotifications();
    }

    interface NotificationsBasePresenter{
        void loadAllNotifications();
        void acceptFriendRequest(Notification notification,int position);
        void rejectFriendRequest(Notification notification,int position);
        void deleteNotification(String notificationId);
        void deleteFriendRequest(Request request);
    }
}
