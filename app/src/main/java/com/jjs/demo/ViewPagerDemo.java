package com.jjs.demo;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * 作者： Jacky
 * 日期：2017-04-18
 * 轮播引导页页面
 */

public class ViewPagerDemo extends AppCompatActivity {


    // 引导页图片资源
    ArrayList<View> views;

    ViewPager viewPager;//滑动轮播
    LinearLayout ll_dot;//小圆点显示控件
    ImageView iv_bg;//整体背景图片
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置画布可以延伸到屏幕外.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
       // setContentView(R.layout.layout_guide);
      //  iv_bg = (ImageView) findViewById(R.id.iv_guide_bg);
      // viewPager = (ViewPager) findViewById(R.id.pager_guide);
       // ll_dot = (LinearLayout) findViewById(R.id.ll_guide_dots);
        //获得视差动画控件

        views = new ArrayList<>();
        //views.add(View.inflate(this, R.layout.guide_1, null));
        //views.add(View.inflate(this, R.layout.guide_2, null));
       // views.add(View.inflate(this, R.layout.guide_3, null));
        //views.add(View.inflate(this, R.layout.guide_4, null));

       /* new PagerUtils().setView(viewPager, ll_dot, iv_bg, views).setMoveBG(40).setAutoPlay(true, true, 2000).setPageTransformer(true, new ParallaxTransformerDemo(0.5f, 0.5f)).setOnItemClickListener(new PagerUtils.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        }).create();*/
    }


}
