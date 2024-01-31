package com.xtree.main.data;


import com.xtree.main.data.source.HttpDataSource;
import com.xtree.main.data.source.LocalDataSource;
import com.xtree.main.data.source.MainApiService;
import com.xtree.main.data.source.http.HttpDataSourceImpl;
import com.xtree.main.data.source.local.LocalDataSourceImpl;
import com.xtree.base.net.RetrofitClient;

/**
 * 注入全局的数据仓库
 */
public class Injection {
    public static MainRepository provideMainRepository(boolean reNew) {

        //网络API服务
        MainApiService apiService = RetrofitClient.getInstance().create(MainApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService, reNew);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return MainRepository.getInstance(httpDataSource, localDataSource, reNew);
    }
}
