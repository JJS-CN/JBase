package com.jjs;

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
        Observable<String> test(@Field("apikey") String sign,@Field("apikeys") String signs);
    }
}
