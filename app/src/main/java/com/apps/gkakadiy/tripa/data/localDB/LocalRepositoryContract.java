package com.apps.gkakadiy.tripa.data.localDB;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface LocalRepositoryContract {

    Completable insertTrip(TripEO tripEO);
    Completable updateTrip(TripEO tripEO);
    Completable deleteTrip(TripEO tripEO);
    Flowable<TripEO> getActiveTrip();

    Completable insertPhoto(PhotoEO photoEO);
    Completable updatePhoto(PhotoEO photoEO);
    Completable deletePhoto(PhotoEO photoEO);
    Flowable<PhotoEO> getAllPhotosOfTrip(String tripId);
}
