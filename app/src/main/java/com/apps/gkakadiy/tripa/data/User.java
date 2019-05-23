package com.apps.gkakadiy.tripa.data;

import java.util.HashMap;
import java.util.List;

public class User {
    private String user_id;
    private String user_name;
    private String user_name_lower;
    private String user_email;
    private String user_profile_pic_url;
    private String user_about;
    private String user_birth_date;
    private String user_cover_pic_url;
    private Boolean user_online;
    private String user_login_type;
    private String user_created_at;
    private String user_last_login_at;
    private String user_places;
    private String user_share_type_default;
    private String user_share_mode_default;
    private String user_fcm_token;
    private HashMap<String,FCMToken> user_app_ids;

    public HashMap<String, FCMToken> getUser_app_ids() {
        return user_app_ids;
    }

    public void setUser_app_ids(HashMap<String, FCMToken> user_app_ids) {
        this.user_app_ids = user_app_ids;
    }

    public String getUser_fcm_token() {
        return user_fcm_token;
    }

    public void setUser_fcm_token(String user_fcm_token) {
        this.user_fcm_token = user_fcm_token;
    }

    public String getUser_name_lower() {
        return user_name_lower;
    }

    public void setUser_name_lower(String user_name_lower) {
        this.user_name_lower = user_name_lower;
    }

    public String getUser_places() {
        return user_places;
    }

    public void setUser_places(String user_places) {
        this.user_places = user_places;
    }

    public String getUser_login_type() {
        return user_login_type;
    }

    public void setUser_login_type(String user_login_type) {
        this.user_login_type = user_login_type;
    }

    public String getUser_created_at() {
        return user_created_at;
    }

    public void setUser_created_at(String user_created_at) {
        this.user_created_at = user_created_at;
    }

    public String getUser_last_login_at() {
        return user_last_login_at;
    }

    public void setUser_last_login_at(String user_last_login_at) {
        this.user_last_login_at = user_last_login_at;
    }

    public String getUser_share_type_default() {
        return user_share_type_default;
    }

    public void setUser_share_type_default(String user_share_type_default) {
        this.user_share_type_default = user_share_type_default;
    }

    public String getUser_share_mode_default() {
        return user_share_mode_default;
    }

    public void setUser_share_mode_default(String user_share_mode_default) {
        this.user_share_mode_default = user_share_mode_default;
    }

    public Boolean getUser_online() {
        return user_online;
    }

    public void setUser_online(Boolean user_online) {
        this.user_online = user_online;
    }

    public String getUser_cover_pic_url() {
        return user_cover_pic_url;
    }

    public void setUser_cover_pic_url(String user_cover_pic_url) {
        this.user_cover_pic_url = user_cover_pic_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profile_pic_url() {
        return user_profile_pic_url;
    }

    public void setUser_profile_pic_url(String user_profile_pic_url) {
        this.user_profile_pic_url = user_profile_pic_url;
    }

    public String getUser_about() {
        return user_about;
    }

    public void setUser_about(String user_about) {
        this.user_about = user_about;
    }

    public String getUser_birth_date() {
        return user_birth_date;
    }

    public void setUser_birth_date(String user_birth_date) {
        this.user_birth_date = user_birth_date;
    }

}
