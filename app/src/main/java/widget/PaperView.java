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

package widget;

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
import com.lumeng.paper.ScreenUtil;

/**
 * @author lumeng on 15/12/18.
 *         E-Mail: lumenghz@gmail.com
 */
public class PaperView extends FrameLayout implements View.OnTouchListener {

    private static final int SCROLL_MAX_DISTANCE = 400;

    private Status status = new Status();

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
    private View layer;
    private View viewPager;

    private GestureDetector gestureDetector;

    private float mLastMotionX;//滑动过程中，x方向的初始坐标
    private float mLastMotionY;//滑动过程中，y方向的初始坐标

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

        this.setLongClickable(true);
        this.setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewPager = getChildAt(0);
        layer = getChildAt(1);
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (status.tend == Status.STATUS_CHANGE_BIGGER) {
            popAnimation(true);
            status.preMode = Status.STATUS_BIGGER;
            status.currentMode = Status.STATUS_BIGGER;
        } else if (status.tend == Status.STATUS_CHANGE_SMALL) {
            popAnimation(false);
            status.preMode = Status.STATUS_NORMAL;
            status.currentMode = Status.STATUS_NORMAL;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO: 15/12/25 check touch area
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        int flag = 0;
        if (isViewUnder(layer, (int) event.getX(), (int) event.getY())) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                case MotionEvent.ACTION_MOVE:
                    final int deltaX = (int) (mLastMotionX - x);
                    final int deltaY = (int) (mLastMotionY - y);
                    boolean xMoved = Math.abs(deltaX) > Math.abs(deltaY);

                    if (xMoved) {
                        layer.onTouchEvent(event);
                        flag = 1;
                    } else {
                        this.onTouch(layer, event);
                        layer.onTouchEvent(event);
                        flag = 2;
                        return true;
                    }
                    break;
            }
        }

        if (isViewUnder(viewPager, (int) event.getX(), (int) event.getY())) {
            viewPager.onTouchEvent(event);
            flag = 3;
            return true;
        }

        if (flag == 1) {
            layer.onTouchEvent(event);
        } else if (flag == 3){
            viewPager.onTouchEvent(event);
        } else {
            this.onTouch(layer, event);
            layer.onTouchEvent(event);
        }

        Log.d("PaperView", "flag:" + flag);

        return false;
    }

    /**
     * Determine if the supplied view is under the given point in the parent view's
     * coordinate system
     * @param view Child view of the paren to hit test
     * @param x X position to test in the parent's coordinate system
     * @param y Y position to test in the parent's coordinate system
     * @return true if the supplied view is under the given point, false otherwise
     */
    private boolean isViewUnder(View view , int x, int y) {
        if (view == null) {
            return false;
        }
        return x >= view.getLeft() &&
                x < view.getRight() &&
                y >= view.getTop() &&
                y < view.getBottom();
    }

    private class gestursListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float beginY = e1.getY();
            float endY = e2.getY();

            float distance = endY - beginY;
            // distance > 0 means move down, translateDistance < 0 means move up

            if (distance < 0 && distance >= -(SCROLL_MAX_DISTANCE + 10)) {
                if (status.preMode == Status.STATUS_CHANGE_SMALL) {
                    status.tend = Status.STATUS_CHANGE_BIGGER;
                }
                if (status.preMode == Status.STATUS_CHANGE_BIGGER || status.preMode == Status.STATUS_NORMAL) {
                    // change bigger
                    float scaleValue = 1 + Math.abs(distance) / SCROLL_MAX_DISTANCE;
                    layer.setScaleX(scaleValue);
                    layer.setScaleY(scaleValue);

                    float move = (float) Math.abs(distance) / SCROLL_MAX_DISTANCE * (float) translateDistance;
                    layer.setTranslationY(-move);

                    popAnimation.setCurrentValue(scaleValue - 1);

                    status.preMode = Status.STATUS_CHANGE_BIGGER;
                    status.tend = Status.STATUS_CHANGE_BIGGER;
                }

            } else if (distance > 0 && distance < SCROLL_MAX_DISTANCE + 10) {
                if (status.preMode == Status.STATUS_CHANGE_BIGGER) {
                    status.tend = Status.STATUS_CHANGE_SMALL;
                }
                if (status.preMode == Status.STATUS_CHANGE_SMALL || status.preMode == Status.STATUS_BIGGER) {
                    // change smaller
                    float scaleValue = 1 - distance / SCROLL_MAX_DISTANCE;
                    layer.setScaleX(scaleValue);
                    layer.setScaleY(scaleValue);

                    // TODO: 15/12/21 figure out why it doesn't need translate when it getting smaller
                    popAnimation.setCurrentValue(scaleValue);

                    status.preMode = Status.STATUS_CHANGE_SMALL;
                    status.tend = Status.STATUS_CHANGE_SMALL;
                }
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

    /**
     * This method is used for deal with event dispatch
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return this.onTouchEvent(event);
        } else {
            return gestureDetector.onTouchEvent(event);
        }
    }

    /**
     * This class is used for mark, mark status of bottom view
     */
    private static class Status {
        /**
         * This is the distance of your finger.
         */
        private static final int STATUS_NORMAL = 1;
        private static final int STATUS_BIGGER = 2;
        private static final int STATUS_CHANGE_BIGGER = 3;
        private static final int STATUS_CHANGE_SMALL = 4;

        private int preMode = STATUS_NORMAL;
        private int currentMode = STATUS_NORMAL;
        private int tend = STATUS_CHANGE_BIGGER;
    }

}
