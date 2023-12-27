package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class ScoreboardInfo implements BaseBean {
    /**
     * 冰球，主队当前被罚下场的人数
     */
    public int ihs;
    /**
     * 冰球，客队当前被罚下场的人数
     */
    public int ias;
    /**
     * 橄榄球，进攻方
     */
    public String rp;
    /**
     * 橄榄球，第几次进攻
     */
    public int rd;
    /**
     * 橄榄球，本次进攻剩余的码数
     */
    public int ry;
    /**
     * 斯诺克，谁在打球
     */
    public String sv;
    /**
     * 斯诺克，剩余红球的数量
     */
    public int srr;
    /**
     * 棒球，好球数量
     */
    public int bs;
    /**
     * 棒球，坏球数量
     */
    public int bb;
    /**
     * 棒球，出局人数
     */
    public int bo;
    /**
     * 棒球，上垒，数字表示三个垒的位置，0表示没有人，1表示有人
     */
    public String bbs;
    /**
     * 手球，主队当前暂停的球员数量
     */
    public int hhs;
    /**
     * 手球，客队当前暂停的球员数量
     */
    public int has;
    /**
     * 板球，回合
     */
    public int co;
    /**
     * 板球，球
     */
    public int cd;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ihs);
        dest.writeInt(this.ias);
        dest.writeString(this.rp);
        dest.writeInt(this.rd);
        dest.writeInt(this.ry);
        dest.writeString(this.sv);
        dest.writeInt(this.srr);
        dest.writeInt(this.bs);
        dest.writeInt(this.bb);
        dest.writeInt(this.bo);
        dest.writeString(this.bbs);
        dest.writeInt(this.hhs);
        dest.writeInt(this.has);
        dest.writeInt(this.co);
        dest.writeInt(this.cd);
    }

    public void readFromParcel(Parcel source) {
        this.ihs = source.readInt();
        this.ias = source.readInt();
        this.rp = source.readString();
        this.rd = source.readInt();
        this.ry = source.readInt();
        this.sv = source.readString();
        this.srr = source.readInt();
        this.bs = source.readInt();
        this.bb = source.readInt();
        this.bo = source.readInt();
        this.bbs = source.readString();
        this.hhs = source.readInt();
        this.has = source.readInt();
        this.co = source.readInt();
        this.cd = source.readInt();
    }

    public ScoreboardInfo() {
    }

    protected ScoreboardInfo(Parcel in) {
        this.ihs = in.readInt();
        this.ias = in.readInt();
        this.rp = in.readString();
        this.rd = in.readInt();
        this.ry = in.readInt();
        this.sv = in.readString();
        this.srr = in.readInt();
        this.bs = in.readInt();
        this.bb = in.readInt();
        this.bo = in.readInt();
        this.bbs = in.readString();
        this.hhs = in.readInt();
        this.has = in.readInt();
        this.co = in.readInt();
        this.cd = in.readInt();
    }

    public static final Creator<ScoreboardInfo> CREATOR = new Creator<ScoreboardInfo>() {
        @Override
        public ScoreboardInfo createFromParcel(Parcel source) {
            return new ScoreboardInfo(source);
        }

        @Override
        public ScoreboardInfo[] newArray(int size) {
            return new ScoreboardInfo[size];
        }
    };
}
