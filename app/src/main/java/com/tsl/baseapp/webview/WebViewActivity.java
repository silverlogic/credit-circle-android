package com.tsl.baseapp.webview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tsl.baseapp.R;
import com.tsl.baseapp.utils.Constants;


public class WebViewActivity extends AppCompatActivity {
    private boolean loadingFinished = true;
    private boolean redirect = false;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setUpWebView();
    }

    private void setUpWebView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        WebView webView = (WebView) findViewById(R.id.webView1);

        Intent intent = getIntent();
        String url = intent.getExtras().getString(Constants.URL);
        String title = intent.getExtras().getString(Constants.TITLE);
        toolbar.setTitle(title);

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
                mProgressDialog.show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
