package com.xtree.recharge.data.source;


/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {

    //Flowable<BaseResponse<Object>> login(String username, String password);

    ApiService getApiService();
}
