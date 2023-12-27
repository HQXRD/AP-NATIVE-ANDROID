package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 串关组合赔率及限额
 */
public class CgOddLimitInfo implements BaseBean {
    /**
     * 串关子单选项个数，如：投注4场比赛的3串1，此字段为3，如果是全串关（4串11×11），则为0；
     */
    public int sn;
    /**
     * 串关子单个数，如 投注4场比赛的3串1*4，此字段为4，全串关（4串11×11），则为11
     */
    public int in;
    /**
     * 串关对应的赔率
     */
    public double sodd;
    /**
     * 串关，最小投注额
     */
    public int mi;
    /**
     * 串关，最大投注额
     */
    public int mx;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sn);
        dest.writeInt(this.in);
        dest.writeDouble(this.sodd);
        dest.writeInt(this.mi);
        dest.writeInt(this.mx);
    }

    public void readFromParcel(Parcel source) {
        this.sn = source.readInt();
        this.in = source.readInt();
        this.sodd = source.readDouble();
        this.mi = source.readInt();
        this.mx = source.readInt();
    }

    public CgOddLimitInfo() {
    }

    protected CgOddLimitInfo(Parcel in) {
        this.sn = in.readInt();
        this.in = in.readInt();
        this.sodd = in.readDouble();
        this.mi = in.readInt();
        this.mx = in.readInt();
    }

    public static final Creator<CgOddLimitInfo> CREATOR = new Creator<CgOddLimitInfo>() {
        @Override
        public CgOddLimitInfo createFromParcel(Parcel source) {
            return new CgOddLimitInfo(source);
        }

        @Override
        public CgOddLimitInfo[] newArray(int size) {
            return new CgOddLimitInfo[size];
        }
    };
}
