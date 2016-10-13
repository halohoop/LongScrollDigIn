package com.tplink.howbigthesize902canshow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Halohoop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        Bitmap bitmap = getShrinkBitmapSize();
        iv.setImageBitmap(bitmap);
    }

    public Bitmap getShrinkBitmapSize(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile
                ("/mnt/sdcard/Pictures/Screenshots/Screenshot_2016-01-04-10-26-16.png",options);
        Log.i(TAG, "getBitmapSize:options.outWidth: "+options.outWidth+" options.outHeight: "+options.outHeight);

        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile
                ("/mnt/sdcard/Pictures/Screenshots/Screenshot_2016-01-04-10-26-16.png",options);
        Log.i(TAG, "getBitmapSize:bitmap.width: "+bitmap.getWidth()+" bitmap.height: "+bitmap.getHeight());

        return bitmap;
    }
}
