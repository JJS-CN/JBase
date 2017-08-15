package com.jjs.base.a_demo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jjs.base.JJsActivity;
import com.jjs.base.R;
import com.jjs.base.bean.BusBean;
import com.jjs.base.bean.HashBean;
import com.jjs.base.widget.BaseDialog;

import java.util.List;

/**
 * 说明：aty创建时填入具体P层，并实现具体P层需要的view接口
 * Created by aa on 2017/7/18.
 */

public class MvpActivityDemo extends JJsActivity<PP> implements VV {
    @Override
    public void onCreateView(@Nullable Bundle savedInstanceState) {
        mPersenter = new PP(this);//进行实例化，this为PP编写时填入的view接口
        mPersenter.cccc(new HashBean(1));//之后进行具体操作即可
        Dialog dialog = new BaseDialog(this)
                .setView(R.layout.layout_load)
                .setClick(R.id.background_style_ripple, R.id.background_style_default)
                .setListener(new BaseDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(View rootView, View checkView) {
                        int i = checkView.getId();
                        if (i == R.id.tv_prompt) {
                        }
                    }
                })
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .create();
        new RxSubjectDemo<BusBean>() {
            @Override
            protected void _onSuccess(BusBean busBean) {

            }

            @Override
            public boolean showToast() {
                return false;
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {


    }

    @Override
    public void onPermissionFailed(int requestCode, List deniedList) {

    }

    @Override
    public void onPermissionSucceed(int requestCode, List grantList) {

    }

    @Override
    public void aa() {
    }

    @Override
    public void ResponseSuccess(int requestCode, String data) {

    }
}
