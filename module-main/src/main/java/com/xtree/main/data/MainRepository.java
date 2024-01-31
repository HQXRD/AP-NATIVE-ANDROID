package com.xtree.main.data;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;


import com.xtree.main.data.source.MainApiService;
import com.xtree.main.data.source.HttpDataSource;
import com.xtree.main.data.source.LocalDataSource;

import me.xtree.mvvmhabit.base.BaseModel;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class MainRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static MainRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private MainRepository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static MainRepository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (MainRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainRepository(httpDataSource, localDataSource);
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
    public MainApiService getApiService() {
        return mHttpDataSource.getApiService();
    }
}
