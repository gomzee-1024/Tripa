package com.apps.gkakadiy.tripa.data.source.user;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.MyApplication;
import com.apps.gkakadiy.tripa.data.Trip;
import com.apps.gkakadiy.tripa.data.TripPhoto;
import com.apps.gkakadiy.tripa.data.TripShareType;
import com.google.firebase.database.DatabaseReference;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;

public class UserTripsRepository implements UserTripsDataSource {

    private static UserTripsRepository INSTANCE = null;
    private DatabaseReference mDatabaseTripsPublic,mDatabaseTripsFriends,mDatabaseTripsPrivate,mDatabaseTrips;
    private UserTripsRepository(){
        mDatabaseTripsPublic = MyApplication.getInstance().getmDatabaseReference().child("trips_public");
        mDatabaseTripsFriends = MyApplication.getInstance().getmDatabaseReference().child("trips_friends");
        mDatabaseTripsPrivate = MyApplication.getInstance().getmDatabaseReference().child("trips_private");
        mDatabaseTrips = MyApplication.getInstance().getmDatabaseReference().child("trips");
    }

    public static UserTripsRepository getInstance(){
        if(INSTANCE!=null){
            return INSTANCE;
        }else{
            INSTANCE = new UserTripsRepository();
            return INSTANCE;
        }
    }

    @Override
    public Flowable<Trip> getAllTrips(String userId, String shareType) {
        return null;
    }

    @Override
    public Flowable<Trip> getTrip(String userId, String tripId) {
        return null;
    }

    @Override
    public Flowable<TripPhoto> getAllPhotos(String userId, String tripId) {
        return null;
    }

    @Override
    public Completable addTrip(String userId,Trip trip) {
        return null;
    }
}
