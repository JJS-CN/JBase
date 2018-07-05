package com.jjs.demo;

import com.jjs.Api;
import com.jjs.base.http.BaseClient;

/**
 * 说明：
 * Created by jjs on 2018/7/2.
 */

public class ApiClient {
    private static BaseClient mBaseClient;

    public static Api.Test getApi() {
        if (mBaseClient == null) {
            mBaseClient = new BaseClient();
        }
        return mBaseClient.create(Api.Test.class);
    }

    public static void refresh() {
        if (mBaseClient != null) {
            mBaseClient.refresh();
        }
    }
}
