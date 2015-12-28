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

package presenter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lumeng.paper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lumeng on 15/12/28.
 */
public class BasePagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    static final List<Integer> PAGES = new ArrayList<Integer>();

    // TODO: 15/12/28 add photo
    static {
        PAGES.add(R.mipmap.bg_one);
        PAGES.add(R.mipmap.bg_two);
        PAGES.add(R.mipmap.bg_three);
    }

    public BasePagerAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_viewpager, container, false);
        final int resId = PAGES.get(position);

        ImageView pager = (ImageView) view.findViewById(R.id.backbg);
        pager.setImageResource(resId);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return PAGES.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
