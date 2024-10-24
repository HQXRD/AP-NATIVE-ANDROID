package com.xtree.live.data.source;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiService {

    /**
     * GET
     *
     * @param url 接口名称
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url);

    /**
     * GET
     *
     * @param url 接口名称
     * @param map 拼接参数
     * @return 返回体
     */
    @GET("{url}")
    Flowable<ResponseBody> get(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> map);

    /**
     * POST
     *
     * @param url 接口名称
     * @param map body
     * @return 返回体
     */
    @POST("{url}")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @Body Map<String, Object> map);

    /**
     * POST
     *
     * @param url  接口名称
     * @param qmap 拼接参数
     * @param map  body
     * @return 返回体
     */
    @POST("{url}")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<ResponseBody> post(@Path(value = "url", encoded = true) String url, @QueryMap(encoded = true) Map<String, Object> qmap, @Body Map<String, Object> map);
}
