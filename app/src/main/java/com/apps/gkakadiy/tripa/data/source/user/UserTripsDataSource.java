package com.apps.gkakadiy.tripa.data.source.user;

import com.apps.gkakadiy.tripa.data.Trip;
import com.apps.gkakadiy.tripa.data.TripPhoto;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface UserTripsDataSource {
    Flowable<Trip> getAllTrips(String userId,String shareType);
    Flowable<Trip> getTrip(String userId,String tripId);
    Flowable<TripPhoto> getAllPhotos(String userId,String tripId);
    Completable addTrip(String userId,Trip trip);
}
