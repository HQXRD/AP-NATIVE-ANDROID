package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class SeriesOrderInfo implements BaseBean {
    /**
     * betAmount : 500
     * marketType : EU
     * maxWinAmount : 432
     * orderNo : 5114101144335604
     * orderStatusCode : 1
     * seriesBetAmount : 500
     * seriesSum : 1
     * seriesValue : 2串1
     */

    public String betAmount;
    /**
     * 盘口类型
     */
    public String marketType;
    /**
     * maxWinAmount
     */
    public String maxWinAmount;
    /**
     * 投注时间(时间戳)
     */
    public String betTime;
    /**
     * 投注时间(YYYY-MM-DD)
     */
    public String betTimeStr;
    /**
     * 订单号
     */
    public String orderNo;
    /**
     * 订单状态
     */
    public int orderStatusCode;
    /**
     * 投注金额
     */
    public String seriesBetAmount;
    /**
     * 串关数量
     */
    public int seriesSum;
    /**
     * 串关描述。若注单为预约投注时，必填
     */
    public String seriesValue;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.betAmount);
        dest.writeString(this.marketType);
        dest.writeString(this.maxWinAmount);
        dest.writeString(this.orderNo);
        dest.writeInt(this.orderStatusCode);
        dest.writeString(this.seriesBetAmount);
        dest.writeInt(this.seriesSum);
        dest.writeString(this.seriesValue);
    }

    public void readFromParcel(Parcel source) {
        this.betAmount = source.readString();
        this.marketType = source.readString();
        this.maxWinAmount = source.readString();
        this.orderNo = source.readString();
        this.orderStatusCode = source.readInt();
        this.seriesBetAmount = source.readString();
        this.seriesSum = source.readInt();
        this.seriesValue = source.readString();
    }

    public SeriesOrderInfo() {
    }

    protected SeriesOrderInfo(Parcel in) {
        this.betAmount = in.readString();
        this.marketType = in.readString();
        this.maxWinAmount = in.readString();
        this.orderNo = in.readString();
        this.orderStatusCode = in.readInt();
        this.seriesBetAmount = in.readString();
        this.seriesSum = in.readInt();
        this.seriesValue = in.readString();
    }

    public static final Creator<SeriesOrderInfo> CREATOR = new Creator<SeriesOrderInfo>() {
        @Override
        public SeriesOrderInfo createFromParcel(Parcel source) {
            return new SeriesOrderInfo(source);
        }

        @Override
        public SeriesOrderInfo[] newArray(int size) {
            return new SeriesOrderInfo[size];
        }
    };
}
