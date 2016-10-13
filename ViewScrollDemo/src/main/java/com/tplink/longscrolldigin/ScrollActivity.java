package com.tplink.longscrolldigin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

public class ScrollActivity extends AppCompatActivity {

    private static final String TAG = "Halohoop";
    private ScrollView sv;
    private static String[] names = new String[100];

    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = "halo" + i;
        }
    }

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_main);
        sv = (ScrollView) findViewById(R.id.sv);
        iv = (ImageView) findViewById(R.id.iv);
        sv.postDelayed(new ScrollPost(),2000);
    }

    private int mScrollDeltaY = 0;

    class ScrollPost implements Runnable {

        @Override
        public void run() {
            if (!sv.canScrollVertically(1)) {
                Log.i(TAG, "run: " + mScrollDeltaY);
                return;
            }
            sv.scrollBy(0, 1);
            mScrollDeltaY++;
            Log.i(TAG, "record: "+mScrollDeltaY+" and get:" + sv.getScrollY());
            sv.postDelayed(this,10);
        }
    }
}
