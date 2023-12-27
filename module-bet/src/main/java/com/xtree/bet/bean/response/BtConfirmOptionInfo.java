package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 投注确认页投注选项信息
 */
public class BtConfirmOptionInfo implements BaseBean {
    /**
     * 玩法id
     */
    public int mid;
    /**
     * 单关，最小投注额限制
     */
    public int smin;
    /**
     * 单关，最小投注额限制
     */
    public int smax;
    /**
     * 是否支持串关，0 不支持，1 支持
     */
    public int au;
    /**
     * 玩法销售状态，0暂停，1开售，-1未开售（未开售状态一般是不展示的）
     */
    public int ss;
    /**
     * 足球让球当前比分， 如1-1
     */
    public String re;
    /**
     * 失效玩法id，主要用于带线（球头）玩法变线后，替换原来失效的玩法id，用omid查询到对应玩法，然后替换成 mid
     */
    public int omid;
    /**
     * 是否为滚球 1滚球 0非滚球
     */
    public int ip;
    /**
     * 玩法选项赔率
     */
    public OptionInfo op;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mid);
        dest.writeDouble(this.smin);
        dest.writeDouble(this.smax);
        dest.writeInt(this.au);
        dest.writeInt(this.ss);
        dest.writeString(this.re);
        dest.writeInt(this.omid);
        dest.writeInt(this.ip);
        dest.writeParcelable(this.op, flags);
    }

    public void readFromParcel(Parcel source) {
        this.mid = source.readInt();
        this.smin = source.readInt();
        this.smax = source.readInt();
        this.au = source.readInt();
        this.ss = source.readInt();
        this.re = source.readString();
        this.omid = source.readInt();
        this.ip = source.readInt();
        this.op = source.readParcelable(OptionInfo.class.getClassLoader());
    }

    public BtConfirmOptionInfo() {
    }

    protected BtConfirmOptionInfo(Parcel in) {
        this.mid = in.readInt();
        this.smin = in.readInt();
        this.smax = in.readInt();
        this.au = in.readInt();
        this.ss = in.readInt();
        this.re = in.readString();
        this.omid = in.readInt();
        this.ip = in.readInt();
        this.op = in.readParcelable(OptionInfo.class.getClassLoader());
    }

    public static final Creator<BtConfirmOptionInfo> CREATOR = new Creator<BtConfirmOptionInfo>() {
        @Override
        public BtConfirmOptionInfo createFromParcel(Parcel source) {
            return new BtConfirmOptionInfo(source);
        }

        @Override
        public BtConfirmOptionInfo[] newArray(int size) {
            return new BtConfirmOptionInfo[size];
        }
    };
}
