package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class BtResultOptionInfo implements BaseBean {
    /**
     * 盘口id
     */
    public String mid;
    /**
     * 欧式赔率
     */
    public String od;
    /**
     * 赔率类型
     */
    public int of;
    /**
     * 下注赔率
     */
    public String bod;
    /**
     * 第三方备注
     */
    public String tr;
    /**
     * 下单时三方带的订单ID
     */
    public String rid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mid);
        dest.writeString(this.od);
        dest.writeInt(this.of);
        dest.writeString(this.bod);
        dest.writeString(this.tr);
        dest.writeString(this.rid);
    }

    public void readFromParcel(Parcel source) {
        this.mid = source.readString();
        this.od = source.readString();
        this.of = source.readInt();
        this.bod = source.readString();
        this.tr = source.readString();
        this.rid = source.readString();
    }

    public BtResultOptionInfo() {
    }

    protected BtResultOptionInfo(Parcel in) {
        this.mid = in.readString();
        this.od = in.readString();
        this.of = in.readInt();
        this.bod = in.readString();
        this.tr = in.readString();
        this.rid = in.readString();
    }

    public static final Creator<BtResultOptionInfo> CREATOR = new Creator<BtResultOptionInfo>() {
        @Override
        public BtResultOptionInfo createFromParcel(Parcel source) {
            return new BtResultOptionInfo(source);
        }

        @Override
        public BtResultOptionInfo[] newArray(int size) {
            return new BtResultOptionInfo[size];
        }
    };
}
