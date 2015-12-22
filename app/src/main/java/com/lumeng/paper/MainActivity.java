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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This Activity presents an ImageView that scales down when pressed and returns to full size when
 * released. This demonstrates a very simple integrates a very Simple integration of a Rebound
 * Spring model to drive a bouncy animation as the photo scales up and down. You can control the
 * Spring configuration by tapping on the blue nub at the bottom of the screen to reveal the
 * SpringConfiguratorView. From this view you can adjust the tension and friction of the animation
 * spring and observe the effect these values have on the animation.
 */
public class MainActivity extends Activity {

    private FrameLayout mRootView;

    private ReboundHorizontalScrollView scrollView;
    private HorizontalScrollViewAdapter adapter;
    private List<Integer> datas = new ArrayList<Integer>(Arrays.asList(
            R.mipmap.one, R.mipmap.two, R.mipmap.three,
            R.mipmap.four, R.mipmap.five, R.mipmap.six
    ));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mRootView = (FrameLayout) findViewById(R.id.root_view);

        scrollView = (ReboundHorizontalScrollView) findViewById(R.id.hrozontalscrollview);
        adapter = new HorizontalScrollViewAdapter(this, datas);

        scrollView.setCurrentImageChangeListener(new ReboundHorizontalScrollView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position, View viewIndicator) {

            }
        });

        scrollView.initDatas(adapter);
    }

}
