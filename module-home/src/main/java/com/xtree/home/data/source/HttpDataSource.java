package com.xtree.home.data.source;


import com.xtree.home.data.ApiService;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    ApiService getApiService();

}
