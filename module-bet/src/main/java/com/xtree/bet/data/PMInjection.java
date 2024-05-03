package com.xtree.bet.data;


import com.xtree.base.net.FBRetrofitClient;
import com.xtree.base.net.PMRetrofitClient;
import com.xtree.base.net.RetrofitClient;
import com.xtree.bet.data.source.HttpDataSource;
import com.xtree.bet.data.source.LocalDataSource;
import com.xtree.bet.data.source.http.HttpDataSourceImpl;
import com.xtree.bet.data.source.http.PMHttpDataSourceImpl;
import com.xtree.bet.data.source.local.LocalDataSourceImpl;

/**
 * 注入全局的数据仓库
 */
public class PMInjection {
    public static BetRepository provideHomeRepository(boolean reNew) {
        //网络API服务
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        //网络API服务
        PMApiService pmApiService = PMRetrofitClient.getInstance().create(PMApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = PMHttpDataSourceImpl.getInstance(pmApiService, apiService, reNew);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return BetRepository.getInstance(httpDataSource, localDataSource);
    }
}
