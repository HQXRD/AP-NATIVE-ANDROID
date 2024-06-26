package com.xtree.bet.ui.viewmodel;

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

public abstract class TemplateBtRecordModel extends BaseBtViewModel implements BtRecordModel{
    public List<String> mOrderIdList = new ArrayList<>();
    public boolean mIsSettled;
    public SingleLiveData<List<BtRecordTime>> btRecordTimeDate = new SingleLiveData<>();
    public SingleLiveData<List<Void>> btUpdateCashOutPrice = new SingleLiveData<>();
    public SingleLiveData<String> btUpdateCashOutBet = new SingleLiveData<>();
    public SingleLiveData<Boolean> btUpdateCashOutStatus = new SingleLiveData<>();

    public TemplateBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

}
