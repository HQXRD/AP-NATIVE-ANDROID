package com.xtree.bet.data.source.http;


import com.xtree.bet.data.ApiService;
import com.xtree.bet.data.FBApiService;
import com.xtree.bet.data.PMApiService;
import com.xtree.bet.data.source.HttpDataSource;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private ApiService baseApiService;
    private FBApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(FBApiService apiService, ApiService baseApiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService, baseApiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(FBApiService apiService, ApiService baseApiService) {
        this.apiService = apiService;
        this.baseApiService = baseApiService;
    }

    @Override
    public FBApiService getApiService() {
        return apiService;
    }

    @Override
    public PMApiService getPMApiService() {
        return null;
    }

    @Override
    public ApiService getBaseApiService() {
        return baseApiService;
    }
}
