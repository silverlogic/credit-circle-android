package com.tsl.baseapp.model.objects.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("email")
    private String email;

    @SerializedName("facebook")
    private String facebook;

    @SerializedName("linkedin")
    private String linkedin;

    @SerializedName("instagram")
    private String instagram;

    @SerializedName("slug")
    private String slug;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("shipping_address")
    private String shipping_address;

    @SerializedName("billing_address")
    private String billing_address;

    @SerializedName("website")
    private String website;

    @SerializedName("credits")
    private double credits;

    @SerializedName("referral_code")
    private String referral_code;

    @SerializedName("new_email")
    private String new_email;

    @SerializedName("is_new_email_confirmed")
    private Boolean is_new_email_confirmed;

    @SerializedName("newsletter")
    private Boolean newsletter;

    @SerializedName("avatar")
    private UserImages userImages;

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

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public String getNew_email() {
        return new_email;
    }

    public void setNew_email(String new_email) {
        this.new_email = new_email;
    }

    public Boolean getIs_new_email_confirmed() {
        return is_new_email_confirmed;
    }

    public void setIs_new_email_confirmed(Boolean is_new_email_confirmed) {
        this.is_new_email_confirmed = is_new_email_confirmed;
    }

    public Boolean getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(Boolean newsletter) {
        this.newsletter = newsletter;
    }

    public UserImages getUserImages() {
        return userImages;
    }

    public void setUserImages(UserImages userImages) {
        this.userImages = userImages;
    }
}
