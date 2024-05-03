package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.fb.BtResultInfo;
import com.xtree.bet.bean.response.fb.BtResultOptionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BtResultFb implements BtResult {
    private BtResultInfo btResultInfo;
    private static Map<String, String> statusMap = new HashMap<>();

    static {
        statusMap.put("0", "未确认");
        statusMap.put("1", "确认中");
        statusMap.put("2", "已拒单");
        statusMap.put("3", "已取消");
        statusMap.put("4", "投注成功");
        statusMap.put("5", "已结算");
    }

    public BtResultFb(BtResultInfo btResultInfo){
        this.btResultInfo = btResultInfo;
    }

    @Override
    public int getStatus() {
        return btResultInfo.st;
    }

    @Override
    public String getStatusDesc() {
        int status = btResultInfo.st;
        return statusMap.get(String.valueOf(status));
    }

    @Override
    public String getId() {
        return btResultInfo.id;
    }

    /**
     * 是否下单成功
     * @return
     */
    @Override
    public boolean isSuccessed() {
        return !(btResultInfo.st == 2 || btResultInfo.st == 3);
    }

    /**
     * 注单是否已结算
     * @return
     */
    @Override
    public boolean isSettled() {
        return btResultInfo.st == 2 || btResultInfo.st == 3 || btResultInfo.st == 5;
    }

    /**
     * 已结算注单用户输赢
     * @return
     */
    @Override
    public double userWin() {
        return Double.valueOf(btResultInfo.uwl);
    }

    @Override
    public String getCgName() {
        if(btResultInfo == null){
            return "";
        }
        if(btResultInfo.sv == 0){
            return btResultInfo.al + "串" + btResultInfo.bn;
        }else {
            return btResultInfo.sv + "串1";
        }

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
        return btResultInfo.sat;
    }
    /**
     * 获取可赢金额
     * @return
     */
    @Override
    public double getBtWin() {
        return btResultInfo.mwa;
    }
    /**
     * 获取投注时间
     * @return
     */
    @Override
    public long getBtDate() {
        return btResultInfo.cte;
    }

    @Override
    public List<BtResultOption> getBetResultOption() {
        List<BtResultOption> resultOptionList = new ArrayList<>();
        for(BtResultOptionInfo btResultOptionInfo : btResultInfo.ops){
            resultOptionList.add(new BtResultOptionFb(btResultOptionInfo));
        }
        return resultOptionList;
    }

    @Override
    public boolean canAdvanceSettle() {
        return btResultInfo != null && btResultInfo.pr != null && btResultInfo.pr.amt > 0;
    }

    @Override
    public double getAdvanceSettleAmount() {
        if(canAdvanceSettle()){
            return btResultInfo.pr.amt * getBtAmount();
        }
        return 0;
    }

    @Override
    public double getUnitCashOutPayoutStake() {
        if(canAdvanceSettle()){
            return btResultInfo.pr.amt;
        }
        return 0;
    }

    @Override
    public boolean isAdvanceSettlement() {
        return btResultInfo != null && btResultInfo.crl != null && !btResultInfo.crl.isEmpty();
    }

    @Override
    public String getAdvanceSettlementStatus() {
        if(isAdvanceSettlement()){
            int status = btResultInfo.crl.get(0).st;
            if(status == 1){
                return "确认中";
            } else if (status == 5) {
                return "全部提前结算成功";
            } else if (status == 3) {
                return "已取消";
            } else if (status == 2) {
                return "已拒单";
            } else if (status == 4) {
                return "已接单";
            }
        }
        return "";
    }

    @Override
    public String getAdvanceSettlementDate() {
        return TimeUtils.longFormatString(btResultInfo.crl.get(0).ct, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
    }

    @Override
    public double getAdvanceSettlementCost() {
        return btResultInfo.crl.get(0).cst;
    }

    @Override
    public double getAdvanceSettlementResult() {
        return btResultInfo.crl.get(0).cops - btResultInfo.crl.get(0).cst;
    }

    @Override
    public double getAdvanceSettlementBack() {
        return btResultInfo.crl.get(0).cops;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.btResultInfo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.btResultInfo = source.readParcelable(BtResultInfo.class.getClassLoader());
    }

    protected BtResultFb(Parcel in) {
        this.btResultInfo = in.readParcelable(BtResultInfo.class.getClassLoader());
    }

    public static final Creator<BtResultFb> CREATOR = new Creator<BtResultFb>() {
        @Override
        public BtResultFb createFromParcel(Parcel source) {
            return new BtResultFb(source);
        }

        @Override
        public BtResultFb[] newArray(int size) {
            return new BtResultFb[size];
        }
    };
}
