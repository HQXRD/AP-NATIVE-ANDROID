package com.xtree.bet.bean.ui;

import android.os.Parcel;

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

    @Override
    public boolean isSuccessed() {
        return !(btResultInfo.st == 2 || btResultInfo.st == 3);
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
