package com.apps.gkakadiy.tripa.data;

public class Notification {
    private String notification_id;
    private String notification_context;
    private String context_id;
    private String sender_id;
    private String user_id;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private String text5;
    private String creation_date;
    private Boolean seen;
    private String date_user_id;

    public String getDate_user_id() {
        return date_user_id;
    }

    public void setDate_user_id(String date_user_id) {
        this.date_user_id = date_user_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getText5() {
        return text5;
    }

    public void setText5(String text5) {
        this.text5 = text5;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_context() {
        return notification_context;
    }

    public void setNotification_context(String notification_context) {
        this.notification_context = notification_context;
    }

    public String getContext_id() {
        return context_id;
    }

    public void setContext_id(String context_id) {
        this.context_id = context_id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notification_id='" + notification_id + '\'' +
                ", notification_context='" + notification_context + '\'' +
                ", context_id='" + context_id + '\'' +
                ", text1='" + text1 + '\'' +
                ", text2='" + text2 + '\'' +
                ", text3='" + text3 + '\'' +
                ", text4='" + text4 + '\'' +
                ", text5='" + text5 + '\'' +
                ", creation_date='" + creation_date + '\'' +
                ", seen=" + seen +
                '}';
    }
}
