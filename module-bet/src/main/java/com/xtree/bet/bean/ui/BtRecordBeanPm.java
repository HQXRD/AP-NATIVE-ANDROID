package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.bet.bean.response.fb.BtResultInfo;
import com.xtree.bet.bean.response.fb.BtResultOptionInfo;
import com.xtree.bet.bean.response.pm.BtRecordRsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BtRecordBeanPm implements BtResult {
    private BtRecordRsp.RecordsBean recordsBean;
    private static Map<String, String> statusMap = new HashMap<>();

    static {
        statusMap.put("0", "未结算");
        statusMap.put("1", "已结算");
        statusMap.put("2", "注单取消");
        statusMap.put("3", "确认中");
        statusMap.put("4", "投注失败");
    }

    public BtRecordBeanPm(BtRecordRsp.RecordsBean recordsBean){
        this.recordsBean = recordsBean;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getStatusDesc() {
        return statusMap.get(recordsBean.orderStatus);
    }

    @Override
    public String getId() {
        return recordsBean.orderNo;
    }

    @Override
    public boolean isSuccessed() {
        return !(Integer.valueOf(recordsBean.orderStatus) == 2 || Integer.valueOf(recordsBean.orderStatus) == 3);
    }

    @Override
    public String getCgName() {
        if(recordsBean == null){
            return "";
        }
        return recordsBean.seriesValue;

        /*
        data.records.sv 串关子单选项个数，如：投注4场比赛的3串1，此字段为3，如果是全串关（4串11*11），则为0；
        data.records.bn 总注单数，单关为1，串关为子单个数，如4串4*11，则该字段为11*/
    }
    /**
     * 获取投注金额
     * @return
     */
    @Override
    public double getBtAmount() {
        return recordsBean.orderAmountTotal;
    }
    /**
     * 获取可赢金额
     * @return
     */
    @Override
    public double getBtWin() {
        return recordsBean.maxWinAmount;
    }
    /**
     * 获取投注时间
     * @return
     */
    @Override
    public long getBtDate() {
        return Long.valueOf(recordsBean.betTime);
    }

    @Override
    public List<BtResultOption> getBetResultOption() {
        List<BtResultOption> resultOptionList = new ArrayList<>();
        for(BtRecordRsp.RecordsBean.DetailBean detailBean : recordsBean.detailList){
            resultOptionList.add(new BtResultOptionPm(detailBean));
        }
        return resultOptionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.recordsBean, flags);
    }

    public void readFromParcel(Parcel source) {
        this.recordsBean = source.readParcelable(BtRecordRsp.RecordsBean.class.getClassLoader());
    }

    protected BtRecordBeanPm(Parcel in) {
        this.recordsBean = in.readParcelable(BtRecordRsp.RecordsBean.class.getClassLoader());
    }

    public static final Creator<BtRecordBeanPm> CREATOR = new Creator<BtRecordBeanPm>() {
        @Override
        public BtRecordBeanPm createFromParcel(Parcel source) {
            return new BtRecordBeanPm(source);
        }

        @Override
        public BtRecordBeanPm[] newArray(int size) {
            return new BtRecordBeanPm[size];
        }
    };
}
