package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 提前结算记录
 */
public class BtAdvanceSettlement implements BaseBean {
    /**
     * 提前结算订单ID
     */
    public String id;
    /**
     * 提前结算订单创建时间
     */
    public long ct;
    /**
     * 提前结算本金
     */
    public double cst;
    /**
     * 提前结算派奖金额
     */
    public double cops;
    /**
     * 提前结算订单状态，0创建成功，1接单确认中，2拒单，3取消，4接单成功，5结算
     */
    public int st;
    /**
     * 提前结算对应的原始订单ID
     */
    public String oid;
    /**
     * 提前结算取消原因
     */
    public String cr;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.ct);
        dest.writeDouble(this.cst);
        dest.writeDouble(this.cops);
        dest.writeInt(this.st);
        dest.writeString(this.oid);
        dest.writeString(this.cr);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.ct = source.readLong();
        this.cst = source.readDouble();
        this.cops = source.readDouble();
        this.st = source.readInt();
        this.oid = source.readString();
        this.cr = source.readString();
    }

    public BtAdvanceSettlement() {
    }

    protected BtAdvanceSettlement(Parcel in) {
        this.id = in.readString();
        this.ct = in.readLong();
        this.cst = in.readDouble();
        this.cops = in.readDouble();
        this.st = in.readInt();
        this.oid = in.readString();
        this.cr = in.readString();
    }

    public static final Creator<BtAdvanceSettlement> CREATOR = new Creator<BtAdvanceSettlement>() {
        @Override
        public BtAdvanceSettlement createFromParcel(Parcel source) {
            return new BtAdvanceSettlement(source);
        }

        @Override
        public BtAdvanceSettlement[] newArray(int size) {
            return new BtAdvanceSettlement[size];
        }
    };
}
