package com.xtree.mine.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.vo.AccountChangeVo;
import com.xtree.mine.vo.BtDetailVo;
import com.xtree.mine.vo.BtPlatformVo;
import com.xtree.mine.vo.BtReportVo;
import com.xtree.mine.vo.GameChangeVo;
import com.xtree.mine.vo.LotteryDetailVo;
import com.xtree.mine.vo.LotteryOrderVo;
import com.xtree.mine.vo.LotteryReportVo;
import com.xtree.mine.vo.ProfitLossReportVo;
import com.xtree.mine.vo.RebateReportVo;
import com.xtree.mine.vo.RechargeReportVo;
import com.xtree.mine.vo.ThirdGameTypeVo;
import com.xtree.mine.vo.ThirdTransferReportVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 报表
 */
public class ReportViewModel extends BaseViewModel<MineRepository> {

    public MutableLiveData<AccountChangeVo> liveDataAccountChange = new MutableLiveData<>(); // 账变
    public MutableLiveData<List<ThirdGameTypeVo>> liveDataGameType = new MutableLiveData<>(); // 平台列表(盈亏)
    public MutableLiveData<ProfitLossReportVo> liveDataProfitLoss = new MutableLiveData<>(); // 盈亏
    public MutableLiveData<RebateReportVo> liveDataRebateReport = new MutableLiveData<>(); // 返水
    public MutableLiveData<ThirdTransferReportVo> liveDataThirdTransferVo = new MutableLiveData<>(); // 三方

    public MutableLiveData<RechargeReportVo> liveDataRechargeReport = new MutableLiveData<>(); // 充提-充值
    public MutableLiveData<RechargeReportVo> liveDataWithdrawReport = new MutableLiveData<>(); // 充提-提现
    public MutableLiveData<RechargeReportVo> liveDataFeedback = new MutableLiveData<>(); // 充提-未到账反馈
    public MutableLiveData<List<BtPlatformVo>> liveDataBtPlatform = new MutableLiveData<>(); // 投注记录-平台列表
    public MutableLiveData<BtReportVo> liveDataBtReport = new MutableLiveData<>(); // 投注记录-列表
    public MutableLiveData<BtDetailVo> liveDataBtDetail = new MutableLiveData<>(); // 投注记录-详情
    public MutableLiveData<LotteryReportVo> liveDataCpReport = new MutableLiveData<>(); // 投注记录-列表(彩票)
    public MutableLiveData<LotteryDetailVo> liveDataBtCpDetail = new MutableLiveData<>(); // 投注记录-详情(彩票)
    public MutableLiveData<GameChangeVo> liveDataGameChange = new MutableLiveData<>(); // 游戏账变记录

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
                        if (vo.msg_type == 2) {
                            ToastUtils.showLong(vo.message); // 无权限，请联系您的上级
                        }
                        if (vo.aProfitLoss == null) {
                            vo.aProfitLoss = new ArrayList<>();
                        }
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

    public void getBtPlatformType() {
        Disposable disposable = (Disposable) model.getApiService().getBtPlatformType()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<BtPlatformVo>>() {
                    @Override
                    public void onResult(List<BtPlatformVo> list) {
                        CfLog.d("******");
                        liveDataBtPlatform.setValue(list);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getBtReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getBtReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BtReportVo>() {
                    @Override
                    public void onResult(BtReportVo vo) {
                        CfLog.d("******");
                        liveDataBtReport.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getBtOrderDetail(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getBtOrderDetail(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<BtDetailVo>() {
                    @Override
                    public void onResult(BtDetailVo vo) {
                        CfLog.d("******");
                        liveDataBtDetail.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getCpReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getCpReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LotteryReportVo>() {
                    @Override
                    public void onResult(LotteryReportVo vo) {
                        CfLog.d("******");
                        liveDataCpReport.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getBtCpOrderDetail(String id) {
        Disposable disposable = (Disposable) model.getApiService().getBtCpOrderDetail(id)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<LotteryDetailVo>() {
                    @Override
                    public void onResult(LotteryDetailVo vo) {
                        CfLog.d("******");
                        liveDataBtCpDetail.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    public void getGameChangeReport(HashMap map) {
        Disposable disposable = (Disposable) model.getApiService().getGameChangeReport(map)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<GameChangeVo>() {
                    @Override
                    public void onResult(GameChangeVo vo) {
                        CfLog.d("******");
                        liveDataGameChange.setValue(vo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        CfLog.e("error, " + t.toString());
                        super.onError(t);
                        liveDataGameChange.setValue(null);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取彩票的状态
     */
    public String getLotteryStatus(Context ctx, LotteryOrderVo vo) {
        if (vo.iscancel.equalsIgnoreCase("1")) {
            return ctx.getString(R.string.txt_cancel_by_self);
        }
        if (vo.iscancel.equals("2")) {
            return ctx.getString(R.string.txt_cancel_by_platform);
        }
        if (vo.iscancel.equals("3")) {
            return ctx.getString(R.string.txt_cancel_by_error);
        }
        if (!vo.iscancel.equals("0")) {
            return ctx.getString(R.string.txt_cancel_no_status);
        }
        if (vo.isgetprize.equals("0")) {
            return ctx.getString(R.string.txt_prizes_not_open);
        }
        if (vo.isgetprize.equals("2")) {
            return ctx.getString(R.string.txt_prizes_not_win);
        }
        if (vo.isgetprize.equals("1")) {
            if (vo.prizestatus == "0") {
                return ctx.getString(R.string.txt_prizes_not_distributed);
            }
            return ctx.getString(R.string.txt_prizes_distributed);
        }
        return "---";
    }

}