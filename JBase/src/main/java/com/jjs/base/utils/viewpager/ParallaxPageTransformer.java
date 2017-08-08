package com.jjs.base.utils.viewpager;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * 说明：单个item的背景平移。对应id的背景将会缓慢平移
 * Created by aa on 2017/7/26.
 */

public class ParallaxPageTransformer implements ViewPager.PageTransformer {
    int bg_id;

    public ParallaxPageTransformer(@IdRes int bg_id) {
        this.bg_id = bg_id;
    }

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(1);

        } else if (position <= 1) { // [-1,1]
            View bgView = view.findViewById(bg_id);
            if (bgView != null)
                bgView.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed
            else
                Log.e("ParallaxTransformer", "id is not found");

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
        }


    }
}
