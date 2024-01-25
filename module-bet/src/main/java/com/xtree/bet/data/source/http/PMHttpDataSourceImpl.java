package com.xtree.bet.data.source.http;


import com.xtree.bet.data.ApiService;
import com.xtree.bet.data.FBApiService;
import com.xtree.bet.data.PMApiService;
import com.xtree.bet.data.source.HttpDataSource;

/**
 * Created by goldze on 2019/3/26.
 */
public class PMHttpDataSourceImpl implements HttpDataSource {
    private ApiService baseApiService;
    private PMApiService apiService;
    private volatile static PMHttpDataSourceImpl INSTANCE = null;

    public static PMHttpDataSourceImpl getInstance(PMApiService apiService, ApiService baseApiService) {
        if (INSTANCE == null) {
            synchronized (PMHttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PMHttpDataSourceImpl(apiService, baseApiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private PMHttpDataSourceImpl(PMApiService apiService, ApiService baseApiService) {
        this.apiService = apiService;
        this.baseApiService = baseApiService;
    }

    @Override
    public FBApiService getApiService() {
        return null;
    }

    @Override
    public PMApiService getPMApiService() {
        return apiService;
    }

    @Override
    public ApiService getBaseApiService() {
        return baseApiService;
    }
}
