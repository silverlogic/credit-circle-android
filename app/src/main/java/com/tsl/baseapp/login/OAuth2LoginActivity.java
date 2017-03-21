package com.tsl.baseapp.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.UrlQuerySanitizer;
import android.webkit.CookieManager;

import com.tsl.baseapp.R;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.webview.WebViewActivity;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kevin Lavi on 3/17/17.
 */

public class OAuth2LoginActivity extends WebViewActivity {
    public static String AUTH_CODE = "AuthCode";

    public static Intent getIntent(Context context, String url, String login, int color, int colorDark) {
        Intent intent = new Intent(context, OAuth2LoginActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, login);
        intent.putExtra(Constants.COLOR, color);
        intent.putExtra(Constants.COLOR_DARK, colorDark);
        return intent;
    }

    public static Intent getIntent(Context context, String url, String login) {
        Intent intent = new Intent(context, OAuth2LoginActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, login);
        return intent;
    }

    @Override
    protected boolean onPageStarted(String url) {

        if (url.startsWith(Constants.REDIRECT_URL)) {
            UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
            String code = sanitizer.getValue("code");

            Intent result = new Intent();
            result.putExtra(AUTH_CODE, code);
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
