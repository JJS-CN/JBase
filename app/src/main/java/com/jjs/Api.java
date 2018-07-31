package com.jjs;

import com.jjs.demo.EntityJ;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 说明：
 * Created by aa on 2017/9/20.
 */

public class Api {
    public interface Test {
        @POST("front/getLoginDiscount")
        @FormUrlEncoded
        Observable<EntityJ> test(@Field("pai_name") String apiname, @Field("apikeys") String signs);
    }

}
