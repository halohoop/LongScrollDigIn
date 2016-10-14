/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * SuperScrollView2.java
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class SuperScrollView extends View implements GestureDetector.OnDoubleTapListener {
    private static final float MAX_VIEW_HEIGHT = 1280;
    private static final float MAX_VIEW_WIDTH = 720;
    private static final String TAG = "SuperScrollView";
    private Bitmap mBitmap = null;
    private GestureDetector mGestureDetector;
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
    private boolean mIsEnd = false;
    private float mShrinkDrawXPosition = 0;
    private float mAnimShrinkDrawXPosition = 0;
    private boolean mIsShrinkAnimationing = false;

    public SuperScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), new GestureListener());
        mGestureDetector.setOnDoubleTapListener(this);
    }

    public void setInitBitmaps(Bitmap bitmap, @Nullable Bitmap footerBitmap) {
        mBitmap = bitmap;
        if (footerBitmap != null) {
            mFooterBitmap = footerBitmap;
            mIsTheFooterIsNotNull = true;
        }
        updateMaxScroll();
        updateShrinkRatio();
        invalidate();
    }

    public void expandBitmap(Bitmap bitmap) {
        if (mBitmap == null) {
            throw new IllegalArgumentException("please call setInitBitmaps first");
        }
        if (bitmap == null) {
            mIsEnd = true;
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

    private void updateShrinkRatio() {
        mShirnkScaleRatio = MAX_VIEW_HEIGHT / ((float) mBitmap.getHeight());
        mAnimShirnkScaleRatio = mShirnkScaleRatio;
    }


    private void updateMaxScroll() {
        mMaxUpScrollDistance = (int) (-mBitmap.getHeight() - (mFooterBitmap != null ?
                mFooterBitmap.getHeight() : 0) + MAX_VIEW_HEIGHT);
        mMaxDownScrollDistance = 0;
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
        if (mIsNeedShrink) {
            if (!mIsShrinkAnimationing) {
                canvas.scale(mShirnkScaleRatio, mShirnkScaleRatio);
                canvas.drawBitmap(mBitmap, mShrinkDrawXPosition / mShirnkScaleRatio, 0, null);
                if (mIsTheFooterIsNotNull) {
                    canvas.drawBitmap(mFooterBitmap, mShrinkDrawXPosition, mBitmap.getHeight(),
                            null);
                }
            } else {
                canvas.scale(mAnimShirnkScaleRatio, mAnimShirnkScaleRatio);
                canvas.translate(0, mScrollDeltaY * mAnimShirnkScaleRatio);
                Log.i(TAG, "onDraw: " + (mAnimShrinkDrawXPosition / mAnimShirnkScaleRatio));
                canvas.drawBitmap(mBitmap, mAnimShrinkDrawXPosition / mAnimShirnkScaleRatio, 0, null);
                if (mIsTheFooterIsNotNull) {
                    canvas.drawBitmap(mFooterBitmap, mShrinkDrawXPosition, mBitmap.getHeight(),
                            null);
                }
            }
        } else {
            canvas.translate(0, mScrollDeltaY);
            canvas.drawBitmap(mBitmap, 0, 0, null);
            if (mIsTheFooterIsNotNull) {
                canvas.drawBitmap(mFooterBitmap, 0, mBitmap.getHeight(), null);
            }
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

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (isAllowDebug) {
            Log.i("onDoubleTap", "Gesture onDoubleTap");
        }
        if (mIsNeedShrink) {
            ValueAnimator shrinkAnim = ValueAnimator.ofFloat(mShirnkScaleRatio, 1);
            shrinkAnim.setDuration(250).setInterpolator(new LinearInterpolator());
            shrinkAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsShrinkAnimationing = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsShrinkAnimationing = false;
                    mIsNeedShrink = false;
                    mShrinkDrawXPosition = mAnimShrinkDrawXPosition;
                    invalidate();
                }
            });
            shrinkAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float ratio = (float) animation.getAnimatedValue();
                    mAnimShirnkScaleRatio = ratio;
                    mAnimShrinkDrawXPosition = MAX_VIEW_WIDTH / 2 - mBitmap.getWidth() *
                            mAnimShirnkScaleRatio / 2;
                    invalidate();
                }
            });
            shrinkAnim.start();
        } else {
            ValueAnimator shrinkAnim = ValueAnimator.ofFloat(1, mShirnkScaleRatio);
            shrinkAnim.setDuration(250).setInterpolator(new LinearInterpolator());
            shrinkAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsNeedShrink = true;//important
                    mIsShrinkAnimationing = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsShrinkAnimationing = false;
                    mIsNeedShrink = true;
                    mShrinkDrawXPosition = mAnimShrinkDrawXPosition;
                    invalidate();
                }
            });
            shrinkAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float ratio = (float) animation.getAnimatedValue();
                    mAnimShirnkScaleRatio = ratio;
                    mAnimShrinkDrawXPosition = MAX_VIEW_WIDTH / 2 - mBitmap.getWidth() *
                            mAnimShirnkScaleRatio / 2;
                    invalidate();
                }
            });
            shrinkAnim.start();
        }
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
