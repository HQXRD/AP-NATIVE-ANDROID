package com.xtree.bet.data;

import com.xtree.bet.bean.response.HotLeagueInfo;

import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by goldze on 2017/6/15.
 */

public interface ApiService {
    /**
     * 热门联赛ID列表
     *
     * @return
     */
    @GET("/api/settings/?")
    Flowable<BaseResponse<HotLeagueInfo>> getSettings(@QueryMap Map<String, String> filters);

}
