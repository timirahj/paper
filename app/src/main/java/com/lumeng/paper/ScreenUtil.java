/*
 * Copyright (C) 2015 LuMeng
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

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * @author lumeng on 15/12/18.
 */
public class ScreenUtil {
    private ScreenUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int[] getScreenWidth(Context context) {
        int[] a = new int[2];
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        a[0] = outMetrics.widthPixels;
        a[1] = outMetrics.heightPixels;
        return a;
    }

    /**
     * Determine if the supplied view is under the given point in the parent view's
     * coordinate system
     *
     * @param view Child view of the paren to hit test
     * @param x    X position to test in the parent's coordinate system
     * @param y    Y position to test in the parent's coordinate system
     * @return true if the supplied view is under the given point, false otherwise
     */
    public static boolean isViewUnder(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        return x >= view.getLeft() &&
                x < view.getRight() &&
                y >= view.getTop() &&
                y < view.getBottom();
    }
}
