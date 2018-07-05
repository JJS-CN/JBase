package com.jjs;

import com.jjs.demo.HttpResultDemo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 说明：
 * Created by aa on 2017/9/20.
 */

public class Api  {
    public interface Test {
        @FormUrlEncoded
        @POST("clothing_classificati")
        Observable<HttpResultDemo<Object>> test(@Field("apikey") String sign, @Field("apikeys") String signs);
    }
}
