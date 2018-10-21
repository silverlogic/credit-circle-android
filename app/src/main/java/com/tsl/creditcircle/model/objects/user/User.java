package com.tsl.creditcircle.model.objects.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Kevin Lavi on 9/19/16.
 */

@RealmClass
public class User extends RealmObject implements Serializable {
    @SerializedName("id")
    @PrimaryKey
    int id;

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    @SerializedName("is_email_confirmed")
    boolean is_email_confirmed;

    @SerializedName("new_email")
    String new_email;

    @SerializedName("is_new_email_confirmed")
    boolean is_new_email_confirmed;

    @SerializedName("referral_code")
    String referral_code;

    @SerializedName("avatar")
    UserImages userImages;

    @SerializedName("first_name")
    String first_name;

    @SerializedName("last_name")
    String last_name;

    String current_password;
    String new_password;
    String token;

    public User(){};

    public void register(String email, String password){
        this.email = email;
        this.password = password;
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

    public void changePasswordFromForgotPassword(String token, String new_password){
        this.token = token;
        this.new_password = new_password;
    }

    public User testUser(String first_name, String last_name, UserImages userImages){
        User user = new User();
        user.first_name = first_name;
        user.last_name = last_name;
        user.userImages = userImages;
        return user;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
