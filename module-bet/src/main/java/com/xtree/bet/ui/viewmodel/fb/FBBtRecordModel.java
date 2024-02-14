package com.xtree.bet.ui.viewmodel.fb;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.request.fb.BtRecordReq;
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
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by marquis
 */

public class FBBtRecordModel extends TemplateBtRecordModel {

    public FBBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void betRecord(boolean isSettled){

        BtRecordReq btRecordReq = new BtRecordReq();
        btRecordReq.setSettled(isSettled);

        Disposable disposable = (Disposable) model.getApiService().betRecord(btRecordReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<BtRecordRsp>() {
                    @Override
                    public void onResult(BtRecordRsp btRecordRsp) {
                        List<BtRecordTime> btRecordTimeList = new ArrayList<>();
                        Map<String, BtRecordTime> btRecordTimeMap = new HashMap<>();
                        for (BtResultInfo btResultInfo : btRecordRsp.records){
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
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
