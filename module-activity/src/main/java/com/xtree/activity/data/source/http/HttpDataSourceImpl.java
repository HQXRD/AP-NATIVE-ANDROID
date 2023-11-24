package com.xtree.activity.data.source.http;


import com.xtree.activity.data.source.ApiService;
import com.xtree.activity.data.source.HttpDataSource;

import io.reactivex.Flowable;
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
