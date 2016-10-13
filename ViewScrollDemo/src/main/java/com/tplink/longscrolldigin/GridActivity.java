package com.tplink.longscrolldigin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class GridActivity extends AppCompatActivity {

    private static final String TAG = "Halohoop";
    private GridView gv;
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
        setContentView(R.layout.grid_main);
        gv = (GridView) findViewById(R.id.gv);
        iv = (ImageView) findViewById(R.id.iv);

        gv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        gv.postDelayed(new Runnable() {
            @Override
            public void run() {
                gv.scrollListBy(6900);
            }
        }, 2000);
//        gv.postDelayed(new ScrollPost(),2000);
    }
    public int getScrollY() {
        View c = gv.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = gv.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }
    private int mScrollDeltaY = 0;

    class ScrollPost implements Runnable {

        @Override
        public void run() {
            if (!gv.canScrollVertically(1)) {
                Log.i(TAG, "run: " + mScrollDeltaY);
                return;
            }
            gv.scrollListBy(1);
            Log.i(TAG, "run: " + getScrollY());
            mScrollDeltaY++;
            gv.post(this);
        }
    }
}
