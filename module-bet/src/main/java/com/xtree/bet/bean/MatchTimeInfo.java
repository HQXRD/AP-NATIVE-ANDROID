package com.xtree.bet.bean;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

/**
 * 比赛时钟信息，滚球走表信息
 */
public class MatchTimeInfo implements BaseBean {
    /**
     * 走表时间，以秒为单位，如250秒，客户端用秒去转换成时分秒时间
     */
    public int s;
    /**
     * 赛事阶段，如 足球上半场，篮球第一节等
     */
    public int pe;
    /**
     * 是否走表，true为走表，false为停表
     */
    public boolean r;
    /**
     * 走表类型 , see enum: clock_type
     */
    public int tp;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.s);
        dest.writeInt(this.pe);
        dest.writeByte(this.r ? (byte) 1 : (byte) 0);
        dest.writeInt(this.tp);
    }

    public void readFromParcel(Parcel source) {
        this.s = source.readInt();
        this.pe = source.readInt();
        this.r = source.readByte() != 0;
        this.tp = source.readInt();
    }

    public MatchTimeInfo() {
    }

    protected MatchTimeInfo(Parcel in) {
        this.s = in.readInt();
        this.pe = in.readInt();
        this.r = in.readByte() != 0;
        this.tp = in.readInt();
    }

    public static final Creator<MatchTimeInfo> CREATOR = new Creator<MatchTimeInfo>() {
        @Override
        public MatchTimeInfo createFromParcel(Parcel source) {
            return new MatchTimeInfo(source);
        }

        @Override
        public MatchTimeInfo[] newArray(int size) {
            return new MatchTimeInfo[size];
        }
    };
}
