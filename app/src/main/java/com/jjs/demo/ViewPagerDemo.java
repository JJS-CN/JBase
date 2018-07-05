package com.jjs.demo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.rxbus.RxBus;
import com.jjs.R;
import com.jjs.base.base.BaseActivity;
import com.jjs.base.base.ChoosePhotoActivity;
import com.jjs.base.entity.ChoosePhotoEntity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者： Jacky
 * 日期：2017-04-18
 * 轮播引导页页面
 */

public class ViewPagerDemo extends BaseActivity {

    @BindView(R.id.tv_Camera)
    TextView mTvCamera;
    @BindView(R.id.tv_Album)
    TextView mTvAlbum;
    @BindView(R.id.ll_bg)
    LinearLayout mLlBg;

    @Override
    protected void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);
        RxBus.getDefault().subscribe(this, R.id.tv_Album + "", new RxBus.Callback<ChoosePhotoEntity>() {
            @Override
            public void onEvent(ChoosePhotoEntity strings) {
                int size = (int) (strings.getOriginalFile().length() / 1024);
                int size2 = (int) (strings.getCompressFile().length() / 1024);
                Log.e("aaaa", strings.getOriginalFile().getPath() + "==" + size);
                Log.e("eeee", strings.getCompressFile().getPath() + "==" + size2);
            }
        });
        RxBus.getDefault().subscribe(this, R.id.tv_Camera + "", new RxBus.Callback<ChoosePhotoEntity>() {
            @Override
            public void onEvent(ChoosePhotoEntity strings) {
                int size = (int) (strings.getOriginalFile().length() / 1024);
                int size2 = (int) (strings.getCompressFile().length() / 1024);
                Log.e("assss", strings.getOriginalFile().getPath() + "==" + size);
                Log.e("asdasf", strings.getCompressFile().getPath() + "==" + size2);
            }
        });
    }

    @OnClick({R.id.tv_Camera, R.id.tv_Album})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_Camera:
                ChoosePhotoActivity.start(this, R.id.tv_Camera + "", true,1,1);
                break;
            case R.id.tv_Album:
                ChoosePhotoActivity.start(this, R.id.tv_Album + "", false,16,9);
                break;
        }
    }
}
