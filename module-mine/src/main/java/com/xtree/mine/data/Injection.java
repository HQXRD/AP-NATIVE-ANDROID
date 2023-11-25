package com.xtree.mine.data;



import com.xtree.mine.data.source.HttpDataSource;
import com.xtree.mine.data.source.LocalDataSource;
import com.xtree.mine.data.source.http.HttpDataSourceImpl;
import com.xtree.mine.data.source.http.service.DemoApiService;
import com.xtree.mine.data.source.local.LocalDataSourceImpl;
import com.xtree.base.net.RetrofitClient;

/**
 * 注入全局的数据仓库
 */
public class Injection {
    public static MineRepository provideHomeRepository() {
        //网络API服务
        DemoApiService apiService = RetrofitClient.getInstance().create(DemoApiService.class);
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return MineRepository.getInstance(httpDataSource, localDataSource);
    }
}
