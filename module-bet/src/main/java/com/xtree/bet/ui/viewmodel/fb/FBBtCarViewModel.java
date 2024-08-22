package com.xtree.bet.ui.viewmodel.fb;

import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.FBHttpCallBack;
import com.xtree.bet.bean.request.fb.BtMultipleListReq;
import com.xtree.bet.bean.request.fb.BtOptionReq;
import com.xtree.bet.bean.request.fb.SingleBtListReq;
import com.xtree.bet.bean.request.fb.BtCgReq;
import com.xtree.bet.bean.response.fb.BtConfirmInfo;
import com.xtree.bet.bean.response.fb.BtConfirmOptionInfo;
import com.xtree.bet.bean.response.fb.BtResultInfo;
import com.xtree.bet.bean.response.fb.CgOddLimitInfo;
import com.xtree.bet.bean.request.fb.BtCarReq;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.BtResultFb;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.CgOddLimitFb;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtCarViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis
 */

public class FBBtCarViewModel extends TemplateBtCarViewModel {

    public FBBtCarViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void batchBetMatchMarketOfJumpLine(List<BetConfirmOption> betConfirmOptionList) {
        BtCarReq btCarReq = new BtCarReq();
        btCarReq.setLanguageType("CMN");
        btCarReq.setCurrencyId(1);
        btCarReq.setSelectSeries(true);
        List<BtCarReq.BetMatchMarket> betMatchMarketList = new ArrayList<>();
        for (BetConfirmOption betConfirmOption : betConfirmOptionList) {
            BtCarReq.BetMatchMarket betMatchMarket = new BtCarReq.BetMatchMarket();
            betMatchMarket.setMarketId(Long.valueOf(betConfirmOption.getPlayTypeId()));
            betMatchMarket.setType(betConfirmOption.getOptionType());
            betMatchMarket.setOddsType(1);
            betMatchMarket.setMatchId(betConfirmOption.getMatch().getId());
            betMatchMarketList.add(betMatchMarket);
        }
        btCarReq.setBetMatchMarketList(betMatchMarketList);

        Disposable disposable = (Disposable) model.getApiService().batchBetMatchMarketOfJumpLine(btCarReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<BtConfirmInfo>() {
                    @Override
                    public void onResult(BtConfirmInfo btConfirmInfo) {
                        List<BetConfirmOption> betConfirmOptionList = new ArrayList<>();
                        for (BtConfirmOptionInfo btConfirmOptionInfo : btConfirmInfo.bms) {
                            betConfirmOptionList.add(new BetConfirmOptionFb(btConfirmOptionInfo, ""));
                        }
                        btConfirmInfoDate.postValue(betConfirmOptionList);

                        List<CgOddLimit> cgOddLimitInfoList = new ArrayList<>();
                        if (!btConfirmInfo.sos.isEmpty()) {
                            int index = 0;
                            for (CgOddLimitInfo cgOddLimitInfo : btConfirmInfo.sos) {
                                cgOddLimitInfoList.add(new CgOddLimitFb(cgOddLimitInfo, btConfirmInfo.bms.get(index++), btConfirmInfo.bms.size()));
                            }
                        } else {
                            cgOddLimitInfoList.add(new CgOddLimitFb(null, btConfirmInfo.bms.get(0), 0));
                        }
                        cgOddLimitDate.postValue(cgOddLimitInfoList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ResponseThrowable) {
                            if (((ResponseThrowable) t).code == CODE_14010) {
                                batchBetMatchMarketOfJumpLine(betConfirmOptionList);
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 单关投注
     */
    public void singleBet(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, int acceptOdds) {
        //int index = 0;
        if (betConfirmOptionList.isEmpty() || cgOddLimitList.isEmpty()) {
            return;
        }
        SingleBtListReq singleBetListReq = new SingleBtListReq();
        BetConfirmOption betConfirmOption = betConfirmOptionList.get(0);

        BtCgReq singleBetReq = new BtCgReq();
        singleBetReq.setOddsChange(acceptOdds);
        singleBetReq.setUnitStake(cgOddLimitList.get(0).getBtAmount());

        BtOptionReq betOptionReq = new BtOptionReq();
        betOptionReq.setOptionType(betConfirmOption.getOptionType());
        betOptionReq.setOdds(betConfirmOption.getOption().getRealOdd());
        betOptionReq.setMarketId(betConfirmOption.getOptionList().getId());
        betOptionReq.setOddsFormat(SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE, 1));

        singleBetReq.addBetOptionList(betOptionReq);
        singleBetListReq.addSingleBetList(singleBetReq);

        if(singleBetReq.getUnitStake() <= 0){
            noBetAmountDate.call();
            return;
        }

        Disposable disposable = (Disposable) model.getApiService().singlePass(singleBetListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<List<BtResultInfo>>() {
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
        addSubscribe(disposable);
    }

    /**
     * 串关投注
     */
    public void betMultiple(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, int acceptOdds) {
        if (betConfirmOptionList.isEmpty() || cgOddLimitList.isEmpty()) {
            return;
        }
        BtMultipleListReq btMultipleListReq = new BtMultipleListReq();
        for (BetConfirmOption betConfirmOption : betConfirmOptionList) {

            BtOptionReq betOptionReq = new BtOptionReq();
            betOptionReq.setOptionType(betConfirmOption.getOptionType());
            betOptionReq.setOdds(betConfirmOption.getOption().getRealOdd());

            betOptionReq.setMarketId(Long.valueOf(betConfirmOption.getPlayTypeId()));
            betOptionReq.setOddsFormat(SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE, 1));
            btMultipleListReq.addBtOptionList(betOptionReq);
        }

        for (CgOddLimit cgOddLimit : cgOddLimitList) {
            if (cgOddLimit.getBtAmount() > 0) {
                BtCgReq btCgReq = new BtCgReq();
                btCgReq.setOddsChange(acceptOdds);
                btCgReq.setUnitStake(cgOddLimit.getBtAmount());
                btCgReq.setSeriesValue(cgOddLimit.getCgCount());
                btMultipleListReq.addBtMultipleData(btCgReq);
            }
        }

        if (btMultipleListReq.getBetMultipleData().isEmpty()) {
            noBetAmountDate.call();
            return;
        }

        Disposable disposable = (Disposable) model.getApiService().betMultiple(btMultipleListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<List<BtResultInfo>>() {
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
        addSubscribe(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
