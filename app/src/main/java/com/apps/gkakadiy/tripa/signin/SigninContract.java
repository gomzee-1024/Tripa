package com.apps.gkakadiy.tripa.signin;

import com.apps.gkakadiy.tripa.data.ProgressDialogType;
import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;

public interface SigninContract {
    interface SigninView {
        void showLoadingPopup();
        void showSnackBar(String message,int period);
        void signInSuccess();
        void showIncorrectPassword();
        void showIncorrectUsername();
        void showInvalidEmail();
        void checkUser(User user);
        void showAccountDisabled();
        void showProgressDialog(String title, String message, ProgressDialogType progressDialogType);
        void hideProgressDialog();
        void showProgressBar();
        void hideProgressBar();
        void disableButtons();
        void enableButtons();
    }

    interface SigninBasePresenter {
        void signIn(String userId, String password);
        void signIn(AuthCredential authCredential,User user);
        void unsubscribe();
        void checkUser(User user);
        void addUserToDB(User user);
        void addUserPublicToDB(UserPublic user);
        FirebaseUser getCurrentFirebaseUser();
    }
}
