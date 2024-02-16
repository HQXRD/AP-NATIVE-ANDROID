package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.response.fb.OptionInfo;

/**
 * 投注确认页投注选项信息
 */
public class BtConfirmOptionInfo implements BaseBean {
    /**
     * id : 143351231151851134
     * oddsStatus : 2
     * oddsType : 2
     * oddsValue : 0
     * playOptions : 0
     */

    /**
     * 盘口id(对应hid)
     */
    public String id;
    /**
     * 投注项状态 1：开盘，2：封盘(对应os)
     */
    public int oddsStatus;
    /**
     *  投注项类型
     */
    public String oddsType;
    /**
     *  投注项赔率(对应ofv)
     */
    public double oddsValue;
    /**
     * 投注项名称(投注时下注的玩法选项)
     */
    public String playOptions;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.oddsStatus);
        dest.writeString(this.oddsType);
        dest.writeDouble(this.oddsValue);
        dest.writeString(this.playOptions);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.oddsStatus = source.readInt();
        this.oddsType = source.readString();
        this.oddsValue = source.readDouble();
        this.playOptions = source.readString();
    }

    public BtConfirmOptionInfo() {
    }

    protected BtConfirmOptionInfo(Parcel in) {
        this.id = in.readString();
        this.oddsStatus = in.readInt();
        this.oddsType = in.readString();
        this.oddsValue = in.readDouble();
        this.playOptions = in.readString();
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
