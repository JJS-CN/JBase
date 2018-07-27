package com.jjs.demo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjs.R;
import com.jjs.base.base.BaseActivity;
import com.jjs.base.base.ChoosePhotoActivity;
import com.jjs.base.http.RxHelper;

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
    }

    @OnClick({R.id.tv_Camera, R.id.tv_Album})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_Camera:
                ApiClient.getApi().test("", "")
                        .compose(RxHelper.getNewInstance(ViewPagerDemo.this).<EntityJ>bind())
                        .subscribe(new RxObserverDemo<EntityJ>() {
                            @Override
                            protected void _onSuccess(EntityJ data) {

                            }

                            @Override
                            public boolean showToast() {
                                return false;
                            }
                        });
                break;
            case R.id.tv_Album:
                ChoosePhotoActivity.start(this, R.id.tv_Album + "", false, 16, 9);
                break;
        }
    }
}
