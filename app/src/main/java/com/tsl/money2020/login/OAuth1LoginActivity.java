package com.tsl.money2020.login;

import android.content.Context;
import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.webkit.CookieManager;

import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.webview.WebViewActivity;

/**
 * Created by Kevin Lavi on 3/20/17.
 */

public class OAuth1LoginActivity extends WebViewActivity {
    public static String VERIFIER = "verifier";
    public static String TOKEN = "token";

    public static Intent getIntent(Context context, String url, String login, int color, int colorDark) {
        Intent intent = new Intent(context, OAuth1LoginActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, login);
        intent.putExtra(Constants.COLOR, color);
        intent.putExtra(Constants.COLOR_DARK, colorDark);
        return intent;
    }

    public static Intent getIntent(Context context, String url, String login) {
        Intent intent = new Intent(context, OAuth1LoginActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, login);
        return intent;
    }

    @Override
    protected boolean onPageStarted(String url) {

        if (url.startsWith(Constants.REDIRECT_URL)) {
            UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
            String verifier = sanitizer.getValue("oauth_verifier");
            String token = sanitizer.getValue("oauth_token");

            Intent result = new Intent();
            result.putExtra(VERIFIER, verifier);
            result.putExtra(TOKEN, token);
            setResult(RESULT_OK, result);
            finish();
            return false;
        }

        return super.onPageStarted(url);
    }

    @Override
    protected void setUpWebView() {
        super.setUpWebView();
        // Clean cookies for facebook login
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
    }
}