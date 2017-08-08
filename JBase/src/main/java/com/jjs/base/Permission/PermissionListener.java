package com.jjs.base.Permission;

import java.util.List;

/**
 * 本页：
 * Created by jjs on 2017-04-12.
 * Email:994462623@qq.com
 */

public interface PermissionListener {

    /**
     * 权限全部申请通过时回调
     *
     * @param requestCode      请求码
     * @param grantPermissionList 申请通过的全部权限
     */
    void onSucceed(int requestCode, List<String> grantPermissionList);

    /**
     * 权限没有全部申请通过时回调
     *
     * @param requestCode       请求码
     * @param deniedPermissionList 没有申请通过的权限
     */
    void onFailed(int requestCode, List<String> deniedPermissionList);

}
