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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "Halohoop";
    private static String[] names = new String[100];

    static {
        for (int i = 0; i < names.length; i++) {
            names[i] = "halo" + i;
        }
    }

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
    }
}
