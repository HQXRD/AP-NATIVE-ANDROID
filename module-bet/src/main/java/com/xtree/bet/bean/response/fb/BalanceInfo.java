package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class BalanceInfo implements BaseBean {

    public String uid; // "1896.0790",
    public String bl; // "35.00",
    public String currencyId; // "0.00",


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.bl);
        dest.writeString(this.currencyId);
    }

    public void readFromParcel(Parcel source) {
        this.uid = source.readString();
        this.bl = source.readString();
        this.currencyId = source.readString();
    }

    public BalanceInfo() {
    }

    protected BalanceInfo(Parcel in) {
        this.uid = in.readString();
        this.bl = in.readString();
        this.currencyId = in.readString();
    }

    public static final Creator<BalanceInfo> CREATOR = new Creator<BalanceInfo>() {
        @Override
        public BalanceInfo createFromParcel(Parcel source) {
            return new BalanceInfo(source);
        }

        @Override
        public BalanceInfo[] newArray(int size) {
            return new BalanceInfo[size];
        }
    };
}
