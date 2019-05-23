package com.apps.gkakadiy.tripa.data.localDB;

import android.app.Application;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class LocalDBRepository implements LocalRepositoryContract {

    private static LocalDBRepository INSTANCE = null;
    private LocalDatabase localDatabase;
    private LocalDBDAO dbDao;

    private LocalDBRepository(Application application) {
        localDatabase = LocalDatabase.getInstance(application);
        dbDao = localDatabase.dbDao();
    }

    public static LocalDBRepository getInstance(Application application){
        if(INSTANCE == null){
            INSTANCE = new LocalDBRepository(application);
        }
        return INSTANCE;
    }


    @Override
    public Completable insertTrip(TripEO tripEO) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbDao.insertTrip(tripEO);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable updateTrip(TripEO tripEO) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbDao.updateTrip(tripEO);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable deleteTrip(TripEO tripEO) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbDao.deleteTrip(tripEO);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Flowable<TripEO> getActiveTrip() {
        return Flowable.create(new FlowableOnSubscribe<TripEO>() {
            @Override
            public void subscribe(FlowableEmitter<TripEO> emitter) throws Exception {
                TripEO tripEO = dbDao.getActiveTrip();
                emitter.onNext(tripEO);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable insertPhoto(PhotoEO photoEO) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbDao.insertPhoto(photoEO);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable updatePhoto(PhotoEO photoEO) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbDao.updatePhoto(photoEO);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Completable deletePhoto(PhotoEO photoEO) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                dbDao.deletePhoto(photoEO);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Flowable<PhotoEO> getAllPhotosOfTrip(String tripId) {
        return Flowable.create(new FlowableOnSubscribe<PhotoEO>() {
            @Override
            public void subscribe(FlowableEmitter<PhotoEO> emitter) throws Exception {
                List<PhotoEO> photoEOList = dbDao.getAllPhotosOfTrip(tripId);
                for (PhotoEO photoEO: photoEOList) {
                    emitter.onNext(photoEO);
                }
                emitter.onComplete();
            }
        },BackpressureStrategy.BUFFER);
    }
}
