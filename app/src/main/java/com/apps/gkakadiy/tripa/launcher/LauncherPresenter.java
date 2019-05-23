package com.apps.gkakadiy.tripa.launcher;

import android.util.Log;

import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.data.source.user.UserRepository;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LauncherPresenter implements LauncherContract.LauncherBasePresenter {

    private UserRepository mUserRepository;

    private LauncherContract.LauncherBaseView mLauncherView;

    private CompositeDisposable mCompositeDisposable;

    public LauncherPresenter(UserRepository userRepository, LauncherContract.LauncherBaseView launcherBaseView){
        mUserRepository = userRepository;
        mLauncherView = launcherBaseView;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void checkLogin() {
        Disposable d = mUserRepository.loginStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    if(status==true) {
                        mLauncherView.loadCurrentUser();
                        //mLauncherView.loginSuccess();
                    }else{
                        mLauncherView.notLoggedIn();
                    }
                },throwable -> {
                    mLauncherView.notLoggedIn();
                });
        mCompositeDisposable.add(d);
    }

    @Override
    public FirebaseUser getCurrentFirebaseUser() {
        return mUserRepository.getCurrentFirebaseUser();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mLauncherView=null;
    }

    @Override
    public void loadCurrentUser() {
        mUserRepository.syncCurrentUser();
        Disposable d = mUserRepository.getCurrentUser()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            mLauncherView.hideProgressbar();
                            mLauncherView.loginSuccess();
                        },throwable -> {
                            Log.d("LauncherPresenter:","Load User Failed");
                            mLauncherView.hideProgressbar();
                            mLauncherView.loginSuccess();
                        });
        mCompositeDisposable.add(d);
    }

    @Override
    public User getCurrentUser() {
        return mUserRepository.getCurrentUserDirect();
    }

}
