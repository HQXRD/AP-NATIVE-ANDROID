package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 报价数据
 */
public class BtCashOutPriceOrderInfo implements BaseBean {
    /**
     * 订单ID，返回为字符串
     */
    public String oid;
    /**
     * 提前结算1元的报价，如返回0.92，就是提前结算1元可获得0.92元，无价格或者为0表示不能进行提前结算
     */
    public double amt;
    /**
     * 当前订单如果有提前结算订单，则返回提前结算状态，如果无提前结算则返回订单状态，
     * 0:订单创建 1：订单确认中，2：订单拒绝 3：订单取消，4：订单已接单 5；订单已结算 101：预约提前结算中 102：提前结算进行中 , see enum: ask_cash_out_status
     */
    public int st;
    /**
     * 单关订单提前结算单次最小结算本金
     */
    public double smis;
    /**
     * 串关订单提前结算单次最小结算本金
     */
    public double pmis;
    /**
     * 预约提前结算金额
     */
    public String rcs;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.oid);
        dest.writeDouble(this.amt);
        dest.writeInt(this.st);
        dest.writeDouble(this.smis);
        dest.writeDouble(this.pmis);
        dest.writeString(this.rcs);
    }

    public void readFromParcel(Parcel source) {
        this.oid = source.readString();
        this.amt = source.readDouble();
        this.st = source.readInt();
        this.smis = source.readDouble();
        this.pmis = source.readDouble();
        this.rcs = source.readString();
    }

    public BtCashOutPriceOrderInfo() {
    }

    protected BtCashOutPriceOrderInfo(Parcel in) {
        this.oid = in.readString();
        this.amt = in.readDouble();
        this.st = in.readInt();
        this.smis = in.readDouble();
        this.pmis = in.readDouble();
        this.rcs = in.readString();
    }

    public static final Creator<BtCashOutPriceOrderInfo> CREATOR = new Creator<BtCashOutPriceOrderInfo>() {
        @Override
        public BtCashOutPriceOrderInfo createFromParcel(Parcel source) {
            return new BtCashOutPriceOrderInfo(source);
        }

        @Override
        public BtCashOutPriceOrderInfo[] newArray(int size) {
            return new BtCashOutPriceOrderInfo[size];
        }
    };
}
