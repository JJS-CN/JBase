package com.jjs.demo;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import com.jjs.base.Permission.PermissionListener;
import com.jjs.base.Permission.PermissionSteward;

import java.util.List;


/**
 * 本页：
 * Created by jjs on 2017-04-13.
 * Email:994462623@qq.com
 */

/**
 * 第一步：实现 权限监听接口
 */
public class PermissionDemo extends Activity implements PermissionListener {

    /**
     * 第二步：重写替换权限回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionSteward.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 发送权限申请请求(权限为String... 方法，所以可无限添加多个请求同时申请)
     * PermissionSteward.requestPermission(this, 1, Manifest.permission.READ_CALENDAR,Manifest.permission.READ_SMS);
     */

    @Override
    public void onSucceed(int requestCode, List<String> grantPermissionList) {
        //权限申请成功时的回调
        Log.e("PermissionDemo", requestCode + "success");
    }

    @Override
    public void onFailed(int requestCode, List<String> deniedPermissionList) {
        //权限申请失败时的回调
        Log.e("PermissionDemo", "code:" + requestCode);
        /**
         * 判断失败的权限
         */
        for (int i = 0; i < deniedPermissionList.size(); i++) {
            if (deniedPermissionList.get(i).equals(Manifest.permission.READ_SMS)) {
                Log.e("PermissionDemo", "sms:Error");
            } else if (deniedPermissionList.get(i).equals(Manifest.permission.READ_CONTACTS)) {
                Log.e("PermissionDemo", "contacts:Error");
            }
        }
        /**
         * 由于未给予权限，对用户进行提示
         */
        // 如果用户对权限申请操作设置了不再提醒，则提示用户到应用设置界面主动授权
        if (PermissionSteward.hasAlwaysDeniedPermission(this, deniedPermissionList)) {
            //默认提示语
            PermissionSteward.defaultSettingDialog(this, 2).show();
            //自定义提示语
        /*    PermissionSteward.defaultSettingDialog(this, 2)
                    .setTitle("权限申请失败")
                    .setMessage("需要的一些权限被拒绝授权，请到设置页面手动授权，否则功能无法正常使用")
                    .setPositiveButton("好，去设置")
                    .show();*/
        }
    }
}
