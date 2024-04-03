package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 订单提前结算
 */
public class BtCashOutStatusInfo implements BaseBean {
    /**
     * 提前结算ID
     */
    public String id;
    /**
     * 创建时间，13位数字时间戳
     */
    public long createTime;
    /**
     * 提前结算本金
     */
    public double cashOutStake;
    /**
     * 提前结算派彩金额
     */
    public double cashOutPayoutStake;
    /**
     * 提前结算利润
     */
    public double cashOutProfitStake;
    /**
     * 提前结算订单状态，0创建成功，1接单确认中，2拒单，3取消，4接单成功，5结算 , see enum: cash_out_order_status
     */
    public int orderStatus;


    /**
     * 取消提前结算时间，13位数字时间戳
     */
    public int cancelTime;

    /**
     * 备注
     */
    public String remark;

    /**
     * 取消原因code
     */
    public int cancelReasonCode;
    /**
     * 取消后退返金额
     */
    public double cancelCashOutAmountTo;
    /**
     * 对应的原始订单ID
     */
    public String orderId;
    /**
     * 币种ID
     */
    public int currencyId;
    /**
     * 汇率快照
     */
    public double exchangeRate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.createTime);
        dest.writeDouble(this.cashOutStake);
        dest.writeDouble(this.cashOutPayoutStake);
        dest.writeDouble(this.cashOutProfitStake);
        dest.writeInt(this.orderStatus);
        dest.writeInt(this.cancelTime);
        dest.writeString(this.remark);
        dest.writeInt(this.cancelReasonCode);
        dest.writeDouble(this.cancelCashOutAmountTo);
        dest.writeString(this.orderId);
        dest.writeInt(this.currencyId);
        dest.writeDouble(this.exchangeRate);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.createTime = source.readLong();
        this.cashOutStake = source.readDouble();
        this.cashOutPayoutStake = source.readDouble();
        this.cashOutProfitStake = source.readDouble();
        this.orderStatus = source.readInt();
        this.cancelTime = source.readInt();
        this.remark = source.readString();
        this.cancelReasonCode = source.readInt();
        this.cancelCashOutAmountTo = source.readDouble();
        this.orderId = source.readString();
        this.currencyId = source.readInt();
        this.exchangeRate = source.readDouble();
    }

    public BtCashOutStatusInfo() {
    }

    protected BtCashOutStatusInfo(Parcel in) {
        this.id = in.readString();
        this.createTime = in.readLong();
        this.cashOutStake = in.readDouble();
        this.cashOutPayoutStake = in.readDouble();
        this.cashOutProfitStake = in.readLong();
        this.orderStatus = in.readInt();
        this.cancelTime = in.readInt();
        this.remark = in.readString();
        this.cancelReasonCode = in.readInt();
        this.cancelCashOutAmountTo = in.readDouble();
        this.orderId = in.readString();
        this.currencyId = in.readInt();
        this.exchangeRate = in.readDouble();
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
