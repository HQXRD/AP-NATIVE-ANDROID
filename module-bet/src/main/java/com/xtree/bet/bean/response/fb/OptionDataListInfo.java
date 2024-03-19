package com.xtree.bet.bean.response.fb;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩法赔率
 */
public class OptionDataListInfo implements BaseBean {
    public long id;
    /**
     * 玩法销售状态，0暂停，1开售，-1未开售（未开售状态一般是不展示的）
     */
    public int ss;
    /**
     * 是否支持串关，0 不可串关，1 可串关
     */
    public int au;
    /**
     * 是否为最优线，带线玩法可用该字段进行排序，从小到大
     */
    public int mbl;
    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     */
    public String li;
    /**
     * 玩法选项
     */
    public List<OptionInfo> op = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.ss);
        dest.writeInt(this.au);
        dest.writeInt(this.mbl);
        dest.writeString(this.li);
        dest.writeTypedList(this.op);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.ss = source.readInt();
        this.au = source.readInt();
        this.mbl = source.readInt();
        this.li = source.readString();
        this.op = source.createTypedArrayList(OptionInfo.CREATOR);
    }

    public OptionDataListInfo() {
    }

    protected OptionDataListInfo(Parcel in) {
        this.id = in.readLong();
        this.ss = in.readInt();
        this.au = in.readInt();
        this.mbl = in.readInt();
        this.li = in.readString();
        this.op = in.createTypedArrayList(OptionInfo.CREATOR);
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
