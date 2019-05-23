package com.apps.gkakadiy.tripa.home;

import android.util.Log;

import com.apps.gkakadiy.tripa.data.source.user.UserRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.HomeBasePresenter {

    private UserRepository mUserRepository;

    private HomeContract.HomeBaseView mHomeView;

    private CompositeDisposable mCompositeDisposable;

    public HomePresenter(UserRepository userRepository, HomeContract.HomeBaseView homeBaseView){
        mUserRepository = userRepository;
        mHomeView = homeBaseView;
        mCompositeDisposable = new CompositeDisposable();
    }



    @Override
    public void signOut() {
        mUserRepository.logout();

    }

    @Override
    public void subscribe() {
        Disposable d = mUserRepository.liveLoginState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if(result.booleanValue()==false){
                        mHomeView.signOutSuccess();
                    }
                },throwable -> {

                });
        mCompositeDisposable.add(d);
        mUserRepository.syncCurrentUser();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void setOnDisconnect() {
        mUserRepository.setOnDisconnect();
    }

    @Override
    public void setOnline() {
        mUserRepository.setOnlineStatus();
    }

    @Override
    public void setFCMToken() {
        Disposable d1 = mUserRepository.setFCMAppIdToken()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(appIdToken -> {
                    Log.d("HomePresenter:","appIdToken:"+appIdToken);
                },throwable -> {
                    Log.d("HomePresenter:","appIdToken:"+throwable.getMessage());
                });
        mCompositeDisposable.add(d1);
    }
}
