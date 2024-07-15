package com.xtree.bet.ui.viewmodel;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.utils.SystemUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.BalanceVo;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.bet.bean.request.UploadExcetionReq;
import com.xtree.bet.data.BetRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * Created by marquis
 */

public class BaseBtViewModel extends BaseViewModel<BetRepository> {
    public SingleLiveData<String> userBalanceData = new SingleLiveData<>();
    public SingleLiveData<Void> tokenInvalidEvent = new SingleLiveData<>();
    public SingleLiveData<Map> liveDataPlayUrl = new SingleLiveData<>();
    public BaseBtViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
    }

    public void getUserBalance() {

        Disposable disposable = (Disposable) model.getBaseApiService().getBalance()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BalanceVo>() {
                    @Override
                    public void onResult(BalanceVo vo) {
                        CfLog.d(vo.toString());
                        SPUtils.getInstance().put(SPKeyGlobal.WLT_CENTRAL_BLC, vo.balance);
                        userBalanceData.postValue(NumberUtils.formatDown(Double.valueOf(vo.balance), 2));
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getGameTokenApi() {
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            getFBGameTokenApi();
        } else {
            getPMGameTokenApi();
        }
    }

    public void getFBGameTokenApi() {
        Flowable<BaseResponse<FBService>> flowable;
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
            flowable = model.getBaseApiService().getFBXCGameTokenApi();
        } else {
            flowable = model.getBaseApiService().getFBGameTokenApi();
        }
        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setDefaultFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.addFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setFbxcDomainUrl(fbService.getDomains());
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setDefaultFbDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.addFbDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setFbDomainUrl(fbService.getDomains());
                        }

                        tokenInvalidEvent.call();
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getPMGameTokenApi() {
        Flowable<BaseResponse<PMService>> flowable;
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            flowable = model.getBaseApiService().getPMXCGameTokenApi();
        } else {
            flowable = model.getBaseApiService().getPMGameTokenApi();
        }
        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PMService>() {
                    @Override
                    public void onResult(PMService pmService) {

                        if (TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_TOKEN, pmService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_API_SERVICE_URL, pmService.getApiDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_IMG_SERVICE_URL, pmService.getImgDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PMXC_USER_ID, pmService.getUserId());
                            BtDomainUtil.setDefaultPmxcDomainUrl(pmService.getApiDomain());
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.PM_TOKEN, pmService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, pmService.getApiDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PM_IMG_SERVICE_URL, pmService.getImgDomain());
                            SPUtils.getInstance().put(SPKeyGlobal.PM_USER_ID, pmService.getUserId());
                            BtDomainUtil.setDefaultPmDomainUrl(pmService.getApiDomain());
                        }

                        tokenInvalidEvent.call();
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void uploadException(UploadExcetionReq uploadExcetionReq) {
        Map<String, String> map = new HashMap<>();
        map.put("log_tag", uploadExcetionReq.getLogTag());
        map.put("api_url", uploadExcetionReq.getApiUrl());
        map.put("device_no", "android-app-" + TagUtils.getDeviceId(Utils.getContext()));
        //map.put("device_no2", "log_tag");
        map.put("log_type", uploadExcetionReq.getLogType());
        map.put("device_type", "9");
        map.put("device_detail", SystemUtil.getDeviceBrand() + " " + SystemUtil.getDeviceModel() + " " + "Android " + SystemUtil.getSystemVersion());
        map.put("msg", uploadExcetionReq.getMsg());
        Disposable disposable = (Disposable) model.getBaseApiService().uploadExcetion(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<String>() {
                    @Override
                    public void onResult(String result) {
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
        addSubscribe(disposable);
    }

    public void getPlayUrl(String gameAlias) {

        int autoThrad = SPUtils.getInstance().getInt(SPKeyGlobal.USER_AUTO_THRAD_STATUS);

        HashMap<String, String> map = new HashMap();
        map.put("autoThrad", autoThrad + "");
        map.put("h5judge", "1");
        map.put("id", "");

        Disposable disposable = (Disposable) model.getBaseApiService().getPlayUrl(gameAlias, map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, Object>>() {
                    @Override
                    public void onResult(Map<String, Object> vo) {
                        // "url": "https://user-h5-bw3.d91a21f.com?token=7c9c***039a"
                        // "url": { "launch_url": "https://cdn-ali.***.com/h5V01/h5.html?sn=dy12&xxx" }
                        CfLog.i("111111111"+vo.toString());
                        if (!vo.containsKey("url")) {
                            return;
                        }
                        CfLog.i("111111112"+vo.toString());
                        liveDataPlayUrl.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }
}
