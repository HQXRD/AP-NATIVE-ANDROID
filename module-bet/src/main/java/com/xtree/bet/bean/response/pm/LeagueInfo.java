package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class LeagueInfo implements BaseBean {
    /**
     * 联赛ID
     */
    public long id;
    /**
     * 该联赛开售的赛事统计
     */
    public int num;
    /**
     * 该联赛正在滚球的赛事个数
     */
    public int lt;
    /**
     * 联赛名称
     */
    public String nameText;
    /**
     * 联赛等级，可用于联赛排序，值越小，联赛等级越高
     */
    public int or;
    /**
     * 联赛图标地址
     */
    public String picUrlthumb;
    /**
     * 联赛的运动种类id
     */
    public int sportId;
    /**
     * 区域id
     */
    public int regionId;
    /**
     * 区域名称
     */
    public String rnm; 
    /**
     * 是否热门
     */
    public boolean hot;
    /**
     * 热门状态
     */
    public int hotStatus;
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
        dest.writeInt(this.num);
        dest.writeInt(this.lt);
        dest.writeString(this.nameText);
        dest.writeLong(this.id);
        dest.writeInt(this.or);
        dest.writeString(this.picUrlthumb);
        dest.writeInt(this.sportId);
        dest.writeInt(this.regionId);
        dest.writeString(this.rnm);
        dest.writeByte(this.hot ? (byte) 1 : (byte) 0);
        dest.writeString(this.slid);
    }

    public void readFromParcel(Parcel source) {
        this.num = source.readInt();
        this.lt = source.readInt();
        this.nameText = source.readString();
        this.id = source.readInt();
        this.or = source.readInt();
        this.picUrlthumb = source.readString();
        this.sportId = source.readInt();
        this.regionId = source.readInt();
        this.rnm = source.readString();
        this.hot = source.readByte() != 0;
        this.slid = source.readString();
    }

    public LeagueInfo() {
    }

    protected LeagueInfo(Parcel in) {
        this.num = in.readInt();
        this.lt = in.readInt();
        this.nameText = in.readString();
        this.id = in.readInt();
        this.or = in.readInt();
        this.picUrlthumb = in.readString();
        this.sportId = in.readInt();
        this.regionId = in.readInt();
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
