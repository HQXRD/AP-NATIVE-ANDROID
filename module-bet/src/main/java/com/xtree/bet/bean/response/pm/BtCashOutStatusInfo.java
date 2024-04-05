package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 注单提前结算状态
 */
public class BtCashOutStatusInfo implements BaseBean {
    /**
     * 注单号
     */
    public String orderNo;
    /**
     * 结算前金额
     */
    public String frontSettleAmount;
    /**
     * 提前结算单状态 0：确认中 1：接单 2：拒单
     */
    public int preSettleOrderStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderNo);
        dest.writeString(this.frontSettleAmount);
        dest.writeInt(this.preSettleOrderStatus);
    }

    public void readFromParcel(Parcel source) {
        this.orderNo = source.readString();
        this.frontSettleAmount = source.readString();
        this.preSettleOrderStatus = source.readInt();
    }

    public BtCashOutStatusInfo() {
    }

    protected BtCashOutStatusInfo(Parcel in) {
        this.orderNo = in.readString();
        this.frontSettleAmount = in.readString();
        this.preSettleOrderStatus = in.readInt();
    }

    public static final Creator<BtCashOutStatusInfo> CREATOR = new Creator<BtCashOutStatusInfo>() {
        @Override
        public BtCashOutStatusInfo createFromParcel(Parcel source) {
            return new BtCashOutStatusInfo(source);
        }

        @Override
        public BtCashOutStatusInfo[] newArray(int size) {
            return new BtCashOutStatusInfo[size];
        }
    };
}
