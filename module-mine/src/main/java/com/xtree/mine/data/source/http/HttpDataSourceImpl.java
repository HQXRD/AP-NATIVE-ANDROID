package com.xtree.mine.data.source.http;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xtree.mine.data.source.APIManager;
import com.xtree.mine.data.source.HttpDataSource;
import com.xtree.mine.data.source.http.service.HttpApiService;
import com.xtree.mine.vo.request.DividendAutoSendRequest;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
import com.xtree.mine.vo.response.DividendAutoSendResponse;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.GameSubordinateRebateResponse;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;
import okhttp3.ResponseBody;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private static Gson gson;
    private static TypeReference<Map<String, Object>> type;
    private HttpApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(HttpApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                    gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().create();
                    type = new TypeReference<Map<String, Object>>() {};
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(HttpApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public @NonNull Observable<Object> login() {
        return Observable.just(new Object()).delay(3, TimeUnit.SECONDS); //延迟3秒
    }

    @Override
    public Observable<BaseResponse<Object>> demoGet() {
        return apiService.demoGet();
    }

    @Override
    public Observable<BaseResponse<Object>> demoPost(String catalog) {
        return apiService.demoPost(catalog);
    }

    @Override
    public HttpApiService getApiService() {
        return apiService;
    }

    @Override
    public Flowable<GameRebateAgrtResponse> getGameRebateAgrtData(String url, GameRebateAgrtRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(url, map).map(new Function<ResponseBody, GameRebateAgrtResponse>() {
            @Override
            public GameRebateAgrtResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), GameRebateAgrtResponse.class);
            }
        });
    }

    @Override
    public Flowable<GameSubordinateAgrteResponse> getGameSubordinateAgrteData(String url, GameSubordinateAgrteRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(url, map).map(new Function<ResponseBody, GameSubordinateAgrteResponse>() {
            @Override
            public GameSubordinateAgrteResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), GameSubordinateAgrteResponse.class);
            }
        });
    }

    @Override
    public Flowable<GameSubordinateRebateResponse> getGameSubordinateRebateData(String url, GameSubordinateRebateRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(url, map).map(new Function<ResponseBody, GameSubordinateRebateResponse>() {
            @Override
            public GameSubordinateRebateResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), GameSubordinateRebateResponse.class);
            }
        });
    }

    @Override
    public Flowable<GameDividendAgrtResponse> getGameDividendAgrtData(GameDividendAgrtRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(APIManager.GAMEDIVIDENDAGRT_URL, map).map(new Function<ResponseBody, GameDividendAgrtResponse>() {
            @Override
            public GameDividendAgrtResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), GameDividendAgrtResponse.class);
            }
        });
    }

    @Override
    public Flowable<DividendAutoSendResponse> getDividendAutoSendData(DividendAutoSendRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(APIManager.GAMEDIVIDENDAGRT_AUTOSEND_URL, map).map(new Function<ResponseBody, DividendAutoSendResponse>() {
            @Override
            public DividendAutoSendResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), DividendAutoSendResponse.class);
            }
        });
    }
}
