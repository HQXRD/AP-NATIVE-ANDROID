package com.xtree.bet.bean;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 玩法选项
 */
public class OptionInfo implements BaseBean {
    /**
     * 选项全称，投注框一般用全称展示
     */
    public String na;
    /**
     * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示
     */
    public String nm;
    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     */
    public int ty;
    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘
     */
    public double od;
    /**
     * 赔率
     */
    public double bod;
    /**
     * 赔率类型
     */
    public int odt;
    /**
     * 选项结算结果，仅虚拟体育展示
     */
    public int otcm;
    /**
     * 是否选中
     */
    public boolean isSelected;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.na);
        dest.writeString(this.nm);
        dest.writeInt(this.ty);
        dest.writeDouble(this.od);
        dest.writeDouble(this.bod);
        dest.writeInt(this.odt);
        dest.writeInt(this.otcm);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.na = source.readString();
        this.nm = source.readString();
        this.ty = source.readInt();
        this.od = source.readDouble();
        this.bod = source.readDouble();
        this.odt = source.readInt();
        this.otcm = source.readInt();
        this.isSelected = source.readByte() != 0;
    }

    public OptionInfo() {
    }

    protected OptionInfo(Parcel in) {
        this.na = in.readString();
        this.nm = in.readString();
        this.ty = in.readInt();
        this.od = in.readDouble();
        this.bod = in.readDouble();
        this.odt = in.readInt();
        this.otcm = in.readInt();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<OptionInfo> CREATOR = new Creator<OptionInfo>() {
        @Override
        public OptionInfo createFromParcel(Parcel source) {
            return new OptionInfo(source);
        }

        @Override
        public OptionInfo[] newArray(int size) {
            return new OptionInfo[size];
        }
    };
}
