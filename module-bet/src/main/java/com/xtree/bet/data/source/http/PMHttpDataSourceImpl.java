package com.xtree.bet.data.source.http;


import com.xtree.bet.data.FBApiService;
import com.xtree.bet.data.PMApiService;
import com.xtree.bet.data.source.HttpDataSource;

/**
 * Created by goldze on 2019/3/26.
 */
public class PMHttpDataSourceImpl implements HttpDataSource {
    private PMApiService apiService;
    private volatile static PMHttpDataSourceImpl INSTANCE = null;

    public static PMHttpDataSourceImpl getInstance(PMApiService apiService) {
        if (INSTANCE == null) {
            synchronized (PMHttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PMHttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private PMHttpDataSourceImpl(PMApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public FBApiService getApiService() {
        return null;
    }

    @Override
    public PMApiService getPMApiService() {
        return apiService;
    }
}
