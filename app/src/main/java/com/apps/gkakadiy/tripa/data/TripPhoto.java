package com.apps.gkakadiy.tripa.data;

public class TripPhoto {
    private String photo_id;
    private String trip_id;
    private String user_id;
    private String photo_url;
    private String photo_security_type;
    private String photo_reaction_count1;
    private String photo_reaction_count2;
    private String photo_reaction_count3;
    private String photo_reaction_count4;
    private String photo_comments_count;
    private String creation_date;
    private Boolean favourite_flag;
    private String meta_data;
    private String photo_size;
    private Boolean featured;

    public String getPhoto_security_type() {
        return photo_security_type;
    }

    public void setPhoto_security_type(String photo_security_type) {
        this.photo_security_type = photo_security_type;
    }

    public String getPhoto_reaction_count1() {
        return photo_reaction_count1;
    }

    public void setPhoto_reaction_count1(String photo_reaction_count1) {
        this.photo_reaction_count1 = photo_reaction_count1;
    }

    public String getPhoto_reaction_count2() {
        return photo_reaction_count2;
    }

    public void setPhoto_reaction_count2(String photo_reaction_count2) {
        this.photo_reaction_count2 = photo_reaction_count2;
    }

    public String getPhoto_reaction_count3() {
        return photo_reaction_count3;
    }

    public void setPhoto_reaction_count3(String photo_reaction_count3) {
        this.photo_reaction_count3 = photo_reaction_count3;
    }

    public String getPhoto_reaction_count4() {
        return photo_reaction_count4;
    }

    public void setPhoto_reaction_count4(String photo_reaction_count4) {
        this.photo_reaction_count4 = photo_reaction_count4;
    }

    public String getPhoto_comments_count() {
        return photo_comments_count;
    }

    public void setPhoto_comments_count(String photo_comments_count) {
        this.photo_comments_count = photo_comments_count;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Boolean getFavourite_flag() {
        return favourite_flag;
    }

    public void setFavourite_flag(Boolean favourite_flag) {
        this.favourite_flag = favourite_flag;
    }

    public String getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(String meta_data) {
        this.meta_data = meta_data;
    }

    public String getPhoto_size() {
        return photo_size;
    }

    public void setPhoto_size(String photo_size) {
        this.photo_size = photo_size;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
