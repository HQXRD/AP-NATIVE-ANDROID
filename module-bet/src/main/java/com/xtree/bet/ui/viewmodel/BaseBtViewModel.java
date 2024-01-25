package com.xtree.bet.ui.viewmodel;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FB;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.utils.NumberUtils;
import com.xtree.bet.bean.response.fb.BalanceInfo;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.data.BetRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class BaseBtViewModel extends BaseViewModel<BetRepository> {
    public SingleLiveData<String> userBalanceData = new SingleLiveData<>();
    public BaseBtViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
    }

    public void getUserBalance(){
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_FB)) {
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
                        super.onError(t);
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
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
