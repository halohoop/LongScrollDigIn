/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * AfterSuperScrollView.java
 *
 * super
 *
 * Author huanghaiqi, Created at 2016-10-13
 *
 * Ver 1.0, 2016-10-13, huanghaiqi, Create file.
 */

package com.tplink.judgetheendofthescrollableview.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AfterSuperScrollView extends View implements GestureDetector.OnDoubleTapListener {
    private static final float MAX_VIEW_HEIGHT = 1280;
    private static final float MAX_VIEW_WIDTH = 720 - 160;
    private static final float MAX_902_WIDTH = 720;
    private static final float MAX_902_HEIGHT = 1280;
    private static final String TAG = "AfterSuperScrollView";
    private Bitmap mBitmap = null;
    private GestureDetector mGestureDetector;
    private boolean isAllowDebug = true;
    private int mMaxUpScrollDistance;
    private int mMaxDownScrollDistance;
    private float mMoveY;
    private float mDownY;
    private float mScrollDeltaY = 0;
    private float mShirnkScaleRatio = 1;
    private boolean mIsNeedShrink = false;
    private float mShrinkDrawXPosition = 0;

    public AfterSuperScrollView(Context context) {
        this(context, null);
    }

    public AfterSuperScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AfterSuperScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), new GestureListener());
        mGestureDetector.setOnDoubleTapListener(this);
    }

    public void setInitBitmaps(Bitmap bitmap) {
        mBitmap = bitmap;
        updateMaxScroll();
        updateShrinkRatio();
        invalidate();
    }

    private void updateShrinkRatio() {
        mShirnkScaleRatio = MAX_VIEW_HEIGHT / ((float) mBitmap.getHeight());
        mShrinkDrawXPosition = (MAX_VIEW_HEIGHT / 2) -
                ((mBitmap.getWidth() * mShirnkScaleRatio) / 2);
    }


    private void updateMaxScroll() {
        mMaxUpScrollDistance = (int) (-mBitmap.getHeight() + MAX_VIEW_HEIGHT);
        mMaxDownScrollDistance = 0;
    }

    public void setScrollDeltaY(float scrollDeltaY) {
        this.mScrollDeltaY = scrollDeltaY;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsNeedShrink) {
                    mMoveY = event.getY();
                    float deltaYMove = mMoveY - mDownY;
                    mScrollDeltaY += deltaYMove;
                    float abs = Math.abs(mScrollDeltaY - mMaxUpScrollDistance);
                    if (mScrollDeltaY > 0) {
                        mScrollDeltaY = 0;
                    }
                    if (mScrollDeltaY < mMaxUpScrollDistance) {
                        mScrollDeltaY = mMaxUpScrollDistance;
                    }
                    invalidate();
                    mDownY = mMoveY;
                }
                break;
            case MotionEvent.ACTION_UP:
                mMoveY = event.getY();
                float deltaYUp = mMoveY - mDownY;
                mScrollDeltaY += deltaYUp;
                invalidate();
                mDownY = mMoveY;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(MAX_VIEW_WIDTH / MAX_902_WIDTH, MAX_VIEW_WIDTH / MAX_902_WIDTH);
        if (mIsNeedShrink) {
            canvas.scale(mShirnkScaleRatio, mShirnkScaleRatio, getMeasuredWidth() >> 1, 0);
            canvas.drawBitmap(mBitmap, mShrinkDrawXPosition, 0, null);
        } else {
            canvas.translate(0, mScrollDeltaY);
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        canvas.restore();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (isAllowDebug) {
            Log.i("onSingleTapConfirmed", "Gesture onSingleTapConfirmed");
        }
        return true;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (isAllowDebug) {
            Log.i("onDoubleTap", "Gesture onDoubleTap");
        }
        if (mIsNeedShrink) {
            mIsNeedShrink = false;
        } else {
            mIsNeedShrink = true;
        }
        invalidate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        if (isAllowDebug) {
            Log.i("onDoubleTapEvent", "Gesture onDoubleTapEvent");
        }
        return true;
    }

    //OnGestureListener监听
    private class GestureListener implements GestureDetector.OnGestureListener {

        public boolean onDown(MotionEvent e) {
            if (isAllowDebug) {
                Log.i("onDown", "Gesture onDown");
            }
            return true;
        }

        public void onShowPress(MotionEvent e) {
            if (isAllowDebug) {
                Log.i("onShowPress", "Gesture onShowPress");
            }
        }

        public boolean onSingleTapUp(MotionEvent e) {
            if (isAllowDebug) {
                Log.i("onSingleTapUp", "Gesture onSingleTapUp");
            }
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (isAllowDebug) {
                Log.i("onScroll", "Gesture onScroll:" + (e2.getX() - e1.getX()) + "   " +
                        distanceX);
            }

            return true;
        }

        public void onLongPress(MotionEvent e) {
            if (isAllowDebug) {
                Log.i("onLongPress", "Gesture onLongPress");
            }
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (isAllowDebug) {
                Log.i("onFling", "Gesture onFling velocityY:" + velocityY);
            }
            return true;
        }
    }
}
