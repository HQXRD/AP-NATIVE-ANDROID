package com.xtree.bet.bean;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩法
 */
public class PlayTypeInfo implements BaseBean {
    /**
     * 玩法类型，如 亚盘、大小球等
     */
    public int mty;
    /**
     * 玩法阶段，如足球上半场、全场等，和玩法类型组合成一个玩法
     */
    public int pe;
    /**
     * 玩法赔率集合，带线玩法，数组里是多个，或者一个玩法，不带线玩法，数组就是一条数据
     */
    public List<OptionDataListInfo> mks;
    /**
     * 玩法展示分类数组， 如：热门、角球、波胆等，返回英文字母简称
     */
    public List<String> tps = new ArrayList<>();
    /**
     * 玩法名称
     */
    public String nm;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mty);
        dest.writeInt(this.pe);
        dest.writeTypedList(this.mks);
        dest.writeStringList(this.tps);
        dest.writeString(this.nm);
    }

    public void readFromParcel(Parcel source) {
        this.mty = source.readInt();
        this.pe = source.readInt();
        this.mks = source.createTypedArrayList(OptionDataListInfo.CREATOR);
        this.tps = source.createStringArrayList();
        this.nm = source.readString();
    }

    public PlayTypeInfo() {
    }

    protected PlayTypeInfo(Parcel in) {
        this.mty = in.readInt();
        this.pe = in.readInt();
        this.mks = in.createTypedArrayList(OptionDataListInfo.CREATOR);
        this.tps = in.createStringArrayList();
        this.nm = in.readString();
    }

    public static final Creator<PlayTypeInfo> CREATOR = new Creator<PlayTypeInfo>() {
        @Override
        public PlayTypeInfo createFromParcel(Parcel source) {
            return new PlayTypeInfo(source);
        }

        @Override
        public PlayTypeInfo[] newArray(int size) {
            return new PlayTypeInfo[size];
        }
    };
}
