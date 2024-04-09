package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 订单提前结算报价
 */
public class BtCashOutPriceInfo implements BaseBean {
    /**
     * 投注额
     */
    public double betAmount;
    /**
     * 主单号
     */
    public String orderNo;
    /**
     * 注单状态(0,未结算,1 已结算)
     */
    public String orderStatus;
    /**
     * 已提前结算额度
     */
    public double preBetAmount;
    /**
     * 返还金额
     */
    public double preSettleMaxWin;
    /**
     * 提前结算单价
     */
    public double csper;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.betAmount);
        dest.writeString(this.orderNo);
        dest.writeString(this.orderStatus);
        dest.writeDouble(this.preBetAmount);
        dest.writeDouble(this.preSettleMaxWin);
        dest.writeDouble(this.csper);
    }

    public void readFromParcel(Parcel source) {
        this.betAmount = source.readDouble();
        this.orderNo = source.readString();
        this.orderStatus = source.readString();
        this.preBetAmount = source.readDouble();
        this.preSettleMaxWin = source.readDouble();
        this.csper = source.readDouble();
    }

    public BtCashOutPriceInfo() {
    }

    protected BtCashOutPriceInfo(Parcel in) {
        this.betAmount = in.readDouble();
        this.orderNo = in.readString();
        this.orderStatus = in.readString();
        this.preBetAmount = in.readDouble();
        this.preSettleMaxWin = in.readDouble();
        this.csper = in.readDouble();
    }

    public static final Creator<BtCashOutPriceInfo> CREATOR = new Creator<BtCashOutPriceInfo>() {
        @Override
        public BtCashOutPriceInfo createFromParcel(Parcel source) {
            return new BtCashOutPriceInfo(source);
        }

        @Override
        public BtCashOutPriceInfo[] newArray(int size) {
            return new BtCashOutPriceInfo[size];
        }
    };
}
