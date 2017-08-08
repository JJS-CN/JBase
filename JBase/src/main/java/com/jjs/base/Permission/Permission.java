package com.jjs.base.Permission;

import android.support.annotation.NonNull;

/**
 * 本页：
 * Created by jjs on 2017-04-12.
 * Email:994462623@qq.com
 */

public interface Permission {

    @NonNull
    Permission permission(String... permissions);

    @NonNull
    Permission requestCode(int requestCode);

    @NonNull
    Permission rationale(RationaleListener rationaleListener);

    void send();

}
