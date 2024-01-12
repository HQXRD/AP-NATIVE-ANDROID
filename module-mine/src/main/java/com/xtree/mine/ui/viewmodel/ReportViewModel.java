package com.xtree.mine.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.AccountChangeVo;
import com.xtree.mine.vo.ProfitLossReportVo;
import com.xtree.mine.vo.RebateReportVo;
import com.xtree.mine.vo.RechargeReportVo;
import com.xtree.mine.vo.ThirdGameTypeVo;
import com.xtree.mine.vo.ThirdTransferReportVo;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * 报表
 */
public class ReportViewModel extends BaseViewModel<MineRepository> {

    public MutableLiveData<AccountChangeVo> liveDataAccountChange = new MutableLiveData<>(); // 账变
    public MutableLiveData<List<ThirdGameTypeVo>> liveDataGameType = new MutableLiveData<>(); // 平台列表(赢亏)
    public MutableLiveData<ProfitLossReportVo> liveDataProfitLoss = new MutableLiveData<>(); // 赢亏
    public MutableLiveData<RebateReportVo> liveDataRebateReport = new MutableLiveData<>(); // 返水
    public MutableLiveData<ThirdTransferReportVo> liveDataThirdTransferVo = new MutableLiveData<>(); // 三方

    public MutableLiveData<RechargeReportVo> liveDataRechargeReport = new MutableLiveData<>(); // 充提-充值
    public MutableLiveData<RechargeReportVo> liveDataWithdrawReport = new MutableLiveData<>(); // 充提-提现
    public MutableLiveData<RechargeReportVo> liveDataFeedback = new MutableLiveData<>(); // 充提-未到账反馈

    public ReportViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    public void getAccountChangeReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getAccountChangeReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<AccountChangeVo>() {
                    @Override
                    public void onResult(AccountChangeVo vo) {
                        CfLog.d("******");
                        liveDataAccountChange.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        liveDataAccountChange.setValue(null);
                    }
                });
        addSubscribe(disposable);
    }

    public void getThirdGameType() {
        Disposable disposable = (Disposable) model.getApiService().getThirdGameType()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<ThirdGameTypeVo>>() {
                    @Override
                    public void onResult(List<ThirdGameTypeVo> list) {
                        CfLog.d("******");
                        liveDataGameType.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getProfitLoss(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getProfitLoss(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ProfitLossReportVo>() {
                    @Override
                    public void onResult(ProfitLossReportVo vo) {
                        CfLog.d("******");
                        liveDataProfitLoss.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getRebateReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getRebateReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RebateReportVo>() {
                    @Override
                    public void onResult(RebateReportVo vo) {
                        CfLog.d("******");
                        liveDataRebateReport.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getThirdTransferReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getThirdTransferReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<ThirdTransferReportVo>() {
                    @Override
                    public void onResult(ThirdTransferReportVo vo) {
                        CfLog.d("******");
                        liveDataThirdTransferVo.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getRechargeReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getRechargeReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RechargeReportVo>() {
                    @Override
                    public void onResult(RechargeReportVo vo) {
                        CfLog.d("******");
                        liveDataRechargeReport.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getWithdrawReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getWithdrawReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RechargeReportVo>() {
                    @Override
                    public void onResult(RechargeReportVo vo) {
                        CfLog.d("******");
                        liveDataWithdrawReport.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getFeedbackReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getFeedbackReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<RechargeReportVo>() {
                    @Override
                    public void onResult(RechargeReportVo vo) {
                        CfLog.d("******");
                        liveDataFeedback.setValue(vo);
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