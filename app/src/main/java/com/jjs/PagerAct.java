package com.jjs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明：
 * Created by aa on 2017/7/26.
 */

public class PagerAct extends Activity {
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    List<View> views;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
     /*   views = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.aa1);
            views.add(imageView);
        }
        new PagerUtils().setView(vp, llDots, null, views).setPageTransformer(true, new ParallaxPageTransformer(R.id.background_style_default)).create();*/
        long curr = System.currentTimeMillis();
        Log.e("log", (System.currentTimeMillis() - curr) + "");


    }

    @JavascriptInterface
    public void getData() {

    }
}
