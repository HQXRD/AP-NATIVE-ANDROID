package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 球队信息
 */
public class TeamInfo implements BaseBean {
    /**
     * 球队名称
     */
    public String na;
    /**
     * 球队ID
     */
    public int id;
    /**
     * 球队图标地址
     */
    public String lurl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.na);
        dest.writeInt(this.id);
        dest.writeString(this.lurl);
    }

    public void readFromParcel(Parcel source) {
        this.na = source.readString();
        this.id = source.readInt();
        this.lurl = source.readString();
    }

    public TeamInfo() {
    }

    protected TeamInfo(Parcel in) {
        this.na = in.readString();
        this.id = in.readInt();
        this.lurl = in.readString();
    }

    public static final Creator<TeamInfo> CREATOR = new Creator<TeamInfo>() {
        @Override
        public TeamInfo createFromParcel(Parcel source) {
            return new TeamInfo(source);
        }

        @Override
        public TeamInfo[] newArray(int size) {
            return new TeamInfo[size];
        }
    };
}
