package com.tsl.baseapp.model.Objects.user;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class SignUpCredentials {
    String email;
    String password;
    String confirm_password;
    String first_name;
    String last_name;

    public SignUpCredentials(String email, String password, String confirm_password,
                             String first_name, String last_name) {
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
        this.first_name = first_name;
        this.last_name = last_name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
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
