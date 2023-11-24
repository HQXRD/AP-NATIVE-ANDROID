package com.xtree.bet.data;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.xtree.bet.data.source.HttpDataSource;
import com.xtree.bet.data.source.LocalDataSource;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class BetRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static BetRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private BetRepository(@NonNull HttpDataSource httpDataSource,
                          @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static BetRepository getInstance(HttpDataSource httpDataSource,
                                            LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (BetRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BetRepository(httpDataSource, localDataSource);
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
    public Flowable<BaseResponse<Object>> login(String username, String password) {
        return mHttpDataSource.login(username, password);
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
