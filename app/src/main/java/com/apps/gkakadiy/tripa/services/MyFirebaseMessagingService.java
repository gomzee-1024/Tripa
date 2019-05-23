package com.apps.gkakadiy.tripa.services;


import android.util.Log;

import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.data.source.user.UserRepository;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private UserRepository mUserRepository = Injection.provideUserRepository();
    @Override
    public void onNewToken(String token) {
        Log.d("MessagingService:", "Refreshed token: " + token);
        mUserRepository.setFCMtoken(mUserRepository.getAppId(),token);
    }
}
