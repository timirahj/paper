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
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

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

//    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
//    private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
    private FrameLayout mRootView;
//    private Spring mScaleSpring;

    private PaperHorizontalScrollView scrollView;
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

        scrollView = (PaperHorizontalScrollView) findViewById(R.id.hrozontalscrollview);
        adapter = new HorizontalScrollViewAdapter(this, datas);

        scrollView.setCurrentImageChangeListener(new PaperHorizontalScrollView.CurrentImageChangeListener() {
            @Override
            public void onCurrentImgChanged(int position, View viewIndicator) {

            }
        });

        scrollView.initDatas(adapter);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        // Add a listener to the spring when the Activity resumes.
//        mScaleSpring.addListener(mSpringListener);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        // Remove the listener to the spring when the Activity pauses.
//        mScaleSpring.removeListener(mSpringListener);
//    }
//
//    private class ExampleSpringListener extends SimpleSpringListener {
//        @Override
//        public void onSpringUpdate(Spring spring) {
//            // On each update of the spring value, we adjust the scale of the image view to match the
//            // springs new value. We use the SpringUtil linear interpolation function mapValueFromRangeToRange
//            // to translate the spring's 0 to 1 scale to a 100% to 50% scale range and apply that to the View
//            // with setScaleX/Y. Note that rendering is an implementation detail of the application and not
//            // Rebound itself. If you need Gingerbread compatibility consider using NineOldAndroids to update
//            // your view properties in a backwards compatible manner.
//            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
//            mImageView.setScaleX(mappedValue);
//            mImageView.setScaleY(mappedValue);
//        }
//    }

}
