package com.xtree.bet.ui.viewmodel.pm;

import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401038;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.bet.bean.request.pm.BtRecordReq;
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
    public PMBtRecordModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 投注前查询指定玩法赔率
     */
    public void betRecord(boolean isSettled){

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

    }

}
