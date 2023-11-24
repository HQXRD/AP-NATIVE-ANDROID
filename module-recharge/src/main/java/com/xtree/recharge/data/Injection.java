package com.xtree.recharge.data;


import com.xtree.net.RetrofitClient;
import com.xtree.recharge.data.source.ApiService;
import com.xtree.recharge.data.source.HttpDataSource;
import com.xtree.recharge.data.source.LocalDataSource;
import com.xtree.recharge.data.source.http.HttpDataSourceImpl;
import com.xtree.recharge.data.source.local.LocalDataSourceImpl;

/**
 * 注入全局的数据仓库
 */
public class Injection {
    public static RechargeRepository provideRechargeRepository() {
        //网络API服务
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return RechargeRepository.getInstance(httpDataSource, localDataSource);
    }
}
