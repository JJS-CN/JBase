package com.jjs;

import com.jjs.demo.EntityJ;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 说明：
 * Created by aa on 2017/9/20.
 */

public class Api {
    public interface Test {
        @GET("front/getLoginDiscount")
        Observable<EntityJ> test(@Query("apikey") String sign, @Query("apikeys") String signs);
    }
}
