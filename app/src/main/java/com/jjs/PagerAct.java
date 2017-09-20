package com.jjs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jjs.base.JJsActivity;
import com.jjs.base.http.RetrofitUtils;
import com.jjs.base.http.RxSchedulers;
import com.jjs.base.utils.viewpager.PagerUtils;
import com.jjs.base.widget.CustomViewPager;
import com.jjs.demo.HttpResultDemo;
import com.jjs.demo.RxObserverDemo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明：
 * Created by aa on 2017/7/26.
 */

public class PagerAct extends JJsActivity {
    @BindView(R.id.vp)
    CustomViewPager vp;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    List<View> views;

    @Override
    protected void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    protected void onPermissionFailed(int requestCode, List deniedList) {

    }

    @Override
    protected void onPermissionSucceed(int requestCode, List grantList) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        views = new ArrayList<>();
        vp.setCanMove(true);
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            if (i % 2 == 0)
                imageView.setImageResource(R.mipmap.ic_launcher);
            else
                imageView.setImageResource(R.mipmap.ic_launcher_round);
            views.add(imageView);
        }
        new PagerUtils().setView(this, vp, llDots, null, views).create();
        long curr = System.currentTimeMillis();
        Log.e("log", (System.currentTimeMillis() - curr) + "");

        RetrofitUtils.getInstance()
                .create(Api.Test.class)
                .test("ecf9b74c0af93e7ddeadf9f27b65ab4f")
               // .test("cb34ec5716c8784af02f7f5ca12f55d7","eyJzZXJ2aWNlTmFtZSI6ImdldEhvbWVEYXRhSW50ZlNlcnZpY2VJbXBsIn0=")
                .compose(RxSchedulers.getInstance(this.bindToLifecycle()).<HttpResultDemo<String>>io_main())
                .subscribe(new RxObserverDemo<String>() {
                    @Override
                    protected void _onSuccess(String s) {

                    }
                });


    }
}
