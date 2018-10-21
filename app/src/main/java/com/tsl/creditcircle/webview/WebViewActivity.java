package com.tsl.creditcircle.webview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tsl.creditcircle.R;
import com.tsl.creditcircle.utils.Constants;


public class WebViewActivity extends AppCompatActivity {
    private boolean loadingFinished = true;
    private boolean redirect = false;
    private ProgressDialog mProgressDialog;

    public static Intent createIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, title);
        return intent;
    }

    public static Intent createIntent(Context context, @StringRes int url, @StringRes int title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, context.getString(url));
        intent.putExtra(Constants.TITLE, context.getString(title));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setUpWebView();
    }

    protected void setUpWebView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        WebView webView = (WebView) findViewById(R.id.webView1);

        Bundle extras = getIntent().getExtras();
        String url = extras.getString(Constants.URL);
        String title = extras.getString(Constants.TITLE);
        int color = extras.getInt(Constants.COLOR);
        int colorDark = extras.getInt(Constants.COLOR_DARK);
        toolbar.setTitle(title);
        if (color != 0){
            toolbar.setBackgroundColor(color);
            if (colorDark != 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(colorDark);
                }
            }
        }

        mProgressDialog = new ProgressDialog(WebViewActivity.this,
                R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.loading));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                view.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                if (WebViewActivity.this.onPageStarted(url)) {
                    mProgressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

                if(loadingFinished && !redirect){
                    //HIDE LOADING IT HAS FINISHED
                    mProgressDialog.dismiss();
                } else{
                    redirect = false;
                }

            }
        });
    }

    /**
     * Intercept urls in subclasses.
     * @param url the url which will start to load
     * @return true if the url is handled, false otherwise.
     */
    protected boolean onPageStarted(String url) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }


    @Override
    public void finish() {
        mProgressDialog.dismiss();
        super.finish();
    }
}
