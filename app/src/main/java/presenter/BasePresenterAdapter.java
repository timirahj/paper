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

package presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import vu.Vu;

/**
 * @author lumeng on 15/12/25.
 */
@SuppressWarnings("unchecked")
public abstract class BasePresenterAdapter<V extends Vu> extends BaseAdapter {
    protected V vu;

    public final View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            try {
                vu = (V) getVuClass().newInstance();
                vu.init(inflater, parent);
                convertView = vu.getView();
                convertView.setTag(vu);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            vu = (V) convertView.getTag();
        }

        if (convertView != null) {
            onBindItemVu(position);
        }

        return convertView;
    }

    protected abstract Class<V> getVuClass();

    protected abstract void onBindItemVu(int position);
}
