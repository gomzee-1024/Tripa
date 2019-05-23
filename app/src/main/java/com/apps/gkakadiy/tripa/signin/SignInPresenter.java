package com.apps.gkakadiy.tripa.signin;

import android.util.Log;

import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.apps.gkakadiy.tripa.data.source.user.UserRepository;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignInPresenter implements SigninContract.SigninBasePresenter {

    private UserRepository mUserRepository;

    private SigninContract.SigninView mSigninView;

    private CompositeDisposable mCompositeDisposable;
    public SignInPresenter(UserRepository userRepository,SigninContract.SigninView signinView){
        mUserRepository = userRepository;
        mSigninView = signinView;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void signIn(String userId, String password) {
        //mSigninView.showProgressDialog(Constants.SIGNING_IN_MESSAGE,null, ProgressDialogType.NO_BUTTON);
        mSigninView.showProgressBar();
        mSigninView.disableButtons();
        Disposable d = mUserRepository.login(userId,password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    mSigninView.hideProgressBar();
                    mSigninView.enableButtons();
                    if(result.booleanValue()==true){
                        mSigninView.signInSuccess();
                    }else{
                        mSigninView.showSnackBar("Sigh In Failed", Snackbar.LENGTH_LONG);
                    }
                },throwable -> {
                    Log.d("SignInPresenter:",throwable.getMessage());
                    mSigninView.hideProgressBar();
                    mSigninView.enableButtons();
                    if (throwable instanceof FirebaseAuthInvalidCredentialsException) {
                        String errorCode =
                                ((FirebaseAuthInvalidCredentialsException) throwable).getErrorCode();
                        Log.d("SignInPresenter: ",errorCode);
                        if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                            mSigninView.showInvalidEmail();
                        }else {
                            mSigninView.showIncorrectPassword();
                        }
                    } else if (throwable instanceof FirebaseAuthInvalidUserException) {
                        String errorCode =
                                ((FirebaseAuthInvalidUserException) throwable).getErrorCode();
                        if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                            mSigninView.showIncorrectUsername();
                        } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                            mSigninView.showAccountDisabled();
                        } else {
                            mSigninView.showSnackBar("Sigh In Failed", Snackbar.LENGTH_LONG);
                        }
                    }else {
                        mSigninView.showSnackBar("Sigh In Failed", Snackbar.LENGTH_LONG);
                    }
                });
        mCompositeDisposable.add(d);
    }

    @Override
    public void signIn(AuthCredential authCredential,User user) {
        if(mSigninView==null){
            Log.d("SignInPresenter:","nSignInView null");
        }
        //mSigninView.showProgressDialog(Constants.SIGNING_IN_MESSAGE,null, ProgressDialogType.NO_BUTTON);
        mSigninView.showProgressBar();
        mSigninView.disableButtons();
        Disposable d = mUserRepository.loginWithCredentials(authCredential)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        if(result.booleanValue()==true){
                            Log.d("SignInPresenter:","SignIn credential success");
                            checkUser(user);
                        }else{
                            mSigninView.hideProgressBar();
                            mSigninView.enableButtons();
                            Log.d("SignInPresenter:","SignIn credential failed");
                            mSigninView.showSnackBar("Sigh In Failed", Snackbar.LENGTH_LONG);
                        }
                    },throwable -> {
                        mSigninView.hideProgressBar();
                        mSigninView.enableButtons();
                        Log.d("SignInPresenter:","SignIn credential failed exception");
                        mSigninView.showSnackBar("Sigh In Failed", Snackbar.LENGTH_LONG);
                    });
        mCompositeDisposable.add(d);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mSigninView =null;
    }

    @Override
    public void checkUser(User user) {
        Log.d("SignInPresenter:","checkUser");
        FirebaseUser firebaseUser = getCurrentFirebaseUser();
        user.setUser_id(firebaseUser.getUid());
        Disposable d = mUserRepository.getUser(user.getUser_id())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user1 -> {
                           if(user1.getUser_id()==null){
                               Log.d("SignInPresenter:","Credentails user not present in DB");
                               addUserToDB(user);
                           }else{
                               mSigninView.hideProgressBar();
                               mSigninView.enableButtons();
                               Log.d("SignInPresenter:","Credentails user present in DB");
                               mSigninView.signInSuccess();
                           }
                        },throwable -> {
                            mSigninView.hideProgressBar();
                            mSigninView.enableButtons();
                            Log.d("SignInPresenter:","getUser Exception");
                        });
        mCompositeDisposable.add(d);
    }

    @Override
    public void addUserToDB(User user) {
        Disposable d = mUserRepository.addUserToDB(user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user1 -> {
                            if(user1!=null){
                                Log.d("SignInPresenter:","Google sign In new user added to DB");
                                UserPublic userPublic = new UserPublic();
                                userPublic.copyUserPublicData(user1);
                                addUserPublicToDB(userPublic);
                            }else{
                                mSigninView.hideProgressBar();
                                mSigninView.enableButtons();
                                Log.d("SignInPresenter:","Google sign In new user failed to DB");
                            }
                        },throwable -> {
                            mSigninView.hideProgressBar();
                            mSigninView.enableButtons();
                            Log.d("SignInPresenter:","Google sign In new user failed to DB");
                        });
        mCompositeDisposable.add(d);
    }

    @Override
    public void addUserPublicToDB(UserPublic user) {
        Disposable d = mUserRepository.addUserPublicToDB(user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(userp ->{
                            mSigninView.hideProgressBar();
                            mSigninView.enableButtons();
                            if(userp!=null){
                                Log.d("SignInPresenter:","Google sign In new user public added to DB");
                                mSigninView.signInSuccess();
                            }else{
                                Log.d("SignInPresenter:","Google sign In new user public failed to DB");
                            }
                        },throwable -> {
                            mSigninView.hideProgressBar();
                            mSigninView.enableButtons();
                            Log.d("SignInPresenter:","Google sign In new user public failed to DB");
                        });
        mCompositeDisposable.add(d);
    }

    @Override
    public FirebaseUser getCurrentFirebaseUser() {
        return mUserRepository.getCurrentFirebaseUser();
    }
}
