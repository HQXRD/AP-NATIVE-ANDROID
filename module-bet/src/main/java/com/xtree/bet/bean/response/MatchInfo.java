package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class MatchInfo implements BaseBean {
    public int id;
    public List<ScoreInfo> nsg;
    /**
     * 玩法列表
     */
    public List<PlayTypeInfo> mg;
    /**
     * 单个赛事玩法总数
     */
    public int tms;
    /**
     * 盘口组标签集合 , see enum: market_tag
     */
    public List<String> tps;
    /**
     *  联赛信息
     */
    public LeagueInfo lg;
    /**
     *  球队信息
     */
    public List<TeamInfo> ts;
    /**
     * 比赛时钟信息，滚球走表信息
     */
    public MatchTimeInfo mc;
    /**
     * 赛事开赛时间
     */
    public long bt;
    /**
     * 赛事进行状态，返回赛事进行状态code，具体请对照枚举 , see enum: match_status
     */
    public int ms;
    /**
     * 赛制的场次、局数、节数
     */
    public int fid;
    /**
     * 赛制 , see enum: match_format
     */
    public int fmt;
    /**
     * 销售状态，1 开售，2 暂停，其他状态都不展示
     */
    public int ss;
    /**
     * 中立场标记，0 非中立场 ，1 中立场
     */
    public int ne;
    /**
     * 动画直接地址集合
     */
    public VideoInfo vs;
    /**
     * 动画直接地址集合
     */
    public List<String> as;
    /**
     * 运动ID
     */
    public int sid;
    /**
     * 主/客发球，1主队发球，2客队发球, see enum: serving_side
     */
    public int ssi;
    /**
     * 赛事辅助标记，如附加赛、季前赛等，具体请看枚举 , see enum: phase
     */
    public String mp;
    /**
     * 滚球赛事当前阶段标识：常规时间，加时赛，点球大战等
     */
    public int smt;
    /**
     * 赛事类型 1 冠军投注赛事，2 正常赛事 , see enum: match_type
     */
    public int ty;
    /**
     * 冠军赛事联赛赛季，返回赛季字符串，如：2019年
     */
    public String ye;
    /**
     * 比分板
     */
    public ScoreboardInfo sb;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.nsg);
        dest.writeTypedList(this.mg);
        dest.writeInt(this.tms);
        dest.writeStringList(this.tps);
        dest.writeParcelable(this.lg, flags);
        dest.writeTypedList(this.ts);
        dest.writeParcelable(this.mc, flags);
        dest.writeLong(this.bt);
        dest.writeInt(this.ms);
        dest.writeInt(this.fid);
        dest.writeInt(this.fmt);
        dest.writeInt(this.ss);
        dest.writeInt(this.ne);
        dest.writeParcelable(this.vs, flags);
        dest.writeStringList(this.as);
        dest.writeInt(this.sid);
        dest.writeInt(this.ssi);
        dest.writeString(this.mp);
        dest.writeInt(this.smt);
        dest.writeInt(this.ty);
        dest.writeString(this.ye);
        dest.writeParcelable(this.sb, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.nsg = source.createTypedArrayList(ScoreInfo.CREATOR);
        this.mg = source.createTypedArrayList(PlayTypeInfo.CREATOR);
        this.tms = source.readInt();
        this.tps = source.createStringArrayList();
        this.lg = source.readParcelable(LeagueInfo.class.getClassLoader());
        this.ts = source.createTypedArrayList(TeamInfo.CREATOR);
        this.mc = source.readParcelable(MatchTimeInfo.class.getClassLoader());
        this.bt = source.readLong();
        this.ms = source.readInt();
        this.fid = source.readInt();
        this.fmt = source.readInt();
        this.ss = source.readInt();
        this.ne = source.readInt();
        this.vs = source.readParcelable(VideoInfo.class.getClassLoader());
        this.as = source.createStringArrayList();
        this.sid = source.readInt();
        this.ssi = source.readInt();
        this.mp = source.readString();
        this.smt = source.readInt();
        this.ty = source.readInt();
        this.ye = source.readString();
        this.sb = source.readParcelable(ScoreboardInfo.class.getClassLoader());
    }

    public MatchInfo() {
    }

    protected MatchInfo(Parcel in) {
        this.id = in.readInt();
        this.nsg = in.createTypedArrayList(ScoreInfo.CREATOR);
        this.mg = in.createTypedArrayList(PlayTypeInfo.CREATOR);
        this.tms = in.readInt();
        this.tps = in.createStringArrayList();
        this.lg = in.readParcelable(LeagueInfo.class.getClassLoader());
        this.ts = in.createTypedArrayList(TeamInfo.CREATOR);
        this.mc = in.readParcelable(MatchTimeInfo.class.getClassLoader());
        this.bt = in.readLong();
        this.ms = in.readInt();
        this.fid = in.readInt();
        this.fmt = in.readInt();
        this.ss = in.readInt();
        this.ne = in.readInt();
        this.vs = in.readParcelable(VideoInfo.class.getClassLoader());
        this.as = in.createStringArrayList();
        this.sid = in.readInt();
        this.ssi = in.readInt();
        this.mp = in.readString();
        this.smt = in.readInt();
        this.ty = in.readInt();
        this.ye = in.readString();
        this.sb = in.readParcelable(ScoreboardInfo.class.getClassLoader());
    }

    public static final Creator<MatchInfo> CREATOR = new Creator<MatchInfo>() {
        @Override
        public MatchInfo createFromParcel(Parcel source) {
            return new MatchInfo(source);
        }

        @Override
        public MatchInfo[] newArray(int size) {
            return new MatchInfo[size];
        }
    };
}
