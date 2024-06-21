package com.xtree.recharge.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class PayOrderDataVo implements Parcelable {

    public String merchant_order; // "PAY0017292024052818444600000656"
    public String platform_order; // "471b6c1b"
    public String amount; // 133
    public OrderInfoVo orderInfo; // 自己加的
    public Object orderinfo; // 接口返回的,有时可能会是 []

    protected PayOrderDataVo(Parcel in) {
        merchant_order = in.readString();
        platform_order = in.readString();
        amount = in.readString();
        orderInfo = in.readParcelable(Object.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(merchant_order);
        dest.writeString(platform_order);
        dest.writeString(amount);
        dest.writeParcelable(orderInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PayOrderDataVo> CREATOR = new Creator<PayOrderDataVo>() {
        @Override
        public PayOrderDataVo createFromParcel(Parcel in) {
            return new PayOrderDataVo(in);
        }

        @Override
        public PayOrderDataVo[] newArray(int size) {
            return new PayOrderDataVo[size];
        }
    };

    @Override
    public String toString() {
        return "PayOrderDataVo { " +
                "merchant_order='" + merchant_order + '\'' +
                ", platform_order='" + platform_order + '\'' +
                ", amount='" + amount + '\'' +
                ", orderInfo=" + orderInfo +
                '}';
    }

}
