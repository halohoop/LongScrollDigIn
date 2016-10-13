/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * Utils.java
 *
 * 
 *
 * Author huanghaiqi, Created at 2016-10-13
 *
 * Ver 1.0, 2016-10-13, huanghaiqi, Create file.
 */

package com.tplink.judgetheendofthescrollableview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;

public class SuperScrollHelper {

    public final static int SCREEN_WIDTH_OF_902 = 720;
    public final static int SCREEN_HEIGHT_OF_902 = 1280;
    private static final String TAG = "Halohoop";

    private static int getNextScrollDeltaY(View scrollableView) {
        boolean b = scrollableView.canScrollVertically(1);
        Log.i(TAG, "canScrollVertically: "+b);
        if (scrollableView != null && b) {
            if (scrollableView instanceof ListView) {
                Rect scrollableViewRect = new Rect();
                ListView listView = ((ListView) scrollableView);
                listView.getGlobalVisibleRect(scrollableViewRect);

                View childAt = listView.getChildAt(listView.getChildCount() - 1);
                Rect childRect = new Rect();
                childAt.getGlobalVisibleRect(childRect);

                //last one is not scroll fully
                if (scrollableViewRect.top + childAt.getBottom() > scrollableViewRect.bottom) {
                    int height = scrollableViewRect.top + childAt.getBottom() - scrollableViewRect
                            .bottom;
                    return height;
                } else {
                    if (listView.canScrollVertically(1)) {
                        return childAt.getHeight();
                    } else {
                        return 0;
                    }
                }
            } else if (scrollableView instanceof GridView) {
                Rect scrollableViewRect = new Rect();
                GridView gridView = ((GridView) scrollableView);
                gridView.getGlobalVisibleRect(scrollableViewRect);
                View childAt = gridView.getChildAt(gridView.getChildCount() - 1);
                Rect childRect = new Rect();
                childAt.getGlobalVisibleRect(childRect);
                //last one is not scroll fully
                if (scrollableViewRect.top + childAt.getBottom() > scrollableViewRect.bottom) {
                    int height = scrollableViewRect.top + childAt.getBottom() - scrollableViewRect
                            .bottom;
                    return height;
                } else {
                    if (gridView.canScrollVertically(1)) {
                        return childAt.getHeight();
                    } else {
                        return 0;
                    }
                }
            } else if (scrollableView instanceof ScrollView) {
                ScrollView scrollView = ((ScrollView) scrollableView);
                int currentScrollY = scrollView.getScrollY();
                View childAt = scrollView.getChildAt(0);
                int paddingBottom = scrollView.getPaddingBottom();
                Log.i(TAG, "ScrollView paddingBottom: "+paddingBottom);
                Rect childAtRect = new Rect();
                childAt.getGlobalVisibleRect(childAtRect);
                int maxScrollAmount = childAt.getHeight() - childAtRect.height();
                int scrollAmountLeft = maxScrollAmount - currentScrollY;
                int scrollViewScrollDeltaY = SCREEN_HEIGHT_OF_902 / 10;
                if (scrollAmountLeft > scrollViewScrollDeltaY) {
                    return scrollViewScrollDeltaY;
                } else {
                    return scrollAmountLeft;
                }
            }
//            else if (scrollableView instanceof Others) {
//                //TODO: 16-10-13
//                return 0;
//            }
        } else {
            return 0;
        }
        return 0;
    }

    public static Bitmap getNextScrollDeltaYBitmap(View decorView, View scrollableView) {
        int nextScrollDeltaY = getNextScrollDeltaY(scrollableView);
        Rect scrollDeltaYRect = new Rect();
        if (scrollableView instanceof ListView) {
            ListView listView = (ListView) scrollableView;
            listView.scrollListBy(nextScrollDeltaY);
            if (listView.canScrollVertically(1)) {
                listView.scrollListBy(listView.getDividerHeight());
                nextScrollDeltaY += listView.getDividerHeight();
            }
            return getScrollBitmap(decorView, scrollableView,
                    nextScrollDeltaY, scrollDeltaYRect);
        } else if (scrollableView instanceof GridView) {
            GridView gridView = (GridView) scrollableView;
            gridView.scrollListBy(nextScrollDeltaY);
            if (gridView.canScrollVertically(1)) {
                int verticalSpacing = gridView.getVerticalSpacing();
                gridView.scrollListBy(verticalSpacing);
                nextScrollDeltaY += verticalSpacing;
            }
            return getScrollBitmap(decorView, scrollableView,
                    nextScrollDeltaY, scrollDeltaYRect);
        } else if (scrollableView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) scrollableView;
            scrollView.scrollBy(0, nextScrollDeltaY);
            return getScrollBitmap(decorView, scrollableView,
                    nextScrollDeltaY, scrollDeltaYRect);
        }
        return null;
    }

    @Nullable
    private static Bitmap getScrollBitmap(View decorView, View scrollableView, int
            nextScrollDeltaY, Rect scrollDeltaYRect) {
        if (scrollableView.getGlobalVisibleRect(scrollDeltaYRect)) {
            if (scrollableView instanceof ScrollView) {
                View childAt = ((ScrollView) scrollableView).getChildAt(0);
                Rect childRect = new Rect();
                childAt.getGlobalVisibleRect(childRect);
                scrollDeltaYRect.set(0, childRect.bottom - nextScrollDeltaY,
                        SCREEN_WIDTH_OF_902, childRect.bottom);
            }else{
                scrollDeltaYRect.set(0, scrollDeltaYRect.bottom - nextScrollDeltaY,
                        SCREEN_WIDTH_OF_902, scrollDeltaYRect.bottom);
            }
            if (scrollDeltaYRect.top < scrollDeltaYRect.bottom) {
                decorView.destroyDrawingCache();
                decorView.setDrawingCacheEnabled(true);
                boolean scrollbarFadingEnabled = scrollableView.isVerticalScrollBarEnabled();
                scrollableView.setVerticalScrollBarEnabled(false);
                decorView.buildDrawingCache();
                Bitmap bitmap = decorView.getDrawingCache();
                scrollableView.setVerticalScrollBarEnabled(scrollbarFadingEnabled);
                bitmap = cropBitmapByRect(bitmap, scrollDeltaYRect);
                decorView.destroyDrawingCache();
                return bitmap;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static Bitmap cropBitmapByRect(Bitmap bitmap, Rect scrollDeltaYRect) {
        if (bitmap.getWidth() != scrollDeltaYRect.width() ||
                bitmap.getHeight() != scrollDeltaYRect.height()) {
            scrollDeltaYRect.right = bitmap.getWidth();
        }
        if (scrollDeltaYRect.height() > bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap cropBitmap = null;
        cropBitmap = Bitmap.createBitmap(bitmap, 0, scrollDeltaYRect.top,
                scrollDeltaYRect.width(), scrollDeltaYRect.height());
        return cropBitmap;
    }

}
