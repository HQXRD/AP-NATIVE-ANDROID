package com.xtree.main.data.source.http;


import com.xtree.main.data.source.HttpDataSource;
import com.xtree.main.data.source.MainApiService;

/**
 * Created by goldze on 2019/3/26.
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private MainApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(MainApiService apiService, boolean reNew) {
        if(reNew){
            synchronized (HttpDataSourceImpl.class) {
                INSTANCE = new HttpDataSourceImpl(apiService);
            }
        }else {
            if (INSTANCE == null) {
                synchronized (HttpDataSourceImpl.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new HttpDataSourceImpl(apiService);
                    }
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(MainApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public MainApiService getApiService() {
        return apiService;
    }
}
