package com.xtree.bet.data;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by goldze on 2017/6/15.
 */

public interface FBApiService {
    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> getFBGameTokenApi();
}
