package com.xtree.bet.ui.viewmodel;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FBXC;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.vo.FBService;
import com.xtree.base.vo.PMService;
import com.xtree.bet.bean.response.fb.BalanceInfo;
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

/**
 * Created by marquis
 */

public class BaseBtViewModel extends BaseViewModel<BetRepository> {
    public SingleLiveData<String> userBalanceData = new SingleLiveData<>();
    public SingleLiveData<Void> tokenInvalidEvent = new SingleLiveData<>();
    public BaseBtViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
    }

    public void getUserBalance(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            getUserBalanceFb();
        }else {
            getUserBalancePm();
        }
    }

    private void getUserBalanceFb() {
        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        Disposable disposable = (Disposable) model.getApiService().getUserBanlace(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<BalanceInfo>() {
                    @Override
                    public void onResult(BalanceInfo balanceInfo) {
                        userBalanceData.postValue(NumberUtils.format(Double.valueOf(balanceInfo.bl), 2));
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                        getUserBalanceFb();
                    }
                });
        addSubscribe(disposable);
    }

    private void getUserBalancePm() {
        Map<String, String> map = new HashMap<>();
        String token = SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN);
        map.put("teken", token);
        Disposable disposable = (Disposable) model.getPMApiService().getUserBanlace(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<com.xtree.bet.bean.response.pm.BalanceInfo>() {
                    @Override
                    public void onResult(com.xtree.bet.bean.response.pm.BalanceInfo balanceInfo) {
                        userBalanceData.postValue(NumberUtils.format(balanceInfo.amount, 2));
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                        getUserBalancePm();
                    }
                });
        addSubscribe(disposable);
    }

    public void getGameTokenApi(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            getFBGameTokenApi();
        }else {
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
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                        }
                        DomainUtil.setFbDomainUrl(fbService.getDomains());
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
        Disposable disposable = (Disposable) model.getBaseApiService().getPMGameTokenApi()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PMService>() {
                    @Override
                    public void onResult(PMService pmService) {
                        SPUtils.getInstance().put(SPKeyGlobal.PM_TOKEN, pmService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, pmService.getApiDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_IMG_SERVICE_URL, pmService.getImgDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_USER_ID, pmService.getUserId());
                        tokenInvalidEvent.call();
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
