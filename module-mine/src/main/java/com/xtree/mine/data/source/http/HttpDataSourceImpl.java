package com.xtree.mine.data.source.http;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xtree.mine.data.source.APIManager;
import com.xtree.mine.data.source.HttpDataSource;
import com.xtree.mine.data.source.http.service.HttpApiService;
import com.xtree.mine.vo.request.GameRebateAgrtRequest;
import com.xtree.mine.vo.request.GameSubordinateAgrteRequest;
import com.xtree.mine.vo.request.GameSubordinateRebateRequest;
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
    private static Type type;
    private HttpApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(HttpApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                    gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().enableComplexMapKeySerialization().create();
                    type = new TypeToken<HashMap<String, Object>>() {
                    }.getType();
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
    public Flowable<GameRebateAgrtResponse> getGameRebateAgrtData(GameRebateAgrtRequest request) {
        String json = gson.toJson(request);
        HashMap<String, Object> map = gson.fromJson(json, type);
        map.put("", "");
        return apiService.get(APIManager.GAMEREBATEAGRT_URL, map).map(new Function<ResponseBody, GameRebateAgrtResponse>() {
            @Override
            public GameRebateAgrtResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), GameRebateAgrtResponse.class);
            }
        });
    }

    @Override
    public Flowable<GameSubordinateAgrteResponse> getGameSubordinateAgrteData(GameSubordinateAgrteRequest request) {
        String json = gson.toJson(request);
        HashMap<String, Object> map = gson.fromJson(json, type);
        map.put("", "");
        return apiService.get(APIManager.GAMESUBORDINATEAGRTE_URL, map).map(new Function<ResponseBody, GameSubordinateAgrteResponse>() {
            @Override
            public GameSubordinateAgrteResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), GameSubordinateAgrteResponse.class);
            }
        });
    }

    @Override
    public Flowable<GameSubordinateRebateResponse> getGameSubordinateRebateData(GameSubordinateRebateRequest request) {
        String json = gson.toJson(request);
        HashMap<String, Object> map = gson.fromJson(json, type);
        map.put("", "");
        return apiService.get(APIManager.GAMESUBORDINATEREBATE_URL, map).map(new Function<ResponseBody, GameSubordinateRebateResponse>() {
            @Override
            public GameSubordinateRebateResponse apply(ResponseBody responseBody) throws Exception {
                return gson.fromJson(responseBody.string(), GameSubordinateRebateResponse.class);
            }
        });
    }


}
