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

package com.lumeng.paper;

import android.util.SparseArray;
import android.view.View;

/**
 * This Class is Useless now.
 *
 * @author L.M
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class ViewHolder {
    private final SparseArray<View> views;
    private View convertView;

    public ViewHolder(View convertView) {
        this.views = new SparseArray<View>();
        this.convertView = convertView;
        convertView.setTag(this);
    }

    public static ViewHolder get(View convertView) {
        if (convertView == null) {
            return new ViewHolder(convertView);
        }
        ViewHolder existedHolder = (ViewHolder) convertView.getTag();
        return existedHolder;
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
