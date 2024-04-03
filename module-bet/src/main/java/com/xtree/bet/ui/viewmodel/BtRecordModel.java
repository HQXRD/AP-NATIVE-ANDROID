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

public interface BtRecordModel {
    /**
     * 查询投注记录
     */
    void betRecord(boolean isSettled);

    /**
     * 批量获取订单提前结算报价
     */
    void cashOutPrice();

    /**
     * 提前结算下注
     * @param orderId 订单ID
     * @param cashOutStake 提前结算本金
     * @param unitCashOutPayoutStake 提前结算报价接口返回的"单位提前结算价格"
     * @param acceptOddsChange 是否接受下注时真实价格低于下注价格(false:不接受价格变低 true:接受价格变低下注)
     * @param parlay 是否串关
     */
    void cashOutPricebBet(String orderId, double cashOutStake, double unitCashOutPayoutStake, boolean acceptOddsChange, boolean parlay);

    /**
     * 按提前结算订单ID查询提前结算订单金额及状态
     * @param id
     */
    void getCashOutsByIds(String id);

}
