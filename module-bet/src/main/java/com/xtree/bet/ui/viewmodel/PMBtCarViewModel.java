package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.bet.bean.request.pm.BtCarCgReq;
import com.xtree.bet.bean.request.pm.BtCarReq;
import com.xtree.bet.bean.response.pm.BtConfirmInfo;
import com.xtree.bet.bean.response.pm.CgOddLimitInfo;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionPm;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.CgOddLimitPm;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class PMBtCarViewModel extends TemplateBtCarViewModel {

    private List<BetConfirmOption> mBetConfirmOptionList;

    public PMBtCarViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void batchBetMatchMarketOfJumpLine(List<BetConfirmOption> betConfirmOptionList) {
        BtCarReq btCarReq = new BtCarReq();
        List<BtCarReq.BetMatchMarket> betMatchMarketList = new ArrayList<>();
        for (BetConfirmOption betConfirmOption : betConfirmOptionList) {
            BtCarReq.BetMatchMarket betMatchMarket = new BtCarReq.BetMatchMarket();
            betMatchMarket.setMatchInfoId(betConfirmOption.getMatch().getId());
            betMatchMarket.setMarketId(Long.valueOf(betConfirmOption.getPlayTypeId()));
            betMatchMarket.setOddsId(betConfirmOption.getOption().getId());
            betMatchMarket.setPlayId(betConfirmOption.getPlayType().getId());
            betMatchMarket.setMatchType(betConfirmOption.getOptionList().getMatchType());
            betMatchMarket.setSportId(Integer.valueOf(betConfirmOption.getMatch().getSportId()));
            betMatchMarketList.add(betMatchMarket);
        }
        btCarReq.setIdList(betMatchMarketList);

        Disposable disposable = (Disposable) model.getPMApiService().batchBetMatchMarketOfJumpLine(btCarReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<BtConfirmInfo>>() {
                    @Override
                    public void onResult(List<BtConfirmInfo> btConfirmInfoList) {
                        if(btConfirmInfoList == null || btConfirmInfoList.isEmpty()){
                            return;
                        }
                        mBetConfirmOptionList = new ArrayList<>();
                        for (BtConfirmInfo btConfirmInfo : btConfirmInfoList) {
                            mBetConfirmOptionList.add(new BetConfirmOptionPm(btConfirmInfo, ""));
                        }
                        queryMarketMaxMinBetMoney(betConfirmOptionList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    private void queryMarketMaxMinBetMoney(List<BetConfirmOption> betConfirmOptionList) {
        BtCarCgReq btCarCgReq = new BtCarCgReq();
        List<BtCarCgReq.OrderMaxBetMoney> orderMaxBetMonieList = new ArrayList<>();
        for (BetConfirmOption betConfirmOption : betConfirmOptionList) {
            BtCarCgReq.OrderMaxBetMoney orderMaxBetMoney = new BtCarCgReq.OrderMaxBetMoney();
            orderMaxBetMoney.setOpenMiltSingle(0);
            orderMaxBetMoney.setPlayId(betConfirmOption.getPlayType().getId());
            orderMaxBetMoney.setPlayOptionId(betConfirmOption.getOption().getId());
            orderMaxBetMoney.setMatchType(betConfirmOption.getOptionList().getMatchType());
            orderMaxBetMoney.setMarketId(betConfirmOption.getPlayTypeId());
            orderMaxBetMoney.setMatchId(betConfirmOption.getMatch().getId());
            orderMaxBetMoney.setOddsValue(betConfirmOption.getOption().getRealOdd() * 100000);
            orderMaxBetMonieList.add(orderMaxBetMoney);
        }
        btCarCgReq.setOrderMaxBetMoney(orderMaxBetMonieList);

        Disposable disposable = (Disposable) model.getPMApiService().queryMarketMaxMinBetMoney(btCarCgReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<CgOddLimitInfo>>() {
                    @Override
                    public void onResult(List<CgOddLimitInfo> cgOddLimitInfos) {

                        List<CgOddLimit> cgOddLimitInfoList = new ArrayList<>();
                        if (!cgOddLimitInfos.isEmpty()) {
                            for (CgOddLimitInfo cgOddLimitInfo : cgOddLimitInfos) {
                                cgOddLimitInfoList.add(new CgOddLimitPm(cgOddLimitInfo));
                            }
                        } else {
                            cgOddLimitInfoList.add(new CgOddLimitPm(null));
                        }
                        btConfirmInfoDate.postValue(mBetConfirmOptionList);
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
     * 单关投注
     */
    public void singleBet(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList) {
        /*int index = 0;
        if(betConfirmOptionList.isEmpty() || cgOddLimitList.isEmpty()){
            return;
        }
        SingleBtListReq singleBetListReq = new SingleBtListReq();
        for(BetConfirmOption betConfirmOption : betConfirmOptionList){

            BtCgReq singleBetReq = new BtCgReq();
            singleBetReq.setOddsChange(1);
            singleBetReq.setUnitStake(cgOddLimitList.get(index ++).getBtAmount());

            BtOptionReq betOptionReq = new BtOptionReq();
            betOptionReq.setOptionType(betConfirmOption.getOptionType());
            betOptionReq.setOdds(betConfirmOption.getOption().getRealOdd());
            betOptionReq.setMarketId(betConfirmOption.getOptionList().getId());
            betOptionReq.setOddsFormat(1);

            singleBetReq.addBetOptionList(betOptionReq);

            singleBetListReq.addSingleBetList(singleBetReq);
        }

        Disposable disposable = (Disposable) model.getApiService().singlePass(singleBetListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BtResultInfo>>() {
                    @Override
                    public void onResult(List<BtResultInfo> btResultRspList) {
                        List<BtResult> btResultList = new ArrayList<>();
                        for (BtResultInfo btResultInfo : btResultRspList) {
                            btResultList.add(new BtResultFb(btResultInfo));
                        }
                        btResultInfoDate.postValue(btResultList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);*/
    }

    /**
     * 串关投注
     */
    public void betMultiple(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList) {
        /*if(betConfirmOptionList.isEmpty() || cgOddLimitList.isEmpty()){
            return;
        }
        BtMultipleListReq btMultipleListReq = new BtMultipleListReq();
        for(BetConfirmOption betConfirmOption : betConfirmOptionList){

            BtOptionReq betOptionReq = new BtOptionReq();
            betOptionReq.setOptionType(betConfirmOption.getOptionType());
            betOptionReq.setOdds(betConfirmOption.getOption().getRealOdd());
            betOptionReq.setMarketId(betConfirmOption.getOptionList().getId());
            betOptionReq.setOddsFormat(1);
            btMultipleListReq.addBtOptionList(betOptionReq);
        }

        for (CgOddLimit cgOddLimit : cgOddLimitList) {
            BtCgReq btCgReq = new BtCgReq();
            btCgReq.setOddsChange(1);
            btCgReq.setUnitStake(cgOddLimit.getBtAmount());
            btCgReq.setSeriesValue(cgOddLimit.getCgCount());
            btMultipleListReq.addBtMultipleData(btCgReq);
        }

        Disposable disposable = (Disposable) model.getApiService().betMultiple(btMultipleListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BtResultInfo>>() {
                    @Override
                    public void onResult(List<BtResultInfo> btResultRspList) {
                        List<BtResult> btResultList = new ArrayList<>();
                        for (BtResultInfo btResultInfo : btResultRspList) {
                            btResultList.add(new BtResultFb(btResultInfo));
                        }
                        btResultInfoDate.postValue(btResultList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
