/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ListActivity.java
 *
 * asd
 *
 * Author huanghaiqi, Created at 2016-10-09
 *
 * Ver 1.0, 2016-10-09, huanghaiqi, Create file.
 */

package com.tplink.forscrollshotdemo;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "Huanghaiqi";
    private WebView wv;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wv = (WebView) findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        et = (EditText) findViewById(R.id.et);
        wv.loadUrl("http://www.github.com");

    }

    public void surf(View view) {
        String url = et.getText().toString();
        wv.loadUrl(url);
        Log.i(TAG, "surf: " + url);
    }

    public void scroll(View view) {
        Rect rect = new Rect();
        wv.getGlobalVisibleRect(rect);
        Log.i(TAG, "halohoop rect: "+rect);

        int contentHeight = wv.getContentHeight();
        float contentHeightScaled = wv.getContentHeight()*wv.getScale();
        Log.i(TAG, "contentHeight: "+contentHeight);
        Log.i(TAG, "contentHeightScaled: "+contentHeightScaled);
        wv.scrollBy(0, (int) contentHeightScaled);
//        int scrollY = wv.getScrollY();
//        Log.i(TAG, "halohoop scroll: "+scrollY);
//        boolean b = wv.canScrollVertically(1);
//        Log.i(TAG, "halohoop scroll can scroll or not: "+b);

    }
}
