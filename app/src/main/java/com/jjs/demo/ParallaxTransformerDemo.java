package com.jjs.demo;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 说明：
 * Created by aa on 2017/7/6.
 */

public class ParallaxTransformerDemo implements ViewPager.PageTransformer {

    float parallaxCoefficient;
    float distanceCoefficient;

    public ParallaxTransformerDemo(float parallaxCoefficient, float distanceCoefficient) {
        this.parallaxCoefficient = parallaxCoefficient;
        this.distanceCoefficient = distanceCoefficient;
    }

    @Override
    public void transformPage(View page, float position) {
        //获取view宽度，并设置差值
        float scrollXOffset = page.getWidth() * parallaxCoefficient;

        // ...
        // layer is the id collection of views in this page
        if (page instanceof ViewGroup) {
            //循环遍历出view
            for (int i = 0; i < ((ViewGroup) page).getChildCount(); i++) {
                View view = ((ViewGroup) page).getChildAt(i);
                //给view设置X方向的偏移
                if (view != null) {
                    //进入为负偏移，出去为正偏移
                    view.setTranslationX(scrollXOffset * position);
                }
                scrollXOffset -=150;
            }
        }
    }
}