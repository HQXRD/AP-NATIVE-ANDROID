package com.xtree.bet.bean.response;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

public class BtResultOptionInfo implements BaseBean {
    /**
     * 盘口id
     */
    public String mid;
    /**
     * 欧式赔率
     */
    public String od;
    /**
     * 赔率类型
     */
    public int of;
    /**
     * 下注赔率
     */
    public String bod;
    /**
     * 第三方备注
     */
    public String tr;
    /**
     * 下单时三方带的订单ID
     */
    public String rid;
    /**
     * 运动ID
     */
    public int sid;
    /**
     * 比赛名称
     */
    public String mn;
    /**
     * 联赛名称
     */
    public String ln;
    /**
     * 比赛开赛时间，13位数字时间戳
     */
    public long bt;
    /**
     * 玩法阶段code，如 上半场、全场等 , see enum: period
     */
    public int pe;
    /**
     * 玩法类型code，如 大小球、让球 , see enum: market_type
     */
    public int mty;
    /**
     * 投注选项完整名称
     */
    public String on;
    /**
     * 投注选项名称(全名or简名，目前为全名)
     */
    public String onm;
    /**
     * 是否滚球玩法 , see enum: in_play_enum
     */
    public String ip;
    /**
     * 比赛球队信息
     */
    public List<TeamInfo> te;
    /**
     * 订单结算时比分，部分玩法没有对应比分
     */
    public String rs;
    /**
     * 选项结算结果，0未结算，2走水，3全输，4全赢，5赢半，6输半，7玩法取消 , see enum: outcome
     */
    public int sr;
    /**
     * 展示赔率，按下单时赔率类型（of字段）展示，如下单时用港盘，港盘赔率是0.82，该字段就是0.82
     */
    public String bo;
    /**
     * 带线（球头）的玩法对应的值，如 大小球 2.5，该字段是"2.5"
     */
    public String li;
    /**
     * 备注
     */
    public String rmk;
    /**
     * 扩展信息，主要是亚盘玩法的比分，如 1-1
     */
    public String re;
    /**
     * 选项的玩法ID
     */
    public int mrid;
    /**
     * 选项类型 , see enum: selection_type
     */
    public int ty;
    /**
     * 玩法名称 marketType+period
     */
    public String mgn;
    /**
     * 取消原因，如果订单被取消，会返回取消原因
     */
    public String cr;
    /**
     * 赛事类型 1 冠军投注赛事，2 正常赛事 , see enum: match_type
     */
    public int mtp;
    /**
     * 赛事状态 , see enum: match_status
     */
    public int ms;
    /**
     * 当前比分
     */
    public List<Integer> scs;
    /**
     * 比赛时钟信息，滚球走表信息
     */
    public MatchTimeInfo mc;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mid);
        dest.writeString(this.od);
        dest.writeInt(this.of);
        dest.writeString(this.bod);
        dest.writeString(this.tr);
        dest.writeString(this.rid);
    }

    public void readFromParcel(Parcel source) {
        this.mid = source.readString();
        this.od = source.readString();
        this.of = source.readInt();
        this.bod = source.readString();
        this.tr = source.readString();
        this.rid = source.readString();
    }

    public BtResultOptionInfo() {
    }

    protected BtResultOptionInfo(Parcel in) {
        this.mid = in.readString();
        this.od = in.readString();
        this.of = in.readInt();
        this.bod = in.readString();
        this.tr = in.readString();
        this.rid = in.readString();
    }

    public static final Creator<BtResultOptionInfo> CREATOR = new Creator<BtResultOptionInfo>() {
        @Override
        public BtResultOptionInfo createFromParcel(Parcel source) {
            return new BtResultOptionInfo(source);
        }

        @Override
        public BtResultOptionInfo[] newArray(int size) {
            return new BtResultOptionInfo[size];
        }
    };
}
