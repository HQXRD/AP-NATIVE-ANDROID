package com.xtree.home.data;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    Flowable<BaseResponse<Object>> login(@Field("username") String username, @Field("password") String password);


}
