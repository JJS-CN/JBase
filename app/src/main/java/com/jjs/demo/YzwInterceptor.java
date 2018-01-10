package com.jjs.demo;

import com.blankj.utilcode.util.LogUtils;
import com.jjs.base.http.BaseInterceptor;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 说明：
 * Created by aa on 2017/12/25.
 */

public class YzwInterceptor extends BaseInterceptor {
    @Override
    protected Request _intercept(Request request) throws IOException {
        /**
         * 在此处进行request的操作
         */
        if (request != null && request.body() != null) {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            //编码设为UTF-8
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = request.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            String body = buffer.readString(charset);
            LogUtils.i("发送----" + "method:" + request.method() + "  url:" + request.url() + "  body:" + body);
           String[] bodys = body.split("&");
            for (int i = 0; i < bodys.length; i++) {
                LogUtils.e(bodys[i]);
                String str = bodys[i];
                int start = str.indexOf("=");
                String key = str.substring(0, start);
                String value = str.substring(start + 1, str.length());
            }
            FormBody.Builder formBody = new FormBody.Builder();
            formBody.add("sign", "111111111111");
            formBody.add("data", "222222222222");
            // 构建 requestBody
            RequestBody requestBody = formBody.build();
            // 构建 budler
            Request.Builder builder = new Request.Builder();
          //  request = builder.url(request.url()).post(requestBody).build();

        }
        return request;
    }
}
