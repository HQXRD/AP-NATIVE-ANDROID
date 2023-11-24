package com.xtree.home.data.source;

import com.xtree.home.vo.BannersVo;

import java.util.Map;

import io.reactivex.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface HomeApiService {

    @GET("action/apiv2/banner?catalog=1")
    Observable<BaseResponse<Object>> demoGet();

    @FormUrlEncoded
    @POST("action/apiv2/banner")
    Observable<BaseResponse<Object>> demoPost(@Field("catalog") String catalog);

    @FormUrlEncoded
    @POST("auth/login")
    Observable<BaseResponse<Object>> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/auth/login")
    Observable<BaseResponse<Object>> login(@QueryMap Map<String, String> filters);

    @GET("/api/bns/4/banners?limit=20")
    Observable<BaseResponse<BannersVo>> getBanners();


}
