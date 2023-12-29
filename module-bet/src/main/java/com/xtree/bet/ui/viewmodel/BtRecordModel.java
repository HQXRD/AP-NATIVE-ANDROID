package com.xtree.bet.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.request.BtCarReq;
import com.xtree.bet.bean.request.BtCgReq;
import com.xtree.bet.bean.request.BtMultipleListReq;
import com.xtree.bet.bean.request.BtOptionReq;
import com.xtree.bet.bean.request.BtRecordReq;
import com.xtree.bet.bean.request.SingleBtListReq;
import com.xtree.bet.bean.response.BtConfirmInfo;
import com.xtree.bet.bean.response.BtConfirmOptionInfo;
import com.xtree.bet.bean.response.BtRecordRsp;
import com.xtree.bet.bean.response.BtResultInfo;
import com.xtree.bet.bean.response.CgOddLimitInfo;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.BtResultFb;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.CgOddLimitFb;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.manager.BtCarManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class BtRecordModel extends BaseViewModel<BetRepository> {
    public SingleLiveData<List<BtRecordTime>> btRecordTimeDate = new SingleLiveData<>();

    public BtRecordModel(@NonNull Application application, BetRepository repository) {
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
                .subscribeWith(new HttpCallBack<BtRecordRsp>() {
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
