package com.jjs;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.jjs.base.base.BaseActivity;
import com.jjs.base.bean.RxBusBean;
import com.jjs.base.http.RxBus;
import com.jjs.base.utils.viewpager.PagerUtils;
import com.jjs.base.widget.CustomViewPager;
import com.jjs.base.widget.ReadMoreTextView;
import com.jjs.demo.AAFragment;
import com.jjs.demo.RxObserverDemo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明：
 * Created by aa on 2017/7/26.
 */

public class PagerAct extends BaseActivity {
    @BindView(R.id.vp)
    CustomViewPager vp;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    List<View> views;
    @BindView(R.id.read)
    ReadMoreTextView mRead;

    @Override
    protected void onActivityResult(int requestCode, Intent data) {
        new RxObserverDemo<String>() {
            @Override
            protected void _onSuccess(String s) {

            }

            @Override
            protected void _onComplete() {
                super._onComplete();
            }

            @Override
            protected void _onError(String msg) {
                super._onError(msg);
            }
        };
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        ButterKnife.bind(this);
        views = new ArrayList<>();
        mRead.setText("我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得我觉得你觉得大家觉得");
        llDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRead.setText("123");
                RxBus.send(0, null, null);
            }
        });
        vp.setCanMove(true);
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            if (i % 2 == 0)
                Glide.with(this).load("http://img0.imgtn.bdimg.com/it/u=4204473876,3868286801&fm=214&gp=0.jpg").into(imageView);
            else
                //GlideApp.with(this).load("http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=1a3963408f94a4c20a76ef2f3bc437e3/e4dde71190ef76c66b5e79649516fdfaae5167f5.jpg").into(imageView);
                imageView.setImageResource(R.mipmap.ic_launcher_round);
            views.add(imageView);
        }
        new PagerUtils().setView(this, vp, llDots, null, views).setOnItemClickListener(new PagerUtils.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(PagerAct.this, XXXX.class));
            }
        }).create();


        PermissionUtils.requestPermissions(this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.e("111111111", "111111111");
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                Log.e("2222222222222222", "222222222222222");
            }
        });
        addFragment(R.id.fff, new AAFragment(), "", false);
        RxBus.with(this).setListener(new RxBus.OnRxBusListener() {
            @Override
            public void onBusListener(RxBusBean busBean) {
                LogUtils.e("bus1");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
