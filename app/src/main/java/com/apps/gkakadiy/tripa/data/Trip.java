package com.apps.gkakadiy.tripa.data;

import java.util.Map;

public class Trip {
    private String trip_id;
    private String trip_creator_id;
    private String trip_start_date;
    private String trip_stop_date;
    private Map<String,UserPublic> trip_friends;
    private String trip_comments_count;
    private Map<String,Comments> trip_comments;
    private String trip_reactions_count1;
    private String trip_reactions_count2;
    private String trip_reactions_count3;
    private String trip_reactions_count4;
    private String trip_name;
    private String trip_location;
    private String trip_photo_url;
    private String trip_status;

    public String getTrip_creator_id() {
        return trip_creator_id;
    }

    public void setTrip_creator_id(String trip_creator_id) {
        this.trip_creator_id = trip_creator_id;
    }

    public String getTrip_start_date() {
        return trip_start_date;
    }

    public void setTrip_start_date(String trip_start_date) {
        this.trip_start_date = trip_start_date;
    }

    public String getTrip_stop_date() {
        return trip_stop_date;
    }

    public void setTrip_stop_date(String trip_stop_date) {
        this.trip_stop_date = trip_stop_date;
    }

    public Map<String, UserPublic> getTrip_friends() {
        return trip_friends;
    }

    public void setTrip_friends(Map<String, UserPublic> trip_friends) {
        this.trip_friends = trip_friends;
    }

    public Map<String, Comments> getTrip_comments() {
        return trip_comments;
    }

    public void setTrip_comments(Map<String, Comments> trip_comments) {
        this.trip_comments = trip_comments;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getTrip_location() {
        return trip_location;
    }

    public void setTrip_location(String trip_location) {
        this.trip_location = trip_location;
    }

    public String getTrip_photo_url() {
        return trip_photo_url;
    }

    public void setTrip_photo_url(String trip_photo_url) {
        this.trip_photo_url = trip_photo_url;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

}