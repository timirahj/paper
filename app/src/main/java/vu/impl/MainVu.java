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

package vu.impl;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import presenter.impl.HorizontalScrollViewAdapter;
import com.lumeng.paper.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import widget.PaperViewPager;
import widget.ReboundHorizontalScrollView;

import vu.Vu;

/**
 * @author lumeng on 15/12/25.
 */
public class MainVu implements Vu {

    View view;
    @Bind(R.id.hrozontalscrollview)
    ReboundHorizontalScrollView scrollView;

    @Bind(R.id.viewPager)
    PaperViewPager viewPager;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
    }

    @Override
    public View getView() {
        return view;
    }

    public void setHorizontalAdapter(HorizontalScrollViewAdapter adapter) {
        scrollView.initDatas(adapter);
    }

    public void setViewPagerAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }

}
