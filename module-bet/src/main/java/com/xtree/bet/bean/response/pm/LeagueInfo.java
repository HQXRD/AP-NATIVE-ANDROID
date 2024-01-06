package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class LeagueInfo implements BaseBean {
    /**
     * 联赛ID
     */
    public long tid;
    /**
     * 该联赛开售的赛事统计
     */
    public int mt;
    /**
     * 该联赛正在滚球的赛事个数
     */
    public int lt;
    /**
     * 联赛名称
     */
    public String tn;
    /**
     * 联赛等级，可用于联赛排序，值越小，联赛等级越高
     */
    public int or;
    /**
     * 联赛图标地址
     */
    public String lurl;
    /**
     * 运动种类id , see enum: sports
     */
    public int sid;
    /**
     * 区域id
     */
    public int rid; 
    /**
     * 区域名称
     */
    public String rnm; 
    /**
     * 是否热门
     */
    public boolean hot;
    /**
     * 联赛分组
     */
    public String slid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mt);
        dest.writeInt(this.lt);
        dest.writeString(this.tn);
        dest.writeLong(this.tid);
        dest.writeInt(this.or);
        dest.writeString(this.lurl);
        dest.writeInt(this.sid);
        dest.writeInt(this.rid);
        dest.writeString(this.rnm);
        dest.writeByte(this.hot ? (byte) 1 : (byte) 0);
        dest.writeString(this.slid);
    }

    public void readFromParcel(Parcel source) {
        this.mt = source.readInt();
        this.lt = source.readInt();
        this.tn = source.readString();
        this.tid = source.readInt();
        this.or = source.readInt();
        this.lurl = source.readString();
        this.sid = source.readInt();
        this.rid = source.readInt();
        this.rnm = source.readString();
        this.hot = source.readByte() != 0;
        this.slid = source.readString();
    }

    public LeagueInfo() {
    }

    protected LeagueInfo(Parcel in) {
        this.mt = in.readInt();
        this.lt = in.readInt();
        this.tn = in.readString();
        this.tid = in.readInt();
        this.or = in.readInt();
        this.lurl = in.readString();
        this.sid = in.readInt();
        this.rid = in.readInt();
        this.rnm = in.readString();
        this.hot = in.readByte() != 0;
        this.slid = in.readString();
    }

    public static final Creator<LeagueInfo> CREATOR = new Creator<LeagueInfo>() {
        @Override
        public LeagueInfo createFromParcel(Parcel source) {
            return new LeagueInfo(source);
        }

        @Override
        public LeagueInfo[] newArray(int size) {
            return new LeagueInfo[size];
        }
    };
}
