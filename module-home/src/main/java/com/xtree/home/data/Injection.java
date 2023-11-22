package com.xtree.home.data;


import com.xtree.base.service.ApiService;
import com.xtree.home.data.source.HttpDataSource;
import com.xtree.home.data.source.LocalDataSource;
import com.xtree.home.data.source.http.HttpDataSourceImpl;
import com.xtree.home.data.source.local.LocalDataSourceImpl;
import com.xtree.net.RetrofitClient;

/**
 * 注入全局的数据仓库
 */
public class Injection {
    public static HomeRepository provideHomeRepository() {
        //网络API服务
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return HomeRepository.getInstance(httpDataSource, localDataSource);
    }
}
