package com.xtree.bet.ui.viewmodel.pm;

import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_400524;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_400525;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401038;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.bet.bean.request.pm.BtCashOutBetReq;
import com.xtree.bet.bean.request.pm.BtRecordReq;
import com.xtree.bet.bean.response.pm.BtCashOutPriceInfo;
import com.xtree.bet.bean.response.pm.BtCashOutStatusInfo;
import com.xtree.bet.bean.response.pm.BtRecordRsp;
import com.xtree.bet.bean.ui.BtRecordBeanPm;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtRecordModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class PMBtRecordModel extends TemplateBtRecordModel {
    List<String> mOrderIdList = new ArrayList<>();
    public Map<String, BtRecordRsp.RecordsBean> mOrderMap = new HashMap<>();

    public PMBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void betRecord(boolean isSettled) {

        BtRecordReq btRecordReq = new BtRecordReq();
        btRecordReq.setOrderStatus(isSettled ? 1 : 0);
        btRecordReq.setTimeType(isSettled ? 1 : 4);

        Disposable disposable = (Disposable) model.getPMApiService().betRecord(btRecordReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<BtRecordRsp>() {
                    @Override
                    public void onResult(BtRecordRsp btRecordRsp) {
                        List<BtRecordTime> btRecordTimeList = new ArrayList<>();
                        Map<String, BtRecordTime> btRecordTimeMap = new HashMap<>();
                        mOrderIdList.clear();
                        mOrderMap.clear();
                        for (BtRecordRsp.RecordsBean recordsBean : btRecordRsp.records) {
                            String time = recordsBean.betTimeStr;
                            BtRecordTime btRecordTime;
                            mOrderMap.put(recordsBean.orderNo, recordsBean);
                            if (!isSettled && recordsBean.enablePreSettle) {
                                mOrderIdList.add(recordsBean.orderNo);
                            }
                            if (btRecordTimeMap.get(time) == null) {
                                btRecordTime = new BtRecordTime();
                                btRecordTime.setTime(Long.valueOf(recordsBean.betTime));
                                btRecordTimeMap.put(time, btRecordTime);
                                btRecordTimeList.add(btRecordTime);
                            } else {
                                btRecordTime = btRecordTimeMap.get(time);
                            }
                            btRecordTime.addBtResultList(new BtRecordBeanPm(recordsBean));
                        }
                        btRecordTimeDate.postValue(btRecordTimeList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (t instanceof ResponseThrowable) {
                            ResponseThrowable error = (ResponseThrowable) t;
                            if (error.code == CODE_401038) {
                                ToastUtils.showShort("请求速度太快，请稍候重试");
                            } else if (error.code == CODE_401026 || error.code == CODE_401013) {
                                getGameTokenApi();
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void cashOutPrice() {
        if(mOrderIdList.isEmpty()){
            return;
        }
        Map<String, String> map = new HashMap<>();
        String orderNo = "";
        for (String orderId : mOrderIdList) {
            orderNo += orderId + ",";
        }
        if(!TextUtils.isEmpty(orderNo)) {
            orderNo = orderNo.substring(0, orderNo.length() - 1);
        }
        map.put("orderNo", orderNo);
        Disposable disposable = (Disposable) model.getPMApiService()
                .getCashoutMaxAmountList(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<BtCashOutPriceInfo>>() {
                    @Override
                    public void onResult(List<BtCashOutPriceInfo> btCashOutPriceInfoList) {
                        for (BtCashOutPriceInfo btCashOutPriceInfo : btCashOutPriceInfoList) {
                            BtRecordRsp.RecordsBean recordsBean = mOrderMap.get(btCashOutPriceInfo.orderNo);
                            recordsBean.pr = btCashOutPriceInfo;
                        }
                        btRecordTimeDate.postValue(btRecordTimeDate.getValue());
                        btUpdateCashOutPrice.call();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (t instanceof ResponseThrowable) {
                            ResponseThrowable error = (ResponseThrowable) t;
                            if (error.code == CODE_401038) {
                                ToastUtils.showShort("请求速度太快，请稍候重试");
                            } else if (error.code == CODE_401026 || error.code == CODE_401013) {
                                getGameTokenApi();
                            } else if (error.code == CODE_400525) {

                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void cashOutPricebBet(String orderId, double cashOutStake, double unitCashOutPayoutStake, boolean acceptOddsChange, boolean parlay) {
        BtCashOutBetReq btCashOutBetReq = new BtCashOutBetReq();
        btCashOutBetReq.setOrderNo(orderId);
        btCashOutBetReq.setSettleAmount(String.valueOf(cashOutStake));
        btCashOutBetReq.setFrontSettleAmount(unitCashOutPayoutStake);
        Disposable disposable = (Disposable) model.getPMApiService()
                .orderPreSettle(btCashOutBetReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<BtCashOutPriceInfo>>() {
                    @Override
                    public void onResult(List<BtCashOutPriceInfo> btCashOutPriceInfoList) {

                    }

                    @Override
                    public void onError(Throwable t) {

                        if (t instanceof ResponseThrowable) {

                            ResponseThrowable error = (ResponseThrowable) t;
                            if (error.code == CODE_400524) {
                                btUpdateCashOutBet.postValue(orderId);
                            } else if (error.code == CODE_401026 || error.code == CODE_401013) {
                                getGameTokenApi();
                            } else if (error.code == CODE_401038) {
                                ToastUtils.showShort("请求速度太快，请稍候重试");
                            } else {
                                super.onError(t);
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void getCashOutsByIds(String id) {
        Disposable disposable = (Disposable) model.getPMApiService()
                .queryOrderPreSettleConfirm()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<BtCashOutStatusInfo>>() {
                    @Override
                    public void onResult(List<BtCashOutStatusInfo> btCashOutStatusInfoList) {
                        if(!btCashOutStatusInfoList.isEmpty()) {
                            BtCashOutStatusInfo btCashOutStatusInfo = null;
                            for (BtCashOutStatusInfo info : btCashOutStatusInfoList) {
                                if(TextUtils.equals(info.orderNo, id)){
                                    btCashOutStatusInfo = info;
                                    break;
                                }
                            }
                            if(btCashOutStatusInfo != null) {
                                if (btCashOutStatusInfo.preSettleOrderStatus == 2) {
                                    btUpdateCashOutStatus.postValue(false);
                                } else if (btCashOutStatusInfo.preSettleOrderStatus == 1) {
                                    btUpdateCashOutStatus.postValue(true);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ResponseThrowable) {
                            super.onError(t);
                            if (t instanceof ResponseThrowable) {
                                ResponseThrowable error = (ResponseThrowable) t;
                                if (error.code == CODE_401038) {
                                    ToastUtils.showShort("请求速度太快，请稍候重试");
                                } else if (error.code == CODE_401026 || error.code == CODE_401013) {
                                    getGameTokenApi();
                                }
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

}
