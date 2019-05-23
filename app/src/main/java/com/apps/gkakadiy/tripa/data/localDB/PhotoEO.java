package com.apps.gkakadiy.tripa.data.localDB;

import androidx.room.Entity;

@Entity(tableName = "photo_status")
public class PhotoEO {
    private String trip_id;
    private String photo_uri;
    private String status;

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
