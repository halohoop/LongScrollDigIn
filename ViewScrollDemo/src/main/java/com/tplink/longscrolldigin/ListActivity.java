package com.tplink.longscrolldigin;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "Halohoop";
    private ListView lv;
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
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        iv = (ImageView) findViewById(R.id.iv);

        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
        lv.postDelayed(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                lv.getGlobalVisibleRect(rect);
                View childAt = lv.getChildAt(lv.getChildCount() - 1);
                Rect childRect = new Rect();
                childAt.getGlobalVisibleRect(childRect);
                int bottom = childAt.getBottom();
                Log.i(TAG, "run: getBottom:"+(bottom+rect.top)+" childRect bottom:"+childRect.bottom+" rect bottom:"+rect.bottom);
            }
        }, 2000);
//        lv.postDelayed(new ScrollPost(), 2000);
    }

    public int getScrollY() {
        View c = lv.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = lv.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    private int mScrollDeltaY = 0;

    class ScrollPost implements Runnable {

        @Override
        public void run() {
            if (!lv.canScrollVertically(1)) {
                Log.i(TAG, "run: " + mScrollDeltaY);
                return;
            }
            lv.scrollListBy(1);
            Log.i(TAG, "run: " + getScrollY());
            mScrollDeltaY++;
            lv.post(this);
        }
    }
}
