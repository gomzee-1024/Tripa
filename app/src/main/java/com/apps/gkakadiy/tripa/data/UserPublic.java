package com.apps.gkakadiy.tripa.data;

public class UserPublic extends User {
    private String user_id;
    private String user_name;
    private String user_name_lower;
    private String user_email;
    private String user_profile_pic_url;
    private String user_about;
    private String user_birth_date;
    private String user_cover_pic_url;
    private Boolean user_online;
    private String user_fcm_token;

    @Override
    public String getUser_fcm_token() {
        return user_fcm_token;
    }

    @Override
    public void setUser_fcm_token(String user_fcm_token) {
        this.user_fcm_token = user_fcm_token;
    }

    @Override
    public String getUser_name_lower() {
        return user_name_lower;
    }

    @Override
    public void setUser_name_lower(String user_name_lower) {
        this.user_name_lower = user_name_lower;
    }

    @Override
    public String getUser_id() {
        return user_id;
    }

    @Override
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String getUser_name() {
        return user_name;
    }

    @Override
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String getUser_email() {
        return user_email;
    }

    @Override
    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    @Override
    public String getUser_profile_pic_url() {
        return user_profile_pic_url;
    }

    @Override
    public void setUser_profile_pic_url(String user_profile_pic_url) {
        this.user_profile_pic_url = user_profile_pic_url;
    }

    @Override
    public String getUser_about() {
        return user_about;
    }

    @Override
    public void setUser_about(String user_about) {
        this.user_about = user_about;
    }

    @Override
    public String getUser_birth_date() {
        return user_birth_date;
    }

    @Override
    public void setUser_birth_date(String user_birth_date) {
        this.user_birth_date = user_birth_date;
    }

    @Override
    public String getUser_cover_pic_url() {
        return user_cover_pic_url;
    }

    @Override
    public void setUser_cover_pic_url(String user_cover_pic_url) {
        this.user_cover_pic_url = user_cover_pic_url;
    }

    @Override
    public Boolean getUser_online() {
        return user_online;
    }

    @Override
    public void setUser_online(Boolean user_online) {
        this.user_online = user_online;
    }

    public void copyUserPublicData(User user){
        user_id = user.getUser_id();
        user_name = user.getUser_name();
        user_name_lower = user.getUser_name_lower();
        user_email = user.getUser_email();
        user_profile_pic_url = user.getUser_profile_pic_url();
        user_about = user.getUser_about();
        user_birth_date = user.getUser_birth_date();
        user_cover_pic_url = user.getUser_cover_pic_url();
        user_fcm_token = user.getUser_fcm_token();
        user_online = user.getUser_online();
    }
}
