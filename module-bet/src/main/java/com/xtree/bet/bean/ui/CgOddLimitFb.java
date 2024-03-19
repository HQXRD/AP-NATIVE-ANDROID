package com.xtree.bet.bean.ui;

import android.os.Parcel;

import com.xtree.bet.bean.response.fb.BtConfirmOptionInfo;
import com.xtree.bet.bean.response.fb.CgOddLimitInfo;

/**
 * 杏彩体育（FB）
 */
public class CgOddLimitFb implements CgOddLimit{
    /**
     * 比赛场数
     */
    private int matchCount;
    private double btCount;
    private CgOddLimitInfo cgOddLimitInfo;
    private BtConfirmOptionInfo betConfirmOption;
    public CgOddLimitFb(CgOddLimitInfo cgOddLimitInfo, BtConfirmOptionInfo betConfirmOptionInfo, int matchCount){
        this.cgOddLimitInfo = cgOddLimitInfo;
        this.betConfirmOption = betConfirmOptionInfo;
        this.matchCount = matchCount;
    }

    @Override
    public int getCgCount() {
        if(cgOddLimitInfo != null) {
            return cgOddLimitInfo.sn;
        }else {
            return 1;
        }
    }

    @Override
    public String getCgName() {
        if(cgOddLimitInfo == null){
            return "单关";
        }
        if(cgOddLimitInfo.sn == 0){
            return matchCount + "串" + cgOddLimitInfo.in;
        }else {
            return cgOddLimitInfo.sn + "串1";
        }

        /*data.sos.sn	integer	串关子单选项个数，如：投注4场比赛的3串1，此字段为3，如果是全串关（4串11×11），则为0；
        data.sos.in	integer	串关子单个数，如 投注4场比赛的3串1*4，此字段为4，全串关（4串11×11），则为11*/
    }

    @Override
    public String getCgType() {
        return null;
    }

    @Override
    public double getDMin() {
        if(betConfirmOption == null){
            return 5;
        }
        return betConfirmOption.smin;
    }

    @Override
    public double getDMax() {
        if(betConfirmOption == null){
            return 5;
        }
        return betConfirmOption.smax;
    }

    @Override
    public double getCMin() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return cgOddLimitInfo.mi;
    }

    @Override
    public double getCMax() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return cgOddLimitInfo.mx;
    }

    @Override
    public double getDOdd() {
        if(betConfirmOption == null || betConfirmOption.op == null){
            return 0;
        }
        return betConfirmOption.op.od;
    }

    @Override
    public double getCOdd() {
        if(cgOddLimitInfo == null){
            return 0;
        }
        return cgOddLimitInfo.sodd;
    }

    @Override
    public double getWin(double amount) {
        if(cgOddLimitInfo == null){
            if(betConfirmOption.op == null){
                return amount;
            }
            return betConfirmOption.op.od * amount;
        }
        return cgOddLimitInfo.sodd * amount;
    }

    @Override
    public int getBtCount() {
        if(cgOddLimitInfo == null){
            return 1;
        }
        return cgOddLimitInfo.in;
    }
    /**
     * 设置投注金额
     * @return
     */
    @Override
    public void setBtAmount(double count) {
        btCount = count;
    }
    /**
     * 获取投注金额
     * @return
     */
    @Override
    public double getBtAmount() {
        return btCount;
    }
    /**
     * 获取总投注金额
     * @return
     */
    @Override
    public double getBtTotalAmount() {
        return btCount * getBtCount();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.matchCount);
        dest.writeDouble(this.btCount);
        dest.writeParcelable(this.cgOddLimitInfo, flags);
        dest.writeParcelable(this.betConfirmOption, flags);
    }

    public void readFromParcel(Parcel source) {
        this.matchCount = source.readInt();
        this.btCount = source.readDouble();
        this.cgOddLimitInfo = source.readParcelable(CgOddLimitInfo.class.getClassLoader());
        this.betConfirmOption = source.readParcelable(BtConfirmOptionInfo.class.getClassLoader());
    }

    protected CgOddLimitFb(Parcel in) {
        this.matchCount = in.readInt();
        this.btCount = in.readDouble();
        this.cgOddLimitInfo = in.readParcelable(CgOddLimitInfo.class.getClassLoader());
        this.betConfirmOption = in.readParcelable(BtConfirmOptionInfo.class.getClassLoader());
    }

    public static final Creator<CgOddLimitFb> CREATOR = new Creator<CgOddLimitFb>() {
        @Override
        public CgOddLimitFb createFromParcel(Parcel source) {
            return new CgOddLimitFb(source);
        }

        @Override
        public CgOddLimitFb[] newArray(int size) {
            return new CgOddLimitFb[size];
        }
    };
}
