package com.xtree.live.data.source;


import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.response.LiveTokenResponse;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by goldze on 2019/3/26.
 */
public interface HttpDataSource {

    ApiService getApiService();

    void setLive(LiveTokenResponse liveData);

    Flowable<BaseResponse<LiveTokenResponse>> getLiveToken(LiveTokenRequest request);
}
