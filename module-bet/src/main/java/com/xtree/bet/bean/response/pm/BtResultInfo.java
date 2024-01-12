package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class BtResultInfo implements BaseBean {

    /**
     * betMoneyTotal : 500
     * globalId : b0a77206831145d6bb28e269e65a7e4a
     * lock : 0
     * maxWinMoneyTotal : 432
     * orderDetailRespList : [{"addition":"","batchNum":"","betMoney":"500.0","marketType":"EU","marketValues":"","matchDay":"","matchDetailType":null,"matchInfo":"维冈竞技 v 曼联","matchName":"英格兰足总杯","matchStatus":0,"matchType":1,"maxWinMoney":"4.32","oddsType":"2","oddsValues":"1.18","orderNo":"5114101144335604","orderStatusCode":null,"playName":"全场独赢","playOptionName":"曼联","playOptionsId":"141127761003422046","preOrderDetailStatus":null,"scoreBenchmark":"","teamName":"维冈竞技"},{"addition":"","batchNum":"","betMoney":"500.0","marketType":"EU","marketValues":"","matchDay":"","matchDetailType":null,"matchInfo":"阿德莱德联 v 麦克阿瑟FC","matchName":"澳大利亚甲级联赛","matchStatus":0,"matchType":1,"maxWinMoney":"4.32","oddsType":"1","oddsValues":"1.58","orderNo":"5114101144335604","orderStatusCode":null,"playName":"全场独赢","playOptionName":"阿德莱德联","playOptionsId":"144388939366003410","preOrderDetailStatus":null,"scoreBenchmark":"","teamName":"阿德莱德联"}]
     * seriesOrderRespList : [{"betAmount":"500","marketType":"EU","maxWinAmount":"432","orderNo":"5114101144335604","orderStatusCode":1,"seriesBetAmount":"500","seriesSum":1,"seriesValue":"2串1"}]
     */

    /**
     * 总投注金额
     */
    public String betMoneyTotal;
    /**
     * 风控请求id
     */
    public String globalId;
    /**
     * 0：不锁，1：锁住
     */
    public int lock;
    /**
     * 最高可盈金额
     */
    public String maxWinMoneyTotal;
    /**
     * 订单下注成功信息
     */
    public List<BtResultOptionInfo> orderDetailRespList;
    /**
     * 注单信息
     */
    public List<SeriesOrderInfo> seriesOrderRespList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.betMoneyTotal);
        dest.writeString(this.globalId);
        dest.writeInt(this.lock);
        dest.writeString(this.maxWinMoneyTotal);
        dest.writeTypedList(this.orderDetailRespList);
        dest.writeTypedList(this.seriesOrderRespList);
    }

    public void readFromParcel(Parcel source) {
        this.betMoneyTotal = source.readString();
        this.globalId = source.readString();
        this.lock = source.readInt();
        this.maxWinMoneyTotal = source.readString();
        this.orderDetailRespList = source.createTypedArrayList(BtResultOptionInfo.CREATOR);
        this.seriesOrderRespList = source.createTypedArrayList(SeriesOrderInfo.CREATOR);
    }

    public BtResultInfo() {
    }

    protected BtResultInfo(Parcel in) {
        this.betMoneyTotal = in.readString();
        this.globalId = in.readString();
        this.lock = in.readInt();
        this.maxWinMoneyTotal = in.readString();
        this.orderDetailRespList = in.createTypedArrayList(BtResultOptionInfo.CREATOR);
        this.seriesOrderRespList = in.createTypedArrayList(SeriesOrderInfo.CREATOR);
    }

    public static final Creator<BtResultInfo> CREATOR = new Creator<BtResultInfo>() {
        @Override
        public BtResultInfo createFromParcel(Parcel source) {
            return new BtResultInfo(source);
        }

        @Override
        public BtResultInfo[] newArray(int size) {
            return new BtResultInfo[size];
        }
    };
}
