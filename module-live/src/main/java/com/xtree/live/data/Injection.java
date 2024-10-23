package com.xtree.live.data;


import com.xtree.base.net.live.LiveClient;
import com.xtree.base.net.RetrofitClient;
import com.xtree.live.data.source.ApiService;
import com.xtree.live.data.source.HttpDataSource;
import com.xtree.live.data.source.LocalDataSource;
import com.xtree.live.data.source.http.HttpDataSourceImpl;
import com.xtree.live.data.source.local.LocalDataSourceImpl;

/**
 * 注入全局的数据仓库
 */
public class Injection {
    public static LiveRepository provideRechargeRepository() {
        //网络API服务
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        ApiService liveService = LiveClient.getInstance().create(ApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService, liveService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return LiveRepository.getInstance(httpDataSource, localDataSource);
    }
}
