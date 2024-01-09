package com.xtree.bet.ui.viewmodel.pm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.request.pm.BtRecordReq;
import com.xtree.bet.bean.response.pm.BtRecordRsp;
import com.xtree.bet.bean.ui.BtRecordBeanPm;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.bean.ui.BtResultPm;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtRecordModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

public class PMBtRecordModel extends TemplateBtRecordModel {
    public PMBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void betRecord(boolean isSettled){

        BtRecordReq btRecordReq = new BtRecordReq();
        btRecordReq.setOrderStatus(isSettled ? 1 : 0);

        Disposable disposable = (Disposable) model.getPMApiService().betRecord(btRecordReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<BtRecordRsp>() {
                    @Override
                    public void onResult(BtRecordRsp btRecordRsp) {
                        List<BtRecordTime> btRecordTimeList = new ArrayList<>();
                        Map<String, BtRecordTime> btRecordTimeMap = new HashMap<>();
                        for (BtRecordRsp.RecordsBean recordsBean : btRecordRsp.records){
                            String time = recordsBean.betTimeStr;
                            BtRecordTime btRecordTime;
                            if(btRecordTimeMap.get(time) == null){
                                btRecordTime = new BtRecordTime();
                                btRecordTime.setTime(Long.valueOf(recordsBean.betTime));
                                btRecordTimeMap.put(time, btRecordTime);
                                btRecordTimeList.add(btRecordTime);
                            }else{
                                btRecordTime = btRecordTimeMap.get(time);
                            }
                            btRecordTime.addBtResultList(new BtRecordBeanPm(recordsBean));
                        }
                        btRecordTimeDate.postValue(btRecordTimeList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
