/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * SuperScrollView.java
 *
 * super
 *
 * Author huanghaiqi, Created at 2016-10-13
 *
 * Ver 1.0, 2016-10-13, huanghaiqi, Create file.
 */

package com.tplink.judgetheendofthescrollableview.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class SuperScrollView extends View {
    public SuperScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
