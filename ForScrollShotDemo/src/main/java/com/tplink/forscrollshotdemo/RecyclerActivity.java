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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new MyAdapter());
//        rv.postDelayed(new ScrollRunnable(), 2000);
        rv.postDelayed(new Runnable(){
            @Override
            public void run() {
                boolean b = rv.canScrollVertically(1);
                Log.i("TAG", "run: "+b);
            }
        }, 2000);
        String name = rv.getClass().getName();
        String simpleName = rv.getClass().getSimpleName();
//        Log.i("TAG", "onCreate: name:" + name);
//        Log.i("TAG", "onCreate: simpleName:" + simpleName);
    }

    class ScrollRunnable implements Runnable {

        @Override
        public void run() {
            rv.scrollBy(0, 100);
            rv.postDelayed(this, 2000);
        }
    }


    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = View.inflate(RecyclerActivity.this, android.R.layout
                    .simple_list_item_1, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            String data = Cheeses.CHEESES[i];
            viewHolder.text1.setText(data);
        }

        @Override
        public int getItemCount() {
            return Cheeses.CHEESES.length;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

}
