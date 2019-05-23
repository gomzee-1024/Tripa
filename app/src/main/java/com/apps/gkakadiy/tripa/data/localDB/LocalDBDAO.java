package com.apps.gkakadiy.tripa.data.localDB;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocalDBDAO {

    @Insert
    void insertTrip(TripEO tripEO);

    @Update
    void updateTrip(TripEO tripEO);

    @Delete
    void deleteTrip(TripEO tripEO);

    @Query("SELECT * from trips where status = 'ACTIVE'")
    TripEO getActiveTrip();

    @Insert
    void insertPhoto(PhotoEO photoEO);

    @Update
    void updatePhoto(PhotoEO photoEO);

    @Delete
    void deletePhoto(PhotoEO photoEO);

    @Query("SELECT * from photo_status where trip_id = :tripId")
    List<PhotoEO> getAllPhotosOfTrip(String tripId);
}
