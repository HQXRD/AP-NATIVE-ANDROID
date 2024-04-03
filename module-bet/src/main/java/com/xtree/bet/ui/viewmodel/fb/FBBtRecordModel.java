package com.xtree.bet.ui.viewmodel.fb;

import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.request.fb.BtCashOutPriceReq;
import com.xtree.bet.bean.request.fb.BtRecordReq;
import com.xtree.bet.bean.response.fb.BtCashOutPriceInfo;
import com.xtree.bet.bean.response.fb.BtCashOutPriceOrderInfo;
import com.xtree.bet.bean.response.fb.BtRecordRsp;
import com.xtree.bet.bean.response.fb.BtResultInfo;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.bean.ui.BtResultFb;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtRecordModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by marquis
 */

public class FBBtRecordModel extends TemplateBtRecordModel {
    List<String> mOrderIdList = new ArrayList<>();
    public Map<String, BtResultInfo> mOrderMap = new HashMap<>();

    public FBBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void betRecord(boolean isSettled){

        BtRecordReq btRecordReq = new BtRecordReq();
        btRecordReq.setSettled(isSettled);
        if(isSettled){
            btRecordReq.setStartTime(TimeUtils.longFormatDate(System.currentTimeMillis(), TimeUtils.FORMAT_YY_MM_DD));
        }

        Disposable disposable = (Disposable) model.getApiService().
                betRecord(btRecordReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<BtRecordRsp>() {
                    @Override
                    public void onResult(BtRecordRsp btRecordRsp) {
                        List<BtRecordTime> btRecordTimeList = new ArrayList<>();
                        Map<String, BtRecordTime> btRecordTimeMap = new HashMap<>();
                        mOrderIdList.clear();
                        mOrderMap.clear();
                        for (BtResultInfo btResultInfo : btRecordRsp.records){
                            mOrderMap.put(btResultInfo.id, btResultInfo);
                            if(!isSettled) {
                                mOrderIdList.add(btResultInfo.id);
                            }
                            String time = TimeUtils.longFormatString(btResultInfo.cte, TimeUtils.FORMAT_YY_MM_DD);
                            BtRecordTime btRecordTime;
                            if(btRecordTimeMap.get(time) == null){
                                btRecordTime = new BtRecordTime();
                                btRecordTime.setTime(btResultInfo.cte);
                                btRecordTimeMap.put(time, btRecordTime);
                                btRecordTimeList.add(btRecordTime);
                            }else{
                                btRecordTime = btRecordTimeMap.get(time);
                            }
                            btRecordTime.addBtResultList(new BtResultFb(btResultInfo));
                        }
                        btRecordTimeDate.postValue(btRecordTimeList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ResponseThrowable) {
                            ResponseThrowable error = (ResponseThrowable) t;
                            if (error.code == CODE_14010) {
                                getGameTokenApi();
                            }else {
                                super.onError(t);
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 批量获取订单提前结算报价
     */
    public void cashOutPrice(){

        BtCashOutPriceReq btCashOutPriceReq = new BtCashOutPriceReq();
        btCashOutPriceReq.setOrderIds(mOrderIdList);
        Disposable disposable = (Disposable) model.getApiService()
                .cashOutPrice(btCashOutPriceReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<BtCashOutPriceInfo>() {
                    @Override
                    public void onResult(BtCashOutPriceInfo btCashOutPriceInfo) {
                        for (BtCashOutPriceOrderInfo btCashOutPriceOrderInfo : btCashOutPriceInfo.pr){
                            BtResultInfo btResultInfo = mOrderMap.get(btCashOutPriceOrderInfo.oid);
                            btResultInfo.pr = btCashOutPriceOrderInfo;
                        }
                        btRecordTimeDate.postValue(btRecordTimeDate.getValue());
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ResponseThrowable) {
                            ResponseThrowable error = (ResponseThrowable) t;
                            if (error.code == CODE_14010) {
                                getGameTokenApi();
                            }else {
                                super.onError(t);
                            }
                        }
                    }
                });
        addSubscribe(disposable);
    }

}
