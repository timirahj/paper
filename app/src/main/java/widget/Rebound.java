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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.lumeng.paper.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import presenter.impl.HorizontalScrollViewAdapter;

/**
 * @author lumeng on 16/1/5.
 */
public class Rebound extends HorizontalScrollView implements View.OnClickListener, PaperView.OnSizeChangeCallback {

    private LinearLayout container;

    private HorizontalScrollViewAdapter adapter;

    private int childWidth, childHeight, width, height;
    private int parentHeight;
    private int currentPage = 0;
    private int childCount;
    private int oldWidth;

    private List<Integer> viewList = new ArrayList<Integer>();

    private ContainerView containerView;

    private PaperView paperView;

    private CurrentImageChangeListener listener;

    private OnItemClickListener onClickListener;

    public Rebound(Context context, AttributeSet attrs) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        getChildInfo();

        childHeight = container.getChildAt(0).getMeasuredHeight();
        childWidth = container.getChildAt(0).getMeasuredWidth();

        parentHeight = getMeasuredHeight();

        if (width == 0 && height == 0) {
            width = oldWidth = childWidth;
            height = childHeight;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = ev.getX();
                getFingerPoi(downX);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * which child of finger at
     */
    private void getFingerPoi(float downX) {
        for (int i = 0; i < childCount; i++) {
            if ((viewList.get(i) - getScrollX()) >= downX && (viewList.get(i) - getScrollX()) <= (downX + childWidth)) {
                currentPage = i;
                break;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        containerView = (ContainerView) getParent();
        paperView = (PaperView) containerView.getParent();
        paperView.setSizeChangeCallback(this);
    }

    /**
     * Measure distance of every child's left to border
     */
    private void getChildInfo() {
        if (container != null) {
            for (int i = 0; i < container.getChildCount(); i++) {
                if (((View) container.getChildAt(i)).getWidth() > 0) {
                    viewList.add(((View) container.getChildAt(i)).getRight());
                }
            }
        }
    }

    public void initDatas(HorizontalScrollViewAdapter adapter) {
        this.adapter = adapter;
        container = (LinearLayout) getChildAt(0);

        final View view = adapter.getView(0, null, container);
        container.addView(view);

        initChild();
    }

    private void initChild() {
        container = (LinearLayout) getChildAt(0);

        container.removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            View view = adapter.getView(i, null, container);
            view.setOnClickListener(this);
            container.addView(view);
        }

        childCount = container.getChildCount();
    }

    /**
     * scroll to current page
     */
    private void smoothScrollToCurrent() {
        smoothScrollTo(viewList.get(currentPage) - 10, 0);
    }

    /**
     * scroll to next page
     */
    private void smoothScrollToNextPage() {
        if (currentPage < childCount - 1) {
            currentPage++;
            smoothScrollTo(viewList.get(currentPage) - 10, 0);
        }
    }

    private void smoothScrollToPrePage() {
        if (currentPage > 0) {
            currentPage--;
            smoothScrollTo(viewList.get(currentPage) - 10, 0);
        }
    }

    @Override
    public void onClick(View v) {
    }

    public interface CurrentImageChangeListener {
        void onCurrentImgChanged(int position, View viewIndicator);
    }

    public interface OnItemClickListener {
        void onClick(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setCurrentImageChangeListener(CurrentImageChangeListener listener) {
        this.listener = listener;
    }

    /**
     * I add this method because cellphone will keep drawing view when user do scale operation. So the width of every
     * cell is keep changing. But what i want is i can keep the cell which user's finger in is always on screen. So i
     * must do scroll operation to keep that cell on screen. Otherwise the cell which user's finger in will be
     * supplanted out of screen. I think it is very unfriendly to user.
     *
     * And you can try to set {@link #onSizeChange(float, boolean)}'s body null, and see what will happen.
     *
     * If you have any hesitation about this method, Please send E-Mail to <b>jiahehz@gmail.com</b> or create new issue.
     * I will keep in touch with you if you have any good idea or issue.
     */
    @Override
    public void onSizeChange(float scale, boolean isBigger) {
        int newWidth = 0;
        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) container.getChildAt(i).getLayoutParams();
            params.width = (int) (width * scale);
            if (i == 0) {
                newWidth = params.width;
            }
        }
        container.requestLayout();

        int scrollDistance = newWidth - oldWidth;

        if (isBigger) {
            smoothScrollBy(scrollDistance * (currentPage + 1), parentHeight);
        } else {
            smoothScrollBy(-scrollDistance * (currentPage + 1), parentHeight);
        }

        oldWidth = newWidth;
    }
}