package com.xtree.recharge.data.source.http;


import androidx.annotation.NonNull;

import com.xtree.base.service.ApiService;
import com.xtree.recharge.data.source.HttpDataSource;


import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private ApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Flowable<BaseResponse<Object>> login(String username, String password) {
        return apiService.login(username, password);
    }
}
