package com.apps.gkakadiy.tripa.launcher;

import com.apps.gkakadiy.tripa.data.User;
import com.google.firebase.auth.FirebaseUser;

public interface LauncherContract {
    interface LauncherBaseView {
        void loginSuccess();
        void notLoggedIn();
        void loadCurrentUser();
        void showProgressbar();
        void hideProgressbar();
    }

    interface LauncherBasePresenter {
        void checkLogin();
        FirebaseUser getCurrentFirebaseUser();
        void unsubscribe();
        void loadCurrentUser();
        User getCurrentUser();
    }
}
