package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.BtConfirmInfo;
import com.xtree.bet.bean.BtConfirmOptionInfo;
import com.xtree.bet.bean.CgOddLimitInfo;
import com.xtree.bet.bean.request.BtCarReq;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.CgOddLimitFb;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.manager.BtCarManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class BtCarViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;
    public SingleLiveData<List<BetConfirmOption>> btConfirmInfoDate = new SingleLiveData<>();
    public SingleLiveData<List<CgOddLimit>> cgOddLimitDate = new SingleLiveData<>();

    public BtCarViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }


    public void addSubscription() {

    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void batchBetMatchMarketOfJumpLine(List<BetConfirmOption> betConfirmOptionList){
        BtCarReq btCarReq = new BtCarReq();
        btCarReq.setLanguageType("CMN");
        btCarReq.setCurrencyId(1);
        btCarReq.setSelectSeries(true);
        List<BtCarReq.BetMatchMarket> betMatchMarketList = new ArrayList<>();
        for (BetConfirmOption betConfirmOption : betConfirmOptionList) {
            BtCarReq.BetMatchMarket betMatchMarket = new BtCarReq.BetMatchMarket();
            betMatchMarket.setMarketId(betConfirmOption.getPlayTypeId());
            betMatchMarket.setType(betConfirmOption.getOptionType());
            betMatchMarket.setMatchId(betConfirmOption.getMatch().getId());
            betMatchMarketList.add(betMatchMarket);
        }
        btCarReq.setBetMatchMarketList(betMatchMarketList);

        Disposable disposable = (Disposable) model.getApiService().batchBetMatchMarketOfJumpLine(btCarReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BtConfirmInfo>() {
                    @Override
                    public void onResult(BtConfirmInfo btConfirmInfo) {
                        List<BetConfirmOption> betConfirmOptionList = new ArrayList<>();
                        for (BtConfirmOptionInfo btConfirmOptionInfo : btConfirmInfo.bms) {
                            betConfirmOptionList.add(new BetConfirmOptionFb(btConfirmOptionInfo, ""));
                        }
                        btConfirmInfoDate.postValue(betConfirmOptionList);

                        List<CgOddLimit> cgOddLimitInfoList = new ArrayList<>();
                        if(!btConfirmInfo.sos.isEmpty()) {
                            int index = 0;
                            for (CgOddLimitInfo cgOddLimitInfo : btConfirmInfo.sos) {
                                cgOddLimitInfoList.add(new CgOddLimitFb(cgOddLimitInfo, btConfirmInfo.bms.get(index++), btConfirmInfo.bms.size()));
                            }
                        }else{
                            cgOddLimitInfoList.add(new CgOddLimitFb(null, btConfirmInfo.bms.get(0), 0));
                        }
                        cgOddLimitDate.postValue(cgOddLimitInfoList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 打开今日
     */
    public void gotoToday(){
        BtCarManager.clearBtCar();
        BtCarManager.setIsCg(false);
        RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPEN_TODAY));
    }

    /**
     * 打开串关
     * @param betConfirmOption
     */
    public void gotoCg(BetConfirmOption betConfirmOption){
        BtCarManager.addBtCar(betConfirmOption);
        BtCarManager.setIsCg(true);
        RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPEN_CG));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
