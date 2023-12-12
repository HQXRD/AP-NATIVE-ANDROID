package com.xtree.bet.bean;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 赛事统计
 */
public class MatchTypeStatisInfo implements BaseBean {
    /**
     * 运动ID
     */
    public int sid;
    /**
     * 统计赛事个数
     */
    public int c;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sid);
        dest.writeInt(this.c);
    }

    public void readFromParcel(Parcel source) {
        this.sid = source.readInt();
        this.c = source.readInt();
    }

    public MatchTypeStatisInfo() {
    }

    protected MatchTypeStatisInfo(Parcel in) {
        this.sid = in.readInt();
        this.c = in.readInt();
    }

    public static final Creator<MatchTypeStatisInfo> CREATOR = new Creator<MatchTypeStatisInfo>() {
        @Override
        public MatchTypeStatisInfo createFromParcel(Parcel source) {
            return new MatchTypeStatisInfo(source);
        }

        @Override
        public MatchTypeStatisInfo[] newArray(int size) {
            return new MatchTypeStatisInfo[size];
        }
    };
}
