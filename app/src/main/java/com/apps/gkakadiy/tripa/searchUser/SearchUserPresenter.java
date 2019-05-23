package com.apps.gkakadiy.tripa.searchUser;

import android.util.Log;

import com.apps.gkakadiy.tripa.data.source.user.UserRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

public class SearchUserPresenter  implements SearchUserContract.SeachUserBasePresenter{

    private UserRepository mUserRepository;

    private SearchUserContract.SearchUserBaseView mSearchUserView;

    private CompositeDisposable mCompositeDisposable;

    public SearchUserPresenter(UserRepository userRepository, SearchUserContract.SearchUserBaseView searchUserView){
        mUserRepository = userRepository;
        mSearchUserView = searchUserView;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void searchUsers(String name) {
        mSearchUserView.showProgressBar();
        Disposable d = mUserRepository.getAllUsersStartingName(name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            Log.d("SearchUserPresenter: ",user.toString());
                            mSearchUserView.addUserToView(user);
                        },throwable -> {
                            Log.d("SearchUserPresenter: ",throwable.getMessage());
                        }, Functions.notificationOnComplete( notification -> {
                            mSearchUserView.hideProgressBar();
                        }));
        mCompositeDisposable.add(d);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        mSearchUserView=null;
    }
}
