package com.xtree.bet.bean;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 赛事统计
 */
public class StatisticalInfo implements BaseBean {
    /**
     * 赛事个数
     */
    public int tc;
    /**
     * 热门总数，包括竞彩赛事和热门联赛赛事
     */
    public int ht;
    /**
     * 所有赛事对应的不同类型的场次集合
     */
    public List<MatchTypeInfo> sl = new ArrayList<>();
    /**
     * 热门联赛下的赛事个数统计
     */
    public List<LeagueInfo> hls = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tc);
        dest.writeInt(this.ht);
        dest.writeTypedList(this.sl);
        dest.writeTypedList(this.hls);
    }

    public void readFromParcel(Parcel source) {
        this.tc = source.readInt();
        this.ht = source.readInt();
        this.sl = source.createTypedArrayList(MatchTypeInfo.CREATOR);
        this.hls = source.createTypedArrayList(LeagueInfo.CREATOR);
    }

    public StatisticalInfo() {
    }

    protected StatisticalInfo(Parcel in) {
        this.tc = in.readInt();
        this.ht = in.readInt();
        this.sl = in.createTypedArrayList(MatchTypeInfo.CREATOR);
        this.hls = in.createTypedArrayList(LeagueInfo.CREATOR);
    }

    public static final Creator<StatisticalInfo> CREATOR = new Creator<StatisticalInfo>() {
        @Override
        public StatisticalInfo createFromParcel(Parcel source) {
            return new StatisticalInfo(source);
        }

        @Override
        public StatisticalInfo[] newArray(int size) {
            return new StatisticalInfo[size];
        }
    };
}
