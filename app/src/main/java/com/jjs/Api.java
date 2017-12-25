package com.jjs;

import com.jjs.demo.HttpResultDemo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 说明：
 * Created by aa on 2017/9/20.
 */

public class Api  {
    public interface Test {
        @GET("clothing_classificati")
        Observable<HttpResultDemo<String>> test(@Query("apikey") String sign);
    }
}
