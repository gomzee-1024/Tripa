package com.apps.gkakadiy.tripa.data.localDB;

import androidx.room.Entity;

@Entity(tableName = "trips")
public class TripEO {

    private String trip_id;
    private String trip_status;
    private String trip_start_date;
    private String trip_mode;

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public String getTrip_start_date() {
        return trip_start_date;
    }

    public void setTrip_start_date(String trip_start_date) {
        this.trip_start_date = trip_start_date;
    }

    public String getTrip_mode() {
        return trip_mode;
    }

    public void setTrip_mode(String trip_mode) {
        this.trip_mode = trip_mode;
    }
}
