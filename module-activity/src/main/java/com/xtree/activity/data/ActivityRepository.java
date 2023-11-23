package com.xtree.activity.data;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;


import com.xtree.activity.data.source.HttpDataSource;
import com.xtree.activity.data.source.LocalDataSource;

import io.reactivex.Observable;
import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class ActivityRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static ActivityRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private ActivityRepository(@NonNull HttpDataSource httpDataSource,
                               @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static ActivityRepository getInstance(HttpDataSource httpDataSource,
                                                 LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (ActivityRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActivityRepository(httpDataSource, localDataSource);
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
    public @NonNull Observable<Object> login() {
        return mHttpDataSource.login();
    }

    @Override
    public Observable<BaseResponse<Object>> demoGet() {
        return null;
    }

    @Override
    public Observable<BaseResponse<Object>> demoPost(String catalog) {
        return null;
    }

    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }
}
