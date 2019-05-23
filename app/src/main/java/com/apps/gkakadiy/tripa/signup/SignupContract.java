package com.apps.gkakadiy.tripa.signup;

import com.apps.gkakadiy.tripa.data.ProgressDialogType;
import com.apps.gkakadiy.tripa.data.User;
import com.google.firebase.auth.FirebaseUser;

public interface SignupContract {
    interface SignupView {
        void showLoadingPopup();
        void showSnackBar(String message,int period);
        void signUpSuccess();
        void showWeakPassword();
        void showInvalidEmail();
        void showIncorrectEmail();
        void showUserAlreadyExist();
        void showProgressDialog(String title, String message, ProgressDialogType progressDialogType);
        void hideProgressDialog();
        void showProgressBar();
        void hideProgessBar();
        void resendVerificationEmail();
        void verifyEmailSuccess();
        void showEmailResent();
        void showEmailResendFailed();
        void currentUserReloadSuccess();
        void disableButtons();
        void enableButtons();
    }

    interface SignupBasePresenter {
        void signUp(User user,String password);
        void sendEmailVerification();
        void sendUpdateEmailVerification(String emaiId);
        FirebaseUser getCurrentFirebaseUser();
        void currentUserReload();
        void unsubscribeSignUp();
        void unsubscribe();
    }
}
