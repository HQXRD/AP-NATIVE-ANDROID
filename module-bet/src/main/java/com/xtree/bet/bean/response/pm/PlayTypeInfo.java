package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;
import com.xtree.bet.bean.ui.OptionList;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩法
 */
public class PlayTypeInfo implements BaseBean, Comparable {
    /**
     * 盘口id
     */
    public long hid;
    /**
     * 盘口名称
     */
    public String hps;
    /**
     *
     */
    public int hpono;
    /**
     * 玩法销售状态，0暂停，1开售，-1未开售（未开售状态一般是不展示的）
     */
    public int hs;
    /**
     * 盘口类型：0-滚球，1-早盘
     */
    public int hmt;
    /**
     * 盘口坑位
     */
    public int hn;
    /**
     *
     */
    public String hmed;
    /**
     *
     */
    public String hmgt;

    /**
     * 是否支持串关 1 支持，0 不支持
     */
    public int hids;
    /**
     * 所属玩法集id
     */
    public String hlid;
    /**
     * 是否多玩法
     */
    public int hmm;
    /**
     * 玩法id
     */
    public String hpid;
    /**
     * 玩法名称
     */
    public String hpn;
    /**
     * 玩法排序值
     */
    public int hpon;
    /**
     * 玩法展示模板id，目前玩法模版不对外提供，可以结合此id设计展示类型
     */
    public int hpt;
    /**
     * 是否展示，默认Yes
     */
    public String hshow;
    /**
     * 支持赔率类型 1：支持欧式、英式、美式、香港、马来、印尼赔率，2：支持欧式、英式、美式赔率
     */
    public String hsw;
    /**
     * 置顶排序值
     */
    public String hton;
    /**
     * 赛事id
     */
    public long mid;
    /**
     * 盘口集合
     */
    public List<OptionDataListInfo> hl;
    /**
     *
     */
    public List<OptionInfo> ol;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.hid);
        dest.writeString(this.hps);
        dest.writeInt(this.hpono);
        dest.writeInt(this.hs);
        dest.writeInt(this.hmt);
        dest.writeInt(this.hn);
        dest.writeString(this.hmed);
        dest.writeString(this.hmgt);
        dest.writeInt(this.hids);
        dest.writeString(this.hlid);
        dest.writeInt(this.hmm);
        dest.writeString(this.hpid);
        dest.writeString(this.hpn);
        dest.writeInt(this.hpon);
        dest.writeInt(this.hpt);
        dest.writeString(this.hshow);
        dest.writeString(this.hsw);
        dest.writeString(this.hton);
        dest.writeLong(this.mid);
        dest.writeTypedList(this.hl);
        dest.writeTypedList(this.ol);
    }

    public void readFromParcel(Parcel source) {
        this.hid = source.readLong();
        this.hps = source.readString();
        this.hpono = source.readInt();
        this.hs = source.readInt();
        this.hmt = source.readInt();
        this.hn = source.readInt();
        this.hmed = source.readString();
        this.hmgt = source.readString();
        this.hids = source.readInt();
        this.hlid = source.readString();
        this.hmm = source.readInt();
        this.hpid = source.readString();
        this.hpn = source.readString();
        this.hpon = source.readInt();
        this.hpt = source.readInt();
        this.hshow = source.readString();
        this.hsw = source.readString();
        this.hton = source.readString();
        this.mid = source.readLong();
        this.hl = source.createTypedArrayList(OptionDataListInfo.CREATOR);
        this.ol = source.createTypedArrayList(OptionInfo.CREATOR);
    }

    public PlayTypeInfo() {
    }

    protected PlayTypeInfo(Parcel in) {
        this.hid = in.readLong();
        this.hps = in.readString();
        this.hpono = in.readInt();
        this.hs = in.readInt();
        this.hmt = in.readInt();
        this.hn = in.readInt();
        this.hmed = in.readString();
        this.hmgt = in.readString();
        this.hids = in.readInt();
        this.hlid = in.readString();
        this.hmm = in.readInt();
        this.hpid = in.readString();
        this.hpn = in.readString();
        this.hpon = in.readInt();
        this.hpt = in.readInt();
        this.hshow = in.readString();
        this.hsw = in.readString();
        this.hton = in.readString();
        this.mid = in.readLong();
        this.hl = in.createTypedArrayList(OptionDataListInfo.CREATOR);
        this.ol = in.createTypedArrayList(OptionInfo.CREATOR);
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

    @Override
    public int compareTo(Object o) {
        if(this.hpon > ((PlayTypeInfo) o).hpon){
            return 1;
        }else if (this.hpon < ((PlayTypeInfo) o).hpon){
            return  -1;
        }else {
            return 0;
        }
    }
}
