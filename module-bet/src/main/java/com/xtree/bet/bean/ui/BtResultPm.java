package com.xtree.bet.bean.ui;

import android.os.Parcel;


import com.xtree.bet.bean.response.pm.BtResultInfo;
import com.xtree.bet.bean.response.pm.SeriesOrderInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BtResultPm implements BtResult {
    private SeriesOrderInfo seriesOrderInfo;
    private static Map<String, String> statusMap = new HashMap<>();

    static {
        statusMap.put("0", "投注失败");
        statusMap.put("1", "接单");
        statusMap.put("2", "确认中");
    }

    public BtResultPm(SeriesOrderInfo seriesOrderInfo){
        this.seriesOrderInfo = seriesOrderInfo;
    }

    @Override
    public int getStatus() {
        return seriesOrderInfo.orderStatusCode;
    }

    @Override
    public String getStatusDesc() {
        return statusMap.get(String.valueOf(getStatus()));
    }

    @Override
    public String getId() {
        return seriesOrderInfo.orderNo;
    }

    @Override
    public boolean isSuccessed() {
        return seriesOrderInfo.orderStatusCode != 0;
    }

    @Override
    public boolean isSettled() {
        return false;
    }

    @Override
    public double userWin() {
        return 0;
    }

    @Override
    public String getCgName() {
        if(seriesOrderInfo == null){
            return "";
        } else  {
            return seriesOrderInfo.seriesValue;
        }
    }
    /**
     * 获取投注金额
     * @return
     */
    @Override
    public double getBtAmount() {
        return Double.valueOf(seriesOrderInfo.seriesBetAmount);
    }
    /**
     * 获取可赢金额
     * @return
     */
    @Override
    public double getBtWin() {
        return Double.valueOf(seriesOrderInfo.maxWinAmount);
    }
    /**
     * 获取投注时间
     * @return
     */
    @Override
    public long getBtDate() {
        return System.currentTimeMillis();
    }

    @Override
    public List<BtResultOption> getBetResultOption() {
        List<BtResultOption> resultOptionList = new ArrayList<>();
        /*for(BtResultOptionInfo btResultOptionInfo : seriesOrderInfo.ops){
            resultOptionList.add(new BtResultOptionFb(btResultOptionInfo));
        }*/
        return resultOptionList;
    }

    @Override
    public boolean canAdvanceSettle() {
        return false;
    }

    @Override
    public double getAdvanceSettleAmount() {
        return 0;
    }

    @Override
    public double getUnitCashOutPayoutStake() {
        return 0;
    }

    @Override
    public boolean isAdvanceSettlement() {
        return false;
    }

    @Override
    public String getAdvanceSettlementStatus() {
        return null;
    }

    @Override
    public String getAdvanceSettlementDate() {
        return null;
    }

    @Override
    public double getAdvanceSettlementCost() {
        return 0;
    }

    @Override
    public double getAdvanceSettlementResult() {
        return 0;
    }

    @Override
    public double getAdvanceSettlementBack() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.seriesOrderInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.seriesOrderInfo = source.readParcelable(BtResultInfo.class.getClassLoader());
    }

    protected BtResultPm(Parcel in) {
        this.seriesOrderInfo = in.readParcelable(BtResultInfo.class.getClassLoader());
    }

    public static final Creator<BtResultPm> CREATOR = new Creator<BtResultPm>() {
        @Override
        public BtResultPm createFromParcel(Parcel source) {
            return new BtResultPm(source);
        }

        @Override
        public BtResultPm[] newArray(int size) {
            return new BtResultPm[size];
        }
    };
}
