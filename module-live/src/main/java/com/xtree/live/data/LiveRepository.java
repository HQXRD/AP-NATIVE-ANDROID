package com.xtree.live.data;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.xtree.live.data.source.ApiService;
import com.xtree.live.data.source.HttpDataSource;
import com.xtree.live.data.source.LocalDataSource;

import me.xtree.mvvmhabit.base.BaseModel;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class LiveRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static LiveRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private LiveRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static LiveRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (LiveRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LiveRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public ApiService getApiService() {
        return mHttpDataSource.getApiService();
    }
}
