package com.apps.gkakadiy.tripa.signup;

import android.util.Log;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.data.LoginType;
import com.apps.gkakadiy.tripa.data.ProgressDialogType;
import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.data.source.user.UserRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpPresenter implements SignupContract.SignupBasePresenter {

    private UserRepository mUserRepository;

    private SignupContract.SignupView mSignupView;

    private SignupContract.SignupView mEmailVerifyView;

    private static SignUpPresenter INSTANCE = null;

    private CompositeDisposable mCompositeDisposable;
    public SignUpPresenter(UserRepository userRepository, SignupContract.SignupView signupView, SignupContract.SignupView emailVerifyView){
        mUserRepository = userRepository;
        mSignupView = signupView;
        mEmailVerifyView = emailVerifyView;
        mCompositeDisposable = new CompositeDisposable();
    }

    public static SignUpPresenter getSignUpPresenterInstance(UserRepository userRepository, SignupContract.SignupView signupView, SignupContract.SignupView emailVerifyView){
        if(INSTANCE!=null){

            return INSTANCE;
        }else{
            INSTANCE = new SignUpPresenter(userRepository,signupView,emailVerifyView);
            return INSTANCE;
        }
    }

    public void setmEmailVerifyView(SignupContract.SignupView mEmailVerifyView) {
        this.mEmailVerifyView = mEmailVerifyView;
    }

    @Override
    public void signUp(User user, String password) {
        //mSignupView.showProgressDialog(Constants.SIGNING_UP_MESSAGE,null, ProgressDialogType.NO_BUTTON);
        mSignupView.showProgressBar();
        mSignupView.disableButtons();
        Disposable d = mUserRepository.createUser(user,password, LoginType.EMAIL)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user1 -> {
                    //mSignupView.hideProgressDialog();
                    mSignupView.hideProgessBar();
                    mSignupView.enableButtons();
                    mSignupView.signUpSuccess();
                },throwable -> {
                    //mSignupView.hideProgressDialog();
                    mSignupView.hideProgessBar();
                    mSignupView.enableButtons();
                    Log.d("SignUpPresenter:",throwable.getMessage());
                    if(throwable instanceof FirebaseAuthWeakPasswordException){
                        mSignupView.showWeakPassword();
                    }else if(throwable instanceof FirebaseAuthInvalidCredentialsException){
                        String errorCode = ((FirebaseAuthInvalidCredentialsException) throwable).getErrorCode();
                        if(errorCode.equals("ERROR_INVALID_EMAIL")) {
                            mSignupView.showInvalidEmail();
                        }else{
                            mSignupView.showIncorrectEmail();
                        }
                    }else if(throwable instanceof FirebaseAuthUserCollisionException){
                        mSignupView.showUserAlreadyExist();
                    }else {
                        mSignupView.showSnackBar("Sign Up Failed", Snackbar.LENGTH_LONG);
                    }
                });
        mCompositeDisposable.add(d);
    }

    @Override
    public void sendEmailVerification() {
        //mEmailVerifyView.showProgressDialog("Account Verification Email","Sending..",ProgressDialogType.NO_BUTTON);
        if(mEmailVerifyView!=null) {
            mEmailVerifyView.showProgressBar();
            mEmailVerifyView.disableButtons();
        }
        Disposable d = mUserRepository.sendVerificationEmail()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            //mEmailVerifyView.hideProgressDialog();
                            if(mEmailVerifyView!=null) {
                                mEmailVerifyView.hideProgessBar();
                                mEmailVerifyView.enableButtons();;
                            }
                            if(result==true){
                                Log.d("SignUpPresenter:","Verification Email Sent");
                                if(mEmailVerifyView!=null) {
                                    mEmailVerifyView.showEmailResent();
                                }
                            }else{
                                Log.d("SignUpPresenter:","Verification Email Sent Failed");
                                if(mEmailVerifyView!=null) {
                                    mEmailVerifyView.showEmailResendFailed();
                                }
                            }
                        },throwable -> {
                            //mEmailVerifyView.hideProgressDialog();
                            if(mEmailVerifyView!=null) {
                                mEmailVerifyView.hideProgessBar();
                                mEmailVerifyView.enableButtons();
                            }
                            if(mEmailVerifyView!=null) {
                                mEmailVerifyView.showEmailResendFailed();
                            }
                        });
        mCompositeDisposable.add(d);
    }

    @Override
    public void sendUpdateEmailVerification(String emaiId) {
        //mEmailVerifyView.showProgressDialog("Account Verification Email","Sending..",ProgressDialogType.NO_BUTTON);
        mEmailVerifyView.showProgressBar();
        mEmailVerifyView.disableButtons();
        Disposable d = mUserRepository.sendVerificationEmail(emaiId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            //mEmailVerifyView.hideProgressDialog();
                            mEmailVerifyView.hideProgessBar();
                            mEmailVerifyView.enableButtons();
                            if(result==true){
                                Log.d("SignUpPresenter:","Verification Email Sent");
                                mEmailVerifyView.showEmailResent();
                            }else{
                                Log.d("SignUpPresenter:","Verification Email Sent Failed");
                                mEmailVerifyView.showEmailResendFailed();
                            }
                        },throwable -> {
                            //mEmailVerifyView.hideProgressDialog();
                            mEmailVerifyView.hideProgessBar();
                            mEmailVerifyView.enableButtons();
                            Log.d("SignUpPresenter:","Verification Email Sent Failed");
                            mEmailVerifyView.showEmailResendFailed();
                        });
        mCompositeDisposable.add(d);
    }

    @Override
    public FirebaseUser getCurrentFirebaseUser() {
        return mUserRepository.getCurrentFirebaseUser();
    }

    @Override
    public void currentUserReload() {
        //mEmailVerifyView.showProgressDialog("Account Verification Email","Sending..",ProgressDialogType.NO_BUTTON);
        mEmailVerifyView.showProgressBar();
        mEmailVerifyView.disableButtons();
        Disposable d = mUserRepository.currentUserReload()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            //mEmailVerifyView.hideProgressDialog();
                            mEmailVerifyView.hideProgessBar();
                            mEmailVerifyView.enableButtons();
                            if(result==true){
                                Log.d("SignUpPresenter:","User reload successfull");
                                mEmailVerifyView.currentUserReloadSuccess();
                            }else{
                                Log.d("SignUpPresenter:","User reload unsuccessfull");
                            }
                        },throwable -> {
                            //mEmailVerifyView.hideProgressDialog();
                            mEmailVerifyView.hideProgessBar();
                            mEmailVerifyView.enableButtons();
                            Log.d("SignUpPresenter:","User reload unsuccessfull");
                        });
        mCompositeDisposable.add(d);
    }

    @Override
    public void unsubscribeSignUp() {
        mCompositeDisposable.clear();
        mSignupView = null;
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mSignupView = null;
        mEmailVerifyView = null;
    }
}
