package com.apps.gkakadiy.tripa.home;

public interface HomeContract {
    interface HomeBaseView {
        void signOutSuccess();
    }

    interface HomeBasePresenter {
        void signOut();
        void subscribe();
        void unsubscribe();
        void setOnDisconnect();
        void setOnline();
        void setFCMToken();
    }
}
