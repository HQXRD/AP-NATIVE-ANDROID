package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 玩法选项
 */
public class OptionInfo implements BaseBean {
    /**
     * 数据源
     */
    public String cds;
    /**
     * 投注项id
     */
    public String oid;
    /**
     * 投注项投注名称展示值
     */
    public String on;
    /**
     * 	投注项对应的title
     */
    public String ott;
    /**
     * 	赛果(2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期)
     */
    public Integer result;
    /**
     * 投注项列表页名称展示值
     */
    public String onb;
    /**
     * 投注项状态
     */
    public int os;
    /**
     * 赔率
     */
    public String ot;
    /**
     * 投注项展示模板id和titile的otd对应
     */
    public int otd;
    /**
     * 投注给那一方 T1：主队，T2：客队
     */
    public String ots;
    /**
     * 投注时展示的内容
     */
    public String otv;
    /**
     * 	赔率
     */
    public double ov;
    /**
     * 是否选中
     */
    public boolean isSelected;
    public int change;
    /**
     *
     */
    public int oddSort;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cds);
        dest.writeString(this.oid);
        dest.writeString(this.on);
        dest.writeString(this.onb);
        dest.writeInt(this.os);
        dest.writeString(this.ot);
        dest.writeInt(this.otd);
        dest.writeString(this.ots);
        dest.writeString(this.otv);
        dest.writeDouble(this.ov);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.change);
        dest.writeInt(this.oddSort);
    }

    public void readFromParcel(Parcel source) {
        this.cds = source.readString();
        this.oid = source.readString();
        this.on = source.readString();
        this.onb = source.readString();
        this.os = source.readInt();
        this.ot = source.readString();
        this.otd = source.readInt();
        this.ots = source.readString();
        this.otv = source.readString();
        this.ov = source.readDouble();
        this.isSelected = source.readByte() != 0;
        this.change = source.readInt();
        this.oddSort = source.readInt();
    }

    public OptionInfo() {
    }

    protected OptionInfo(Parcel in) {
        this.cds = in.readString();
        this.oid = in.readString();
        this.on = in.readString();
        this.onb = in.readString();
        this.os = in.readInt();
        this.ot = in.readString();
        this.otd = in.readInt();
        this.ots = in.readString();
        this.otv = in.readString();
        this.ov = in.readDouble();
        this.isSelected = in.readByte() != 0;
        this.change = in.readInt();
        this.oddSort = in.readInt();
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
