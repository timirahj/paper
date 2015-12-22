/*
 * Copyright 2015 LuMeng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lumeng.paper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个自定义view是从网上直接down下来的. 下面是原文地址
 * http://blog.csdn.net/lmj623565791/article/details/38140505
 */
public abstract class ReboundHorizontalScrollView extends HorizontalScrollView {

    /**
     * This is the distance of your finger.
     */
    private static final int SCROLL_MAX_DISTANCE = 400;
    private static final int STATUS_NORMAL = 1;
    private static final int STATUS_BIGGER = 2;
    private static final int STATUS_CHANGE_BIGGER = 3;
    private static final int STATUS_CHANGE_SMALL = 4;

    /**
     * Load status of bottom. It used when user's finger leave screen of cellphone
     * {@link #STATUS_NORMAL} present the original status
     * {@link #STATUS_BIGGER} present the FULLSCREEN status
     */
    private int BOTTOM_STATUS = STATUS_NORMAL;

    /**
     * Save Screen parameters int Array
     */
    private int[] screen;

    /**
     * Value of magnification(放大率).
     */
    private float SCALE;

    /**
     * Distance that bottom view should translateDistance when it comes to Scale Animations
     */
    private static double translateDistance;

    /**
     * Spring operation parameters
     */
    private final SpringSystem springSystem;
    private final Spring popAnimation;

    /**
     * HorizontalListView中的LinearLayout
     */
    public LinearLayout mContainer;

    /**
     * data adapter
     */
    public HorizontalScrollViewAdapter mAdapter;

    private GestureDetector gestureDetector;

    public ReboundHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        springSystem = SpringSystem.create();
        gestureDetector = new GestureDetector(context, new gestureListener());

        popAnimation = springSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(5, 10))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        setPopAnimationProgress((float) spring.getCurrentValue());
                    }
                });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContainer = (LinearLayout) getChildAt(0);
        screen = ScreenUtil.getScreenWidth(getContext());
        int contentHeight = ReboundHorizontalScrollView.this.getHeight();
        SCALE = (float) screen[1] / (float) contentHeight;
        translateDistance = (double) (screen[1] - mContainer.getHeight()) / 2;
    }

    private void popAnimation(boolean on) {
        popAnimation.setEndValue(on ? 1 : 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//			Log.e(TAG, getScrollX() + "");
//
//                int scrollX = getScrollX();
//                // 如果当前scrollX为view的宽度，加载下一张，移除第一张
//                if (scrollX >= mChildWidth) {
//                    loadNextImg();
//                }
//                // 如果当前scrollX = 0， 往前设置一张，移除最后一张
//                if (scrollX == 0) {
//                    loadPreImg();
//                }
//                break;
//        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (BOTTOM_STATUS == STATUS_CHANGE_BIGGER) {
                popAnimation(true);
                BOTTOM_STATUS = STATUS_BIGGER;
            } else if (BOTTOM_STATUS == STATUS_CHANGE_SMALL) {
                popAnimation(false);
                BOTTOM_STATUS = STATUS_NORMAL;
            }
            return super.onTouchEvent(event);
        }
//        return super.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    private class gestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            // 用户轻触触摸屏, 由1个MotionEvent ACTION_DOWN触发
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
            // 注意和onDown()的区别，强调的是没有松开或者拖动的状态
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float beginY = e1.getY();
            float endY = e2.getY();

            float distance = endY - beginY;
            // translateDistance > 0 means move down, translateDistance < 0 means move up

            // TODO: 15/12/21 solve the problem when scroll down after scroll up without finger leave

            if (distance < 0 && distance >= -(SCROLL_MAX_DISTANCE * (SCALE - 1))) {
                // change bigger
                float scaleValue = 1 + Math.abs(distance) / SCROLL_MAX_DISTANCE;
                ReboundHorizontalScrollView.this.setScaleX(scaleValue);
                ReboundHorizontalScrollView.this.setScaleY(scaleValue);

                float move = (float) Math.abs(distance) / SCROLL_MAX_DISTANCE * (float) translateDistance;
                ReboundHorizontalScrollView.this.setTranslationY(-move);
                popAnimation.setCurrentValue(scaleValue - 1);

                BOTTOM_STATUS = STATUS_CHANGE_BIGGER;
            } else if (distance > 0 && distance < SCROLL_MAX_DISTANCE) {
                // change smaller
                float scaleValue = 1 - distance / SCROLL_MAX_DISTANCE;
                ReboundHorizontalScrollView.this.setScaleX(scaleValue);
                ReboundHorizontalScrollView.this.setScaleY(scaleValue);

                // TODO: 15/12/21 figure out why it doesn't need translate when it getting smaller
                popAnimation.setCurrentValue(scaleValue);
                BOTTOM_STATUS = STATUS_CHANGE_SMALL;
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setPopAnimationProgress(float progress) {
        float bottomScale = transition(progress, 1f, SCALE);
        ReboundHorizontalScrollView.this.setScaleX(bottomScale);
        ReboundHorizontalScrollView.this.setScaleY(bottomScale);

        float translate = (float) SpringUtil.mapValueFromRangeToRange(popAnimation.getCurrentValue(), 0, 1, 0, translateDistance);
        ReboundHorizontalScrollView.this.setTranslationY(-translate);
    }

    /**
     * Facebook rebound and origami provide this method
     *
     * @param progress progress in range
     * @return SCALE value
     */
    private float transition(float progress, float startValue, float endValue) {
        return (float) SpringUtil.mapValueFromRangeToRange(progress, 0, 1, startValue, endValue);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() != MotionEvent.ACTION_UP) {
            this.gestureDetector.onTouchEvent(ev);
        } else {
            return this.onTouchEvent(ev);
        }
        return true;
    }

    /**
     * 加载下一张图片
     */
    protected abstract void loadNextImg();

    /**
     * 加载前一张图片
     */
    protected abstract void loadPreImg();

    /**
     * 初始化数据，设置数据适配器
     *
     * @param mAdapter
     */
    public abstract void initDatas(HorizontalScrollViewAdapter mAdapter);

    /**
     * 加载第一屏的View
     *
     * @param mCountOneScreen
     */
    public abstract void initFirstScreenChildren(int mCountOneScreen);
}