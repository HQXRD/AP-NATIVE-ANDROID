package com.xtree.recharge.data;


import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.xtree.recharge.data.source.ApiService;
import com.xtree.recharge.data.source.HttpDataSource;
import com.xtree.recharge.data.source.LocalDataSource;
import com.xtree.recharge.data.source.request.ExBankInfoRequest;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.data.source.request.ExOrderCancelRequest;
import com.xtree.recharge.data.source.request.ExReceiptUploadRequest;
import com.xtree.recharge.data.source.request.ExReceiptocrRequest;
import com.xtree.recharge.data.source.request.ExRechargeOrderCheckRequest;
import com.xtree.recharge.data.source.response.ExBankInfoResponse;
import com.xtree.recharge.data.source.response.ExCreateOrderResponse;
import com.xtree.recharge.data.source.response.ExReceiptUploadResponse;
import com.xtree.recharge.data.source.response.ExReceiptocrResponse;
import com.xtree.recharge.data.source.response.ExRechargeOrderCheckResponse;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.base.BaseModel;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据（一个应用可以有多个Repositor）
 */
public class RechargeRepository extends BaseModel implements HttpDataSource, LocalDataSource {
    private volatile static RechargeRepository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private RechargeRepository(@NonNull HttpDataSource httpDataSource,
                               @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static RechargeRepository getInstance(HttpDataSource httpDataSource,
                                                 LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (RechargeRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RechargeRepository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }

    /*@Override
    public Flowable<BaseResponse<Object>> login(String username, String password) {
        return mHttpDataSource.login(username, password);
    }*/

    @Override
    public ApiService getApiService() {
        return mHttpDataSource.getApiService();
    }

    @Override
    public Flowable<BaseResponse<ExCreateOrderResponse>> createorder(ExCreateOrderRequest request) {
        return mHttpDataSource.createorder(request);
    }

    @Override
    public Flowable<BaseResponse> cancelOrderWait(ExOrderCancelRequest request) {
        return mHttpDataSource.cancelOrderWait(request);
    }

    @Override
    public Flowable<BaseResponse> cancelOrderProcess(ExOrderCancelRequest request) {
        return mHttpDataSource.cancelOrderProcess(request);
    }

    @Override
    public Flowable<BaseResponse<ExBankInfoResponse>> rechargeBankInfo(ExBankInfoRequest request) {
        return mHttpDataSource.rechargeBankInfo(request);
    }

    @Override
    public Flowable<BaseResponse<ExReceiptocrResponse>> rechargeReceiptOCR(ExReceiptocrRequest request) {
        return mHttpDataSource.rechargeReceiptOCR(request);
    }

    @Override
    public Flowable<BaseResponse<ExReceiptUploadResponse>> rechargeReceiptUpload(ExReceiptUploadRequest request) {
        return mHttpDataSource.rechargeReceiptUpload(request);
    }

    @Override
    public Flowable<ExRechargeOrderCheckResponse> rechargeOrderCheck(ExRechargeOrderCheckRequest request) {
        return mHttpDataSource.rechargeOrderCheck(request);
    }

}
