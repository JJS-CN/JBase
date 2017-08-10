package com.jjs.base.http;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * 说明：
 * Created by aa on 2017/8/10.
 */

public class JJsApiService {
    public interface Request {
        @POST("{url}")
        @FormUrlEncoded
        Observable<String> sendPost(@Path("url") String url, @FieldMap Map<String, Object> map);

        @GET("{url}")
        Observable<String> sendGet(@Path("url") String url, @QueryMap Map<String, Object> map);
    }
}
