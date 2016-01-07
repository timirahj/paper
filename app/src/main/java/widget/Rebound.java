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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import presenter.impl.HorizontalScrollViewAdapter;

/**
 * @author lumeng on 16/1/5.
 */
public class Rebound extends HorizontalScrollView implements View.OnClickListener, PaperView.OnSizeChangeCallback {

    private LinearLayout container;

    private HorizontalScrollViewAdapter adapter;

    private int childWidth, childHeight;

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

        container = (LinearLayout) getChildAt(0);
        containerView = (ContainerView) getParent();
        paperView = (PaperView) containerView.getParent();

        childHeight = container.getChildAt(0).getMeasuredHeight();
        childWidth = container.getChildAt(0).getMeasuredWidth();

        paperView.setSizeChangeCallback(this);
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

    @Override
    public void onSizeChange(float scale) {
        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) container.getChildAt(i).getLayoutParams();
            params.width = (int) (childWidth * scale);
            params.height = (int) (childHeight * scale);
        }
        container.requestLayout();
    }
}
