package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class LeagueInfo implements BaseBean {
    /**
     * 联赛ID
     */
    public long tournamentId;
    /**
     * 该联赛开售的赛事统计
     */
    public int num;
    /**
     * 联赛名称
     */
    public String nameText;
    /**
     * 联赛等级，可用于联赛排序，值越小，联赛等级越高
     */
    public int tournamentLevel;
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
    public String regionName;
    /**
     * 是否热门, 1-热门，0-非热门
     */
    public int hotStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.tournamentId);
        dest.writeInt(this.num);
        dest.writeString(this.nameText);
        dest.writeInt(this.tournamentLevel);
        dest.writeString(this.picUrlthumb);
        dest.writeInt(this.sportId);
        dest.writeInt(this.regionId);
        dest.writeString(this.regionName);
        dest.writeInt(this.hotStatus);
    }

    public void readFromParcel(Parcel source) {
        this.tournamentId = source.readLong();
        this.num = source.readInt();
        this.nameText = source.readString();
        this.tournamentLevel = source.readInt();
        this.picUrlthumb = source.readString();
        this.sportId = source.readInt();
        this.regionId = source.readInt();
        this.regionName = source.readString();
        this.hotStatus = source.readInt();
    }

    public LeagueInfo() {
    }

    protected LeagueInfo(Parcel in) {
        this.tournamentId = in.readLong();
        this.num = in.readInt();
        this.nameText = in.readString();
        this.tournamentLevel = in.readInt();
        this.picUrlthumb = in.readString();
        this.sportId = in.readInt();
        this.regionId = in.readInt();
        this.regionName = in.readString();
        this.hotStatus = in.readInt();
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
