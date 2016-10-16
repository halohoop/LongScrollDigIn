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

public class SuperScrollView extends View {
    private static final float MAX_VIEW_HEIGHT = 1280;
    private static final float MAX_VIEW_WIDTH = 720 - 160;
    private static final float MAX_902_WIDTH = 720;
    private static final float MAX_902_HEIGHT = 1280;
    private static final String TAG = "SuperScrollView";
    private Bitmap mBitmap = null;
    private boolean isAllowDebug = true;
    private int mMaxUpScrollDistance;
    private int mMaxDownScrollDistance;
    private float mMoveY;
    private float mDownY;
    private float mScrollDeltaY = 0;
    private float mShirnkScaleRatio = 1;
    private float mAnimShirnkScaleRatio = 1;
    private boolean mIsTheFooterIsNotNull = false;
    private Bitmap mFooterBitmap;
    private boolean mIsNeedShrink = false;
    private boolean mIsNoMoreBitmap = false;
    private float mShrinkDrawXPosition = 0;
    private float mAnimShrinkDrawXPosition = 0;
    private boolean mIsShrinkAnimationing = false;

    public SuperScrollView(Context context) {
        this(context, null);
    }

    public SuperScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    public void setInitBitmaps(Bitmap bitmap, Bitmap footerBitmap) {
        mBitmap = bitmap;
        if (footerBitmap != null) {
            mFooterBitmap = footerBitmap;
            mIsTheFooterIsNotNull = true;
        }
        updateMaxScroll();
        updateShrinkRatio();
        invalidate();
    }

    private void expandBitmap() {
        if (mBitmap == null) {
            throw new IllegalArgumentException("please call setInitBitmaps first");
        }
        Bitmap bitmap = mBitmapDataHelper.getMore();
        if (bitmap == null) {
            mIsNoMoreBitmap = true;
            return;
        }
        Bitmap newMergeBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
                mBitmap.getHeight() + bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newMergeBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawBitmap(bitmap, 0, mBitmap.getHeight(), null);
        mBitmap.recycle();
        mBitmap = null;
        mBitmap = newMergeBitmap;
        updateMaxScroll();
        updateShrinkRatio();
    }

    public void endScroll(){
        Bitmap finalBitmap = mergeFinalBitmap();
        mBitmapDataHelper.onScrollShotEnd(finalBitmap);
    }

    private Bitmap mergeFinalBitmap() {
        int finalHeight = mBitmap.getHeight() +
                (mFooterBitmap != null ? mFooterBitmap.getHeight() : 0);
        Bitmap finalBitmap = Bitmap.createBitmap(mBitmap.getWidth(), finalHeight, Bitmap.Config
                .ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        if (mFooterBitmap != null) {
            canvas.drawBitmap(mFooterBitmap, 0, mBitmap.getHeight(), null);
        }
        recycleBitmap(mBitmap);
        recycleBitmap(mFooterBitmap);
        mBitmap = null;
        mFooterBitmap = null;
        return finalBitmap;
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }


    private void updateShrinkRatio() {
        mShirnkScaleRatio = MAX_VIEW_HEIGHT / ((float) mBitmap.getHeight() +
                (mFooterBitmap != null ?
                        mFooterBitmap.getHeight() : 0));
        mAnimShirnkScaleRatio = mShirnkScaleRatio;
    }

    public float getScrollDeltaY() {
        return mScrollDeltaY;
    }

    private void updateMaxScroll() {
        mMaxUpScrollDistance = (int) (-mBitmap.getHeight() - (mFooterBitmap != null ?
                mFooterBitmap.getHeight() : 0) + MAX_VIEW_HEIGHT);
        mMaxDownScrollDistance = 0;
    }

    public interface BitmapDataHelper {
        Bitmap getMore();

        void onScrollShotEnd(Bitmap finalBitmap);
    }

    private BitmapDataHelper mBitmapDataHelper;

    public void setBitmapDataHelper(BitmapDataHelper bitmapDataHelper) {
        this.mBitmapDataHelper = bitmapDataHelper;
    }

    private int mGetMoreCount = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
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
                    if (!mIsNoMoreBitmap && abs <= MAX_VIEW_HEIGHT && mGetMoreCount < 5) {
                        mGetMoreCount++;
                        expandBitmap();
                    }
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
                mGetMoreCount = 0;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(MAX_VIEW_WIDTH / MAX_902_WIDTH, MAX_VIEW_WIDTH / MAX_902_WIDTH);
        if (mIsNeedShrink) {
            if (!mIsShrinkAnimationing) {
                canvas.scale(mShirnkScaleRatio, mShirnkScaleRatio);
                canvas.drawBitmap(mBitmap, mShrinkDrawXPosition / mShirnkScaleRatio, 0, null);
                if (mIsTheFooterIsNotNull) {
                    canvas.drawBitmap(mFooterBitmap,
                            mShrinkDrawXPosition / mShirnkScaleRatio,
                            MAX_VIEW_HEIGHT - mFooterBitmap.getHeight() - mScrollDeltaY,
                            null);
                }
            } else {
                canvas.scale(mAnimShirnkScaleRatio, mAnimShirnkScaleRatio);
                canvas.translate(0, mScrollDeltaY * mAnimShirnkScaleRatio);
                Log.i(TAG, "onDraw: " + (mAnimShrinkDrawXPosition / mAnimShirnkScaleRatio));
                canvas.drawBitmap(mBitmap, mAnimShrinkDrawXPosition / mAnimShirnkScaleRatio, 0,
                        null);
                if (mIsTheFooterIsNotNull) {
                    canvas.drawBitmap(mFooterBitmap,
                            mAnimShrinkDrawXPosition / mAnimShirnkScaleRatio,
                            (MAX_VIEW_HEIGHT - mFooterBitmap.getHeight() - mScrollDeltaY)
                                    / mAnimShirnkScaleRatio,
                            null);
                }
            }
        } else {
            canvas.translate(0, mScrollDeltaY);
            canvas.drawBitmap(mBitmap, 0, 0, null);
            if (mIsTheFooterIsNotNull) {
                canvas.drawBitmap(mFooterBitmap, 0,
                        MAX_VIEW_HEIGHT - mFooterBitmap.getHeight() - mScrollDeltaY,
                        null);
            }
        }
        canvas.restore();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

}
