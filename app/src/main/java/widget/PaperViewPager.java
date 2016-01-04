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

package widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author lumeng on 15/12/29.
 */
public class PaperViewPager extends ViewPager {

    private boolean left = false;
    private boolean right = false;
    private boolean isScrolling = false;
    private int lastValue = -1;
    private ViewChangeCallback callback = null;

    public PaperViewPager(Context context) {
        super(context);
        init();
    }

    public PaperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnPageChangeListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            PaperView.canDeal = false;
            PaperView.isHandle = false;
        }
        if (isViewUnder(this, (int) ev.getX(), (int) ev.getY())) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * Judge if the click position under viewpager or not
     * @return true if under
     *         false otherwise
     */
    private boolean isViewUnder(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        return x >= view.getLeft() &&
                x < view.getRight() &&
                y >= view.getTop() &&
                y < view.getBottom();
    }

    private OnPageChangeListener listener = new OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (isScrolling) {
                if (lastValue > positionOffsetPixels) {
                    right = true;
                    left = false;
                } else if (lastValue < positionOffsetPixels) {
                    right = false;
                    left = true;
                } else if (lastValue == positionOffsetPixels) {
                    right = left = false;
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (callback != null) {
                callback.getCurrentPageIndex(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 1) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }

            if (state == 2) {
                if (callback != null) {
                    callback.changeView(left, right);
                }
                right = left = false;
            }
        }
    };

    public void setChangeViewCallback(ViewChangeCallback callback) {
        this.callback = callback;
    }

    public interface ViewChangeCallback {
        public void changeView(boolean left, boolean right);
        public void getCurrentPageIndex(int index);
    }
}
