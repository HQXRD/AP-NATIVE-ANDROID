package com.xtree.bet.bean;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩法选项实时赔率及限额信息
 */
public class BtConfirmInfo implements BaseBean {
    /**
     * 投注确认页投注选项信息
     */
    public List<BtConfirmOptionInfo> bms = new ArrayList<>();
    /**
     * 串关组合赔率及限额
     */
    public List<CgOddLimitInfo> sos = new ArrayList<>();
    /**
     * 串关，最大投注额
     */
    public int mon;
    /**
     * 串关，最大投注额
     */
    public int msl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.bms);
        dest.writeTypedList(this.sos);
        dest.writeInt(this.mon);
        dest.writeInt(this.msl);
    }

    public void readFromParcel(Parcel source) {
        this.bms = source.createTypedArrayList(BtConfirmOptionInfo.CREATOR);
        this.sos = source.createTypedArrayList(CgOddLimitInfo.CREATOR);
        this.mon = source.readInt();
        this.msl = source.readInt();
    }

    public BtConfirmInfo() {
    }

    protected BtConfirmInfo(Parcel in) {
        this.bms = in.createTypedArrayList(BtConfirmOptionInfo.CREATOR);
        this.sos = in.createTypedArrayList(CgOddLimitInfo.CREATOR);
        this.mon = in.readInt();
        this.msl = in.readInt();
    }

    public static final Creator<BtConfirmInfo> CREATOR = new Creator<BtConfirmInfo>() {
        @Override
        public BtConfirmInfo createFromParcel(Parcel source) {
            return new BtConfirmInfo(source);
        }

        @Override
        public BtConfirmInfo[] newArray(int size) {
            return new BtConfirmInfo[size];
        }
    };
}
