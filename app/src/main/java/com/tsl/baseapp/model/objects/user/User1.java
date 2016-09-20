package com.tsl.baseapp.model.objects.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kevin Lavi on 9/19/16.
 */

public class User1 {
    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("is_email_confirmed")
    private boolean is_email_confirmed;

    @SerializedName("new_email")
    private String new_email;

    @SerializedName("is_new_email_confirmed")
    private boolean is_new_email_confirmed;

    @SerializedName("referral_code")
    private String referral_code;

    @SerializedName("avatar")
    private UserImages userImages;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    private String current_password;
    private String new_password;

    public User1(){};

    public void register(String email, String password, String first_name, String last_name){
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public void register(String email, String password, String first_name, String last_name, String referral_code){
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.referral_code = referral_code;
    }

    public void login(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void changePassword(String current_password, String new_password){
        this.current_password = current_password;
        this.new_password = new_password;
    }

    public void changeEmail(String new_email){
        this.new_email = new_email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean is_email_confirmed() {
        return is_email_confirmed;
    }

    public void setIs_email_confirmed(boolean is_email_confirmed) {
        this.is_email_confirmed = is_email_confirmed;
    }

    public String getNew_email() {
        return new_email;
    }

    public void setNew_email(String new_email) {
        this.new_email = new_email;
    }

    public boolean is_new_email_confirmed() {
        return is_new_email_confirmed;
    }

    public void setIs_new_email_confirmed(boolean is_new_email_confirmed) {
        this.is_new_email_confirmed = is_new_email_confirmed;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public UserImages getUserImages() {
        return userImages;
    }

    public void setUserImages(UserImages userImages) {
        this.userImages = userImages;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
