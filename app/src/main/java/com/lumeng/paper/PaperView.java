/*
 * Copyright (C) 2015 LuMeng
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
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

/**
 * @author lumeng on 15/12/18.
 *         E-Mail: lumenghz@gmail.com
 */
public class PaperView extends FrameLayout implements View.OnTouchListener {

    /**
     * This is the distance of your finger.
     */
    private static final int SCROLL_MAX_DISTANCE = 300;
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
     * <b>Useless now</b>
     * Max move distance of the bottomView when it Scale
     */
    private static float maxMoveDistance;

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
    private View layer;

    private GestureDetector gestureDetector;

    public PaperView(Context context) {
        this(context, null);
    }

    public PaperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        springSystem = SpringSystem.create();
        gestureDetector = new GestureDetector(context, new gestursListener());

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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layer = getChildAt(0);
        layer.setOnTouchListener(this);
        layer.setLongClickable(true);
        int contentHeight = layer.getHeight();
        SCALE = (float) screen[1] / (float) contentHeight;
        translateDistance = (double) (screen[1] - layer.getHeight()) / 2;
    }

    private void popAnimation(boolean on) {
        popAnimation.setEndValue(on ? 1 : 0);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setPopAnimationProgress(float progress) {
        float bottomScale = transition(progress, 1f, SCALE);
        layer.setScaleX(bottomScale);
        layer.setScaleY(bottomScale);

        float translate = (float) SpringUtil.mapValueFromRangeToRange(popAnimation.getCurrentValue(), 0, 1, 0, PaperView.translateDistance);
        layer.setTranslationY(-translate);
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

    /**
     * Get the screen height and width
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        screen = ScreenUtil.getScreenWidth(getContext());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * This method is used for deal width event dispatch
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            return this.onTouchEvent(event);
        } else {
            return gestureDetector.onTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (BOTTOM_STATUS == STATUS_CHANGE_BIGGER) {
            popAnimation(true);
            BOTTOM_STATUS = STATUS_BIGGER;
        } else if (BOTTOM_STATUS == STATUS_CHANGE_SMALL) {
            popAnimation(false);
            BOTTOM_STATUS = STATUS_NORMAL;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private class gestursListener implements GestureDetector.OnGestureListener {

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

            if (distance < 0 && distance >= -SCROLL_MAX_DISTANCE) {
                // change bigger
                float scaleValue = 1 + Math.abs(distance) / SCROLL_MAX_DISTANCE;
                layer.setScaleX(scaleValue);
                layer.setScaleY(scaleValue);

                float move = (float) Math.abs(distance) / SCROLL_MAX_DISTANCE * (float) translateDistance;
                layer.setTranslationY(-move);
                popAnimation.setCurrentValue(scaleValue - 1);

                BOTTOM_STATUS = STATUS_CHANGE_BIGGER;
            } else if (distance > 0 && distance < SCROLL_MAX_DISTANCE) {
                // change smaller
                float scaleValue = 1 - distance / SCROLL_MAX_DISTANCE;
                layer.setScaleX(scaleValue);
                layer.setScaleY(scaleValue);

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
}
