package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class HotLeagueInfo implements BaseBean {

    public List<String> fbxc_popular_leagues; // FB热门联赛ID
    public List<String> obg_popular_leagues; // PM热门联赛ID

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.fbxc_popular_leagues);
        dest.writeStringList(this.obg_popular_leagues);
    }

    public void readFromParcel(Parcel source) {
        this.fbxc_popular_leagues = source.createStringArrayList();
        this.obg_popular_leagues = source.createStringArrayList();
    }

    public HotLeagueInfo() {
    }

    protected HotLeagueInfo(Parcel in) {
        this.fbxc_popular_leagues = in.createStringArrayList();
        this.obg_popular_leagues = in.createStringArrayList();
    }

    public static final Creator<HotLeagueInfo> CREATOR = new Creator<HotLeagueInfo>() {
        @Override
        public HotLeagueInfo createFromParcel(Parcel source) {
            return new HotLeagueInfo(source);
        }

        @Override
        public HotLeagueInfo[] newArray(int size) {
            return new HotLeagueInfo[size];
        }
    };
}
