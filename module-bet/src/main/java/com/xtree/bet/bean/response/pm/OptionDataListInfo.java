package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩法赔率
 */
public class OptionDataListInfo implements BaseBean {
    /**
     * 盘口id
     */
    public String hid;
    /**
     * 盘口类型：0-滚球，1-早盘
     */
    public int hmt;
    /**
     * 盘口级别
     */
    public String hon;
    /**
     * 盘口状态
     */
    public int hs;
    /**
     * 盘口坑位
     */
    public int hn;
    /**
     * 盘口值
     */
    public String hv;

    /**
     * 玩法选项
     */
    public List<OptionInfo> ol = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hid);
        dest.writeInt(this.hmt);
        dest.writeString(this.hon);
        dest.writeInt(this.hs);
        dest.writeInt(this.hn);
        dest.writeString(this.hv);
        dest.writeTypedList(this.ol);
    }

    public void readFromParcel(Parcel source) {
        this.hid = source.readString();
        this.hmt = source.readInt();
        this.hon = source.readString();
        this.hs = source.readInt();
        this.hn = source.readInt();
        this.hv = source.readString();
        this.ol = source.createTypedArrayList(OptionInfo.CREATOR);
    }

    public OptionDataListInfo() {
    }

    protected OptionDataListInfo(Parcel in) {
        this.hid = in.readString();
        this.hmt = in.readInt();
        this.hon = in.readString();
        this.hs = in.readInt();
        this.hn = in.readInt();
        this.hv = in.readString();
        this.ol = in.createTypedArrayList(OptionInfo.CREATOR);
    }

    public static final Creator<OptionDataListInfo> CREATOR = new Creator<OptionDataListInfo>() {
        @Override
        public OptionDataListInfo createFromParcel(Parcel source) {
            return new OptionDataListInfo(source);
        }

        @Override
        public OptionDataListInfo[] newArray(int size) {
            return new OptionDataListInfo[size];
        }
    };
}
