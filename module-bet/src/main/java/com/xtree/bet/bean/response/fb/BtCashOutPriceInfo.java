package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单提前结算报价
 */
public class BtCashOutPriceInfo implements BaseBean {
    /**
     * 单笔订单最大有效提前结算次数
     */
    public int mxc;
    /**
     * 报价数据
     */
    public List<BtCashOutPriceOrderInfo> pr = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mxc);
        dest.writeTypedList(this.pr);
    }

    public void readFromParcel(Parcel source) {
        this.mxc = source.readInt();
        this.pr = source.createTypedArrayList(BtCashOutPriceOrderInfo.CREATOR);
    }

    public BtCashOutPriceInfo() {
    }

    protected BtCashOutPriceInfo(Parcel in) {
        this.mxc = in.readInt();
        this.pr = in.createTypedArrayList(BtCashOutPriceOrderInfo.CREATOR);
    }

    public static final Creator<BtCashOutPriceInfo> CREATOR = new Creator<BtCashOutPriceInfo>() {
        @Override
        public BtCashOutPriceInfo createFromParcel(Parcel source) {
            return new BtCashOutPriceInfo(source);
        }

        @Override
        public BtCashOutPriceInfo[] newArray(int size) {
            return new BtCashOutPriceInfo[size];
        }
    };
}
