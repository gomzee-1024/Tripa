package com.apps.gkakadiy.tripa.searchUser;

import com.apps.gkakadiy.tripa.data.UserPublic;

public class SearchUserContract {
    interface SearchUserBaseView{
        void showClearButton();
        void hideClearButton();
        void showProgressBar();
        void hideProgressBar();
        void addUserToView(UserPublic user);
    }

    interface SeachUserBasePresenter{
        void searchUsers(String name);
        void unsubscribe();
    }
}
