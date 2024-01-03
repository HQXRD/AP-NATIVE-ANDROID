package com.xtree.bet.data.source;


import com.xtree.bet.data.FBApiService;
import com.xtree.bet.data.PMApiService;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {
    FBApiService getApiService();
    PMApiService getPMApiService();
}
