package com.jjs.base.http;

import java.io.IOException;

import okhttp3.Request;

/**
 * 说明：固定参数拼接在url中，进行处理
 * Created by jjs on 2018/7/2.
 */

public class FixedInterceptor extends BaseInterceptor {
    @Override
    protected void _intercept(Request request) throws IOException {
        if (canInjectIntoBody(request)) {
            String[] urls = request.url().toString().split("\\?");
            if (urls.length >= 2) {
                String[] params = urls[1].split("&");
                for (int i = 0; i < params.length; i++) {
                    String[] keyValue = params[i].split("=");
                    this.addFieldParam(keyValue[0], keyValue[1]);
                }
            }
            request = request.newBuilder().url(urls[0]).build();
        }
    }
}
