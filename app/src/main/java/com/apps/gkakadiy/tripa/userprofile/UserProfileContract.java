package com.apps.gkakadiy.tripa.userprofile;

import com.apps.gkakadiy.tripa.data.FriendStatusType;

public interface UserProfileContract {
    interface UserProfileBaseView {
        void setCoverImage(String url);
        void setProfileImage(String url);
        void setFriendRequestIcon(int resId);
        void setFriendRequestText(String text);
        void setAboutText(String about);
        void showAboutText();
        void hideAboutText();
        void setDisplayName(String diplay_name);
        void setTripCount(String count);
        void setFriendCount(String count);
        void setFriendStatus(FriendStatusType status);
        FriendStatusType getFriendStatusType();
    }

    interface UserProfileBasePresenter {
        void loadUser(String userId);
        void isUserFriend(String userId);
        void sendFriendRequest();
        void unsubscribe();
    }
}
