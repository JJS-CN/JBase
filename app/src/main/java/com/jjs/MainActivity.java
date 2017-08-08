package com.jjs;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.jjs.base.JJsActivity;
import com.jjs.base.Permission.PermissionListener;
import com.jjs.base.Permission.PermissionSteward;
import com.jjs.base.a_demo.PP;
import com.jjs.base.bean.BusBean;
import com.jjs.base.http.RetrofitUtils;
import com.jjs.base.mvp.RxBus;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends JJsActivity<PP> implements PermissionListener {


    @Override
    public void onActivityResult(int requestCode, Intent data) {

    }

    @Override
    public void onPermissionSucceed(int requestCode, List<String> grantList) {

    }

    @Override
    public void onPermissionFailed(int requestCode, List<String> deniedList) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RetrofitUtils.init("http://www.baidu.com/");
        RetrofitUtils.updateHeaders(new HashMap<String, String>());
        mPersenter.cccc();
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().send(new BusBean().setAction("action").setType(100).setData(new BusBean().setData("StringData")));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionSteward.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onSucceed(int requestCode, List<String> grantPermissionList) {
        ToastUtils.showShort("success" + requestCode + "==" + grantPermissionList.toString());
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissionList) {
        ToastUtils.showShort("error:" + requestCode + "==" + deniedPermissionList.toString());
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        PermissionSteward.requestPermission(this, 1, Manifest.permission.CALL_PHONE);
    }
}
