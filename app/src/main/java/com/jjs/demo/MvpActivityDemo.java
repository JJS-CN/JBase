package com.jjs.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jjs.base.JJsActivity;
import com.jjs.base.bean.HashBean;

import java.util.List;

/**
 * 说明：aty创建时填入具体P层，并实现具体P层需要的view接口
 * Created by aa on 2017/7/18.
 */

public class MvpActivityDemo extends JJsActivity<PP> implements VV {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPersenter = new PP(this);//进行实例化，this为PP编写时填入的view接口
        mPersenter.cccc(new HashBean(1));//之后进行具体操作即可

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
