package com.xtree.home.data.source;

import com.xtree.base.vo.FBService;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.CookieVo;
import com.xtree.home.vo.DataVo;
import com.xtree.home.vo.GameStatusVo;
import com.xtree.home.vo.LoginResultVo;
import com.xtree.home.vo.NoticeVo;
import com.xtree.home.vo.ProfileVo;
import com.xtree.home.vo.SettingsVo;
import com.xtree.home.vo.VipInfoVo;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface HomeApiService {

    @FormUrlEncoded
    @POST("/api/auth/login")
    Flowable<BaseResponse<Object>> login(@Field("username") String username, @Field("password") String password);

    /**
     * 获取 banner列表
     *
     * @return
     */
    @GET("/api/bns/4/banners?limit=20")
    Flowable<BaseResponse<List<BannersVo>>> getBanners();

    /**
     * 获取 公告列表
     *
     * @return
     */
    @GET("/api/notice/list?page=1&per_page=10&sort=-istop,-sendtime")
    Flowable<BaseResponse<DataVo<NoticeVo>>> getNotices();

    /**
     * 获取 游戏状态列表（主要是游戏的状态,别名）
     *
     * @return
     */
    @GET("/api/game/status")
    Flowable<BaseResponse<List<GameStatusVo>>> getGameStatus();

    /**
     * 获取 游戏的链接
     *
     * @return
     */
    @GET("/api/game/{gameAlias}/playurl")
    Flowable<BaseResponse<Map<String, String>>> getPlayUrl(@Path("gameAlias") String gameAlias, @QueryMap Map<String, String> filters);

    /**
     * 获取 配置信息（主要是公钥,客服链接）
     *
     * @return
     */
    @GET("/api/settings/?")
    Flowable<BaseResponse<SettingsVo>> getSettings(@QueryMap Map<String, String> filters);

    /**
     * 登录
     *
     * @return
     */
    @POST("/api/auth/login")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<LoginResultVo>> login(@Body Map<String, String> map);

    /**
     * 获取 cookie,session
     *
     * @return
     */
    @GET("/api/auth/sessid?client_id=10000005")
    Flowable<BaseResponse<CookieVo>> getCookie();

    /**
     * 获取 个人信息
     *
     * @return
     */
    @GET("/api/account/profile")
    Flowable<BaseResponse<ProfileVo>> getProfile();

    /**
     * 获取 VIP信息
     */
    @GET("/api/account/vipinfo")
    Flowable<BaseResponse<VipInfoVo>> getVipInfo();

    /**
     * 获取 FB体育请求服务地址
     * @return
     */
    @POST("/api/sports/fb/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<FBService>> getFBGameTokenApi();

    /**
     * 获取 PM体育请求服务地址
     * @return
     */
    @POST("/api/sports/obg/getToken?cachedToken=1")
    @Headers({"Content-Type: application/vnd.sc-api.v1.json"})
    Flowable<BaseResponse<String>> getPMGameTokenApi();

}
