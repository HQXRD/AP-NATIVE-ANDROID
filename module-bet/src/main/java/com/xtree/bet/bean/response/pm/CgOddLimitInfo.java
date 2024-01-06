package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 串关组合赔率及限额
 */
public class CgOddLimitInfo implements BaseBean {

    /**
     * code : 0
     * globalId : e1eaa8758d024132875a2e0b9d2a556c
     * minBet : 10.0
     * orderMaxPay : 300
     * playId : 4
     * playOptionsId : 146030915133953245
     * seriesOdds : 6.54
     * type : 3001
     */

    public int code;
    public String globalId;
    public String minBet;
    public String orderMaxPay;
    public String playId;
    public String playOptionsId;
    public String seriesOdds;
    public String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.globalId);
        dest.writeString(this.minBet);
        dest.writeString(this.orderMaxPay);
        dest.writeString(this.playId);
        dest.writeString(this.playOptionsId);
        dest.writeString(this.seriesOdds);
        dest.writeString(this.type);
    }

    public void readFromParcel(Parcel source) {
        this.code = source.readInt();
        this.globalId = source.readString();
        this.minBet = source.readString();
        this.orderMaxPay = source.readString();
        this.playId = source.readString();
        this.playOptionsId = source.readString();
        this.seriesOdds = source.readString();
        this.type = source.readString();
    }

    public CgOddLimitInfo() {
    }

    protected CgOddLimitInfo(Parcel in) {
        this.code = in.readInt();
        this.globalId = in.readString();
        this.minBet = in.readString();
        this.orderMaxPay = in.readString();
        this.playId = in.readString();
        this.playOptionsId = in.readString();
        this.seriesOdds = in.readString();
        this.type = in.readString();
    }

    public static final Creator<CgOddLimitInfo> CREATOR = new Creator<CgOddLimitInfo>() {
        @Override
        public CgOddLimitInfo createFromParcel(Parcel source) {
            return new CgOddLimitInfo(source);
        }

        @Override
        public CgOddLimitInfo[] newArray(int size) {
            return new CgOddLimitInfo[size];
        }
    };
}
