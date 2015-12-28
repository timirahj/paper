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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lumeng.paper.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import vu.Vu;

/**
 * @author lumeng on 15/12/28.
 */
public class PagerItemVu implements Vu {

    View view;

    @Bind(R.id.backbg)
    ImageView backbg;

    @Override
    public void init(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.item_viewpager, container, false);
        ButterKnife.bind(this, view);
    }

    @Override
    public View getView() {
        return view;
    }

    public void setResId(int resId) {
        backbg.setImageResource(resId);
    }
}
