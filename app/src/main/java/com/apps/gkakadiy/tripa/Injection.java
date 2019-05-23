package com.apps.gkakadiy.tripa;

import com.apps.gkakadiy.tripa.data.source.user.UserFriendsRepository;
import com.apps.gkakadiy.tripa.data.source.user.UserNotificationsRepository;
import com.apps.gkakadiy.tripa.data.source.user.UserRepository;

public class Injection {
    public static UserRepository provideUserRepository(){
        return UserRepository.getInstance();
    }
    public static UserFriendsRepository provideUserFriendsRepository(){
        return UserFriendsRepository.getInstance();
    }
    public static UserNotificationsRepository provideUserNotificationsRepository(){
        return UserNotificationsRepository.getInstance();
    }
}
