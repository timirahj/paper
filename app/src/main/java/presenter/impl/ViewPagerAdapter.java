/*
 * Copyright 2015-2016 LuMeng
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

package presenter.impl;

import com.lumeng.paper.R;

import java.util.ArrayList;
import java.util.List;

import presenter.BaseViewPagerAdapter;
import vu.impl.PagerItemVu;

/**
 * @author lumeng on 15/12/28.
 */
public class ViewPagerAdapter extends BaseViewPagerAdapter<PagerItemVu> {

    static final List<Integer> PAGES = new ArrayList<Integer>();

    static {
        PAGES.add(R.mipmap.bg_one);
        PAGES.add(R.mipmap.bg_two);
        PAGES.add(R.mipmap.bg_three);
    }

    @Override
    protected Class<PagerItemVu> getVuClass() {
        return PagerItemVu.class;
    }

    @Override
    protected void onBindItemVu(int position) {
        vu.setResId(PAGES.get(position));
    }

    @Override
    public int getCount() {
        return PAGES.size();
    }
}
