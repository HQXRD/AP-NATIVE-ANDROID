package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 订单提前结算
 */
public class BtCashOutBetInfo implements BaseBean {
    /**
     * 下注成功后，生成提前结算id
     */
    public String id;
    /**
     * 提前结算订单状态，下注后为异步接单，该字段只返回0创建成功、1确认中
     */
    public int st;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.st);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.st = source.readInt();
    }

    public BtCashOutBetInfo() {
    }

    protected BtCashOutBetInfo(Parcel in) {
        this.id = in.readString();
        this.st = in.readInt();
    }

    public static final Creator<BtCashOutBetInfo> CREATOR = new Creator<BtCashOutBetInfo>() {
        @Override
        public BtCashOutBetInfo createFromParcel(Parcel source) {
            return new BtCashOutBetInfo(source);
        }

        @Override
        public BtCashOutBetInfo[] newArray(int size) {
            return new BtCashOutBetInfo[size];
        }
    };
}
