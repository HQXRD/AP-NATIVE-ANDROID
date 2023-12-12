package com.xtree.bet.data;


import com.xtree.base.net.FBRetrofitClient;
import com.xtree.bet.data.source.LocalDataSource;
import com.xtree.bet.data.source.HttpDataSource;
import com.xtree.bet.data.source.http.HttpDataSourceImpl;
import com.xtree.bet.data.source.local.LocalDataSourceImpl;
import com.xtree.base.net.RetrofitClient;

/**
 * 注入全局的数据仓库
 */
public class Injection {
    public static BetRepository provideHomeRepository() {

        //网络API服务
        FBApiService fbApiService = FBRetrofitClient.getInstance().create(FBApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(fbApiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return BetRepository.getInstance(httpDataSource, localDataSource);
    }
}
