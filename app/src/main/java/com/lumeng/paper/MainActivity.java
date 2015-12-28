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

import presenter.impl.HorizontalScrollViewAdapter;
import presenter.impl.ViewPagerAdapter;
import presenter.BasePresenterActivity;
import vu.impl.MainVu;

public class MainActivity extends BasePresenterActivity<MainVu> {

    @Override
    protected Class<MainVu> getVuClass() {
        return MainVu.class;
    }

    @Override
    protected void onBindVu() {
        HorizontalScrollViewAdapter adapter = new HorizontalScrollViewAdapter();
        vu.setHorizontalAdapter(adapter);

        vu.setViewPagerAdapter(new ViewPagerAdapter());
    }
}
