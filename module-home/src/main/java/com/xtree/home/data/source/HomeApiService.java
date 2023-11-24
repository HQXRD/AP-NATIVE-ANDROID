package com.xtree.home.data.source;

import com.xtree.home.vo.BannersVo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface HomeApiService {

    @FormUrlEncoded
    @POST("auth/login")
    Flowable<BaseResponse<Object>> login(@Field("username") String username, @Field("password") String password);

    @GET("bns/4/banners?limit=20")
    Flowable<BaseResponse<List<BannersVo>>> getBanners();

}
