package com.tsl.baseapp.model.Objects.user;

/**
 * Created by Kevin on 10/15/15.
 */
public class User {

    private String username;
    private String email;
    private String password;
    private String confirm_password;
    public String first_name;
    private String last_name;
    private int id;
    private String slug;
    private String facebook;
    private String instagram;
    private String linkedin;
    private String tagline;
    private String img_url;
    private String old_password;
    private String new_password;
    private String confirm_new_password;
    private String access_token;
    private String backend;

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(){}

    public User(String email, String password, String confirm_password, String first_name, String last_name){
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = email;
    }

    public User(String old_password, String new_password, String confirm_new_password){
        this.old_password = old_password;
        this.new_password = new_password;
        this.confirm_new_password = confirm_new_password;
    }

    public User(String access_token, String backend){
        this.access_token = access_token;
        this.backend = backend;
    }

}
