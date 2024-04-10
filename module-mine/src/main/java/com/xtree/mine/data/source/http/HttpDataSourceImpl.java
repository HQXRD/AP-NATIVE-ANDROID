package com.xtree.mine.data.source.http;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xtree.mine.data.source.APIManager;
import com.xtree.mine.data.source.HttpDataSource;
import com.xtree.mine.data.source.http.service.HttpApiService;
import com.xtree.mine.vo.request.CommissionsReportsRequest;
import com.xtree.mine.vo.request.DividendAgrtCheckRequest;
import com.xtree.mine.vo.request.DividendAgrtCreateRequest;
import com.xtree.mine.vo.request.DividendAgrtSendQuery;
import com.xtree.mine.vo.request.DividendAgrtSendRequest;
import com.xtree.mine.vo.request.DividendAutoSendRequest;
import com.xtree.mine.vo.request.DividendAutoSentQuery;
import com.xtree.mine.vo.request.GameDividendAgrtRequest;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
import com.xtree.mine.vo.request.RebateAgrtCreateQuery;
import com.xtree.mine.vo.request.RebateAgrtCreateRequest;
import com.xtree.mine.vo.request.RecommendedReportsRequest;
import com.xtree.mine.vo.response.CommissionsReportsResponse;
import com.xtree.mine.vo.response.DividendAgrtCheckResponse;
import com.xtree.mine.vo.response.DividendAgrtCreateResponse;
import com.xtree.mine.vo.response.DividendAgrtSendReeponse;
import com.xtree.mine.vo.response.DividendAutoSendResponse;
import com.xtree.mine.vo.response.FunctionMenuResponse;
import com.xtree.mine.vo.response.GameDividendAgrtResponse;
import com.xtree.mine.vo.response.GameRebateAgrtResponse;
import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;
import com.xtree.mine.vo.response.GameSubordinateRebateResponse;
import com.xtree.mine.vo.response.RebateAgrtCreateResponse;
import com.xtree.mine.vo.response.RecommendedReportsResponse;

import java.util.HashMap;
import java.util.List;
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
    public Flowable<DividendAutoSendResponse> getDividendAutoSendData(DividendAutoSentQuery query, DividendAutoSendRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);

        String queryJson = JSON.toJSONString(query);
        Map<String, Object> queryMap = JSON.parseObject(queryJson, type);
        return apiService.post(APIManager.GAMEDIVIDENDAGRT_AUTOSEND_URL, queryMap, map).map(new Function<ResponseBody, DividendAutoSendResponse>() {
            @Override
            public DividendAutoSendResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), DividendAutoSendResponse.class);
            }
        });
    }

    @Override
    public Flowable<RecommendedReportsResponse> getRecommendedReportsData(RecommendedReportsRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(APIManager.GAMEDIVIDENDAGRT_URL, map).map(new Function<ResponseBody, RecommendedReportsResponse>() {
            @Override
            public RecommendedReportsResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), RecommendedReportsResponse.class);
            }
        });
    }

    @Override
    public Flowable<RebateAgrtCreateResponse> getRebateAgrtCreateData(RebateAgrtCreateQuery query, RebateAgrtCreateRequest request) {

        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);

        String queryJson = JSON.toJSONString(query);
        Map<String, Object> queryMap = JSON.parseObject(queryJson, type);

        return apiService.post("", queryMap, map).map(new Function<ResponseBody, RebateAgrtCreateResponse>() {
            @Override
            public RebateAgrtCreateResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), RebateAgrtCreateResponse.class);
            }
        });
    }

    @Override
    public Flowable<DividendAgrtCheckResponse> getDividendAgrtData(DividendAgrtCheckRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(APIManager.GAMEDIVIDENDAGRT_DETAIL_URL, map).map(new Function<ResponseBody, DividendAgrtCheckResponse>() {
            @Override
            public DividendAgrtCheckResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), DividendAgrtCheckResponse.class);
            }
        });
    }

    @Override
    public Flowable<DividendAgrtSendReeponse> getDividendAgrtSendStep1Data(DividendAgrtSendQuery query, DividendAgrtSendRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);

        String queryJson = JSON.toJSONString(query);
        Map<String, Object> queryMap = JSON.parseObject(queryJson, type);

        return apiService.post(APIManager.GAMEDIVIDENDAGRT_SEND_STEP1_URL, queryMap, map).map(new Function<ResponseBody, DividendAgrtSendReeponse>() {
            @Override
            public DividendAgrtSendReeponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), DividendAgrtSendReeponse.class);
            }
        });
    }

    @Override
    public Flowable<DividendAgrtSendReeponse> getDividendAgrtSendStep2Data(DividendAgrtSendQuery query, DividendAgrtSendRequest request) {

        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);

        String queryJson = JSON.toJSONString(query);
        Map<String, Object> queryMap = JSON.parseObject(queryJson, type);

        return apiService.post(APIManager.GAMEDIVIDENDAGRT_SEND_STEP2_URL, queryMap, map).map(new Function<ResponseBody, DividendAgrtSendReeponse>() {
            @Override
            public DividendAgrtSendReeponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), DividendAgrtSendReeponse.class);
            }
        });
    }

    @Override
    public Flowable<BaseResponse<List<FunctionMenuResponse>>> getFunctionMenuData() {
        return apiService.get(APIManager.FUNCTIONT_MENUS_URL).map(new Function<ResponseBody, BaseResponse<List<FunctionMenuResponse>>>() {
            @Override
            public BaseResponse<List<FunctionMenuResponse>> apply(ResponseBody responseBody) throws Exception {
                return JSON.parseObject(responseBody.string(),
                        new TypeReference<BaseResponse<List<FunctionMenuResponse>>>() {});
            }
        });
    }

    @Override
    public Flowable<CommissionsReportsResponse> getCommissionsData(CommissionsReportsRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        return apiService.get(APIManager.COMMISSIONS_REPORTS_URL, map).map(new Function<ResponseBody, CommissionsReportsResponse>() {
            @Override
            public CommissionsReportsResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), CommissionsReportsResponse.class);
            }
        });
    }

    @Override
    public Flowable<DividendAgrtCreateResponse> getDividendAgrtCreateData(DividendAgrtCreateRequest request) {
        String json = JSON.toJSONString(request);
        Map<String, Object> map = JSON.parseObject(json, type);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("1", 1);
        queryMap.put("client", "m");
        return apiService.post(APIManager.DIVIDENDAGRT_CREATE_URL, queryMap, map).map(new Function<ResponseBody, DividendAgrtCreateResponse>() {
            @Override
            public DividendAgrtCreateResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), DividendAgrtCreateResponse.class);
            }
        });
    }
}
