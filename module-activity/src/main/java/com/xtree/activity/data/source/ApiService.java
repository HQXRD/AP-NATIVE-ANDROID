package com.xtree.activity.data.source;

import com.xtree.activity.vo.NewVo;

import java.util.List;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    Flowable<BaseResponse<Object>> login(@Field("username") String username, @Field("password") String password);

    /**
     * 获取 活动列表 /api/activity/getNewList
     *
     * @return
     */
    @GET("/api/activity/getNewList")
    Flowable<BaseResponse<List<NewVo>>> getNewList();

}
