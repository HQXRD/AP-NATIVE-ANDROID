package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

public class PaymentVo implements Parcelable {

    public String bankdirect_url;
    public int chongzhiListCount;
    public List<RechargeVo> chongzhiList;
    // public List<HashMap> chongzhiList;
    public ProcessingDataVo processingData;

    protected PaymentVo(Parcel in) {
        bankdirect_url = in.readString();
        chongzhiListCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankdirect_url);
        dest.writeInt(chongzhiListCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaymentVo> CREATOR = new Creator<PaymentVo>() {
        @Override
        public PaymentVo createFromParcel(Parcel in) {
            return new PaymentVo(in);
        }

        @Override
        public PaymentVo[] newArray(int size) {
            return new PaymentVo[size];
        }
    };

    @Override
    public String toString() {
        return "PaymentVo{" +
                "bankdirect_url='" + bankdirect_url + '\'' +
                ", chongzhiListCount=" + chongzhiListCount +
                ", chongzhiList=" + chongzhiList +
                '}';
    }

}
