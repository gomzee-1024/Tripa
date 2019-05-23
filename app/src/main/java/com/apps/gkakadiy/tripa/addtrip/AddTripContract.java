package com.apps.gkakadiy.tripa.addtrip;

import com.apps.gkakadiy.tripa.data.Trip;
import com.apps.gkakadiy.tripa.data.UserPublic;

public class AddTripContract {
    interface AddTripBaseView{
        void addFriend(UserPublic user);
        void showShareModeTextView();
        void hideShareModeTextView();
        void showShareTypeTextView();
        void hideShareTypeTextView();
        void showClearButton();
        void hideClearButton();
        void showTripCreated();
        void showProgressBar();
        void hideProgressBar();
    }

    interface AddTripBasePresenter{
        void getAllFriends(String userId,String searchText);
        void addTrip(String userId, Trip trip);
    }
}
