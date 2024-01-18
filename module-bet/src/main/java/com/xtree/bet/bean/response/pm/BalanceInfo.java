package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class BalanceInfo implements BaseBean {

    public double amount; // "1896.0790",

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.amount);
    }

    public void readFromParcel(Parcel source) {
        this.amount = source.readDouble();
    }

    public BalanceInfo() {
    }

    protected BalanceInfo(Parcel in) {
        this.amount = in.readDouble();
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
