/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * MainActivity2.java
 *
 * 
 *
 * Author huanghaiqi, Created at 2016-10-13
 *
 * Ver 1.0, 2016-10-13, huanghaiqi, Create file.
 */

package com.tplink.judgetheendofthescrollableview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tplink.judgetheendofthescrollableview.widgets.AfterSuperScrollView;
import com.tplink.judgetheendofthescrollableview.widgets.SuperScrollView;

public class MainActivity2 extends AppCompatActivity implements SuperScrollView.BitmapDataHelper {
    private static final String TAG = "Halohoop";
    private static String[] names = new String[100];
    private ListView lv;

    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = "halo" + i;
        }
    }

    private AfterSuperScrollView aSuperScrollView;
    private SuperScrollView superScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);
        }
    }


    public void click(View view) {
        SuperScrollHelper.mAllBitmapHeight = 0;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager
                .LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
//        SuperScrollView superScrollView = new SuperScrollView(this);
        View inflate = View.inflate(this, R.layout.super_layout, null);
        superScrollView = (SuperScrollView) inflate.findViewById(R.id.ssv);
        aSuperScrollView = (AfterSuperScrollView) inflate.findViewById(R.id.assv);
        superScrollView.setBackgroundColor(Color.BLACK);

//        Bitmap body = BitmapFactory.decodeFile
//                ("/mnt/sdcard/Pictures/Screenshots/Screenshot_2016-01-05-13-21-17_body.png");
//        Bitmap footer = BitmapFactory.decodeFile
//                ("/mnt/sdcard/Pictures/Screenshots/Screenshot_2016-01-05-13-21-17_footer.png");
        Bitmap body = getBody();
        Bitmap footer = getFooter();
        superScrollView.setInitBitmaps(body, footer);
        superScrollView.setBitmapDataHelper(this);
//        wm.addView(superScrollView, layoutParams);
        wm.addView(inflate, layoutParams);
    }

    @Override
    public Bitmap getMore() {
        Bitmap nextScrollDeltaYBitmap = SuperScrollHelper.getNextScrollDeltaYBitmap(
                getWindow().getDecorView(), lv);
        return nextScrollDeltaYBitmap;
    }

    @Override
    public void onScrollShotEnd(Bitmap finalBitmap) {
        aSuperScrollView.setInitBitmaps(finalBitmap);
        aSuperScrollView.setScrollDeltaY(superScrollView.getScrollDeltaY());
        superScrollView.setVisibility(View.GONE);
        aSuperScrollView.setVisibility(View.VISIBLE);
    }

    public Bitmap getBody() {
        View decorView = getWindow().getDecorView();

        Rect bodyRect = new Rect();
        Rect footerRect = new Rect();
        Rect scrollViewRect = new Rect();
        decorView.getGlobalVisibleRect(bodyRect);
        lv.getGlobalVisibleRect(scrollViewRect);
        footerRect.set(bodyRect);
        footerRect.top = scrollViewRect.bottom;
        bodyRect.bottom = footerRect.top;

        decorView.destroyDrawingCache();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        Bitmap body = Bitmap.createBitmap(drawingCache, 0, 0, bodyRect.width(), bodyRect.height());
        decorView.destroyDrawingCache();
        return body;
    }

    public Bitmap getFooter() {
        View decorView = getWindow().getDecorView();

        Rect bodyRect = new Rect();
        Rect footerRect = new Rect();
        Rect scrollViewRect = new Rect();
        decorView.getGlobalVisibleRect(bodyRect);
        lv.getGlobalVisibleRect(scrollViewRect);
        footerRect.set(bodyRect);
        footerRect.top = scrollViewRect.bottom;
        bodyRect.bottom = footerRect.top;

        decorView.destroyDrawingCache();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        Bitmap footer = Bitmap.createBitmap(drawingCache, 0, footerRect.top,
                footerRect.width(), footerRect.height());
        decorView.destroyDrawingCache();
        return footer;
    }
}
