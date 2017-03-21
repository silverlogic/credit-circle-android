package com.tsl.baseapp.model.objects.user;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.tsl.baseapp.R;
import com.tsl.baseapp.utils.Constants;

/**
 * Created by Kevin Lavi on 3/16/17.
 */

public class SocialAuth {
    @SerializedName("provider")
    private String provider;

    @SerializedName("code")
    private String code;

    @SerializedName("redirect_uri")
    private String redirectURL;

    @SerializedName("oauth_token")
    private String oauthToken;

    @SerializedName("oauth_token_secret")
    private String oauthTokenSecret;

    @SerializedName("oauth_verifier")
    private String oauthTokenVerifier;

    @SerializedName("oauth_callback_confirmed")
    private String oauthVerifiedConfirmed;

    @SerializedName("email")
    private String email;

    public static SocialAuth forFacebook(Context context, String code) {
        SocialAuth socialAuth = new SocialAuth();
        socialAuth.code = code;
        socialAuth.redirectURL = Constants.REDIRECT_URL;
        socialAuth.provider = "facebook";
        return socialAuth;
    }

    public static SocialAuth forLinkedIn(Context context, String code) {
        SocialAuth socialAuth = new SocialAuth();
        socialAuth.code = code;
        socialAuth.redirectURL = Constants.REDIRECT_URL;
        socialAuth.provider = "linkedin-oauth2";
        return socialAuth;
    }

    public static SocialAuth forLinkedInTwitter(String oauthToken, String oauthTokenSecret, String oauthTokenVerifier) {
        SocialAuth socialAuth = new SocialAuth();
        socialAuth.provider = "twitter";
        socialAuth.oauthToken = oauthToken;
        socialAuth.oauthTokenSecret = oauthTokenSecret;
        socialAuth.oauthTokenVerifier = oauthTokenVerifier;
        return socialAuth;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }

    public void setOauthTokenSecret(String oauthTokenSecret) {
        this.oauthTokenSecret = oauthTokenSecret;
    }

    public String getOauthTokenVerifier() {
        return oauthTokenVerifier;
    }

    public void setOauthTokenVerifier(String oauthTokenVerifier) {
        this.oauthTokenVerifier = oauthTokenVerifier;
    }

    public String getOauthVerifiedConfirmed() {
        return oauthVerifiedConfirmed;
    }

    public void setOauthVerifiedConfirmed(String oauthVerifiedConfirmed) {
        this.oauthVerifiedConfirmed = oauthVerifiedConfirmed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
