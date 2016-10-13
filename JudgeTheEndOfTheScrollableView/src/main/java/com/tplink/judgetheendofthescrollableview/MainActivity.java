package com.tplink.judgetheendofthescrollableview;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ImageView iv;
    private static final String TAG = "Halohoop";
    private static String[] names = new String[100];

    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = "halo" + i;
        }
    }

    private GridView gv;
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        lv = (ListView) findViewById(R.id.lv);
//        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
//        iv = (ImageView) findViewById(R.id.iv);
//        lv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                View childAt0 = lv.getChildAt(0);
//                View childAt1 = lv.getChildAt(1);
//                int bottom0 = childAt0.getBottom();
//                int top1 = childAt1.getTop();
//                Log.i(TAG, "run: " + bottom0);
//                Log.i(TAG, "run: " + top1);
//                Log.i(TAG, "run: 2:" + lv.getDividerHeight());
//            }
//        }, 2000);
        //-------------------------------
//        setContentView(R.layout.activity_main_grid);
//        gv = (GridView) findViewById(R.id.gv);
//        gv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
//        iv = (ImageView) findViewById(R.id.iv);
        //-------------------------------
        setContentView(R.layout.activity_main_scroll);
        sv = (ScrollView) findViewById(R.id.sv);
        iv = (ImageView) findViewById(R.id.iv);
    }

    public void click(View view) {
        click2(sv);
    }

    private void click2(View scrollableView){
        Bitmap nextScrollDeltaYBitmap = SuperScrollHelper.getNextScrollDeltaYBitmap(
                getWindow().getDecorView(), scrollableView);
        iv.setImageBitmap(nextScrollDeltaYBitmap);
    }


}
