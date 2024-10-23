package com.xtree.live.data.source.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xtree.base.net.live.LiveClient;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.live.data.source.APIManager;
import com.xtree.live.data.source.ApiService;
import com.xtree.live.data.source.HttpDataSource;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.response.LiveTokenResponse;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.ResponseBody;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {

    private static TypeReference<Map<String, Object>> type;
    private ApiService apiService;
    private ApiService liveService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(ApiService apiService, ApiService liveService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService, liveService);
                    type = new TypeReference<Map<String, Object>>() {
                    };
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(ApiService apiService, ApiService liveService) {
        this.apiService = apiService;
        this.liveService = liveService;
    }

    @Override
    public ApiService getApiService() {
        return liveService;
    }

    @Override
    public void setLive(LiveTokenResponse liveData) {

        X9LiveInfo.INSTANCE.setChannel(liveData.getChannelCode());
        X9LiveInfo.INSTANCE.setToken(liveData.getXLiveToken());
        X9LiveInfo.INSTANCE.setVisitor(liveData.getVisitorId());

        LiveClient.setApi(liveData.getAppApi().get(0));
        liveService = LiveClient.getInstance().create(ApiService.class);
    }

    @Override
    public Flowable<BaseResponse<LiveTokenResponse>> getLiveToken(LiveTokenRequest request) {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(request), type);
        return apiService.get(APIManager.X9_TOKEN_URL,map).map(new Function<ResponseBody, BaseResponse<LiveTokenResponse>>() {
            @Override
            public BaseResponse<LiveTokenResponse> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<LiveTokenResponse>>() {
                        });
            }
        });
    }
}
