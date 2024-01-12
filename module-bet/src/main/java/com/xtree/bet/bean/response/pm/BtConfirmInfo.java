package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.List;

/**
 * 最新盘口数据
 */
public class BtConfirmInfo implements BaseBean {

    /**
     * away : 科斯莫斯M
     * home : 建造师M
     * id : 143009411205894307
     * marketOddsList : [{"id":"143351231151851134","oddsStatus":2,"oddsType":"2","oddsValue":0,"playOptions":"0"}]
     * marketType : null
     * marketValue : 0
     * matchHandicapStatus : 0
     * matchInfoId : 3048435
     * matchOver : 0
     * matchPeriodId : 7
     * matchStatus : 1
     * pendingOrderStatus : 0
     * placeNum : 1
     * playId : 4
     * playName : 全场让球
     * score : S1|3:0
     * status : 2
     * tournamentName :
     */

    /**
     * 客队名称
     */
    public String away;
    /**
     * 主队名称
     */
    public String home;
    /**
     * 盘口id(对应hid)
     */
    public String id;
    /**
     * 盘口类型
     */
    public String marketType;
    /**
     * 盘口值(对应hv)
     */
    public String marketValue;
    /**
     * 赛事级别开关 0：active 开盘，1：suspended 封盘，2：deactivated关盘，11：锁盘状态(对应mhs）
     */
    public int matchHandicapStatus;
    /**
     * 赛事id(对应mid)
     */
    public String matchInfoId;
    public String marketId;
    /**
     * 赛事结束状态 0：未结束，1：结束(对应mo)
     */
    public int matchOver;
    /**
     * 比赛阶段:赛事阶段比对
     */
    public int matchPeriodId;
    /**
     * 赛事状态 0：未开赛，1：进行中，4：结束(对应ms)
     */
    public int matchStatus;
    /**
     * 是否支持预约投注(1:支持 0或null:不支持)
     */
    public int pendingOrderStatus;
    /**
     * 坑位(对应hn)
     */
    public int placeNum;
    /**
     * 玩法
     */
    public int playId;
    /**
     * 玩法名称
     */
    public String playName;
    /**
     * 盘口基准分
     */
    public String score;
    /**
     * 盘口状态 0：active 开盘，1：suspended 封盘，2：deactivated 关盘，11：锁盘状态(对应hs）
     */
    public int status;
    /**
     *
     */
    public String tournamentName;
    /**
     * 投注项集合
     */
    public List<BtConfirmOptionInfo> marketOddsList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.away);
        dest.writeString(this.home);
        dest.writeString(this.id);
        dest.writeString(this.marketType);
        dest.writeString(this.marketValue);
        dest.writeInt(this.matchHandicapStatus);
        dest.writeString(this.matchInfoId);
        dest.writeInt(this.matchOver);
        dest.writeInt(this.matchPeriodId);
        dest.writeInt(this.matchStatus);
        dest.writeInt(this.pendingOrderStatus);
        dest.writeInt(this.placeNum);
        dest.writeInt(this.playId);
        dest.writeString(this.playName);
        dest.writeString(this.score);
        dest.writeInt(this.status);
        dest.writeString(this.tournamentName);
        dest.writeTypedList(this.marketOddsList);
    }

    public void readFromParcel(Parcel source) {
        this.away = source.readString();
        this.home = source.readString();
        this.id = source.readString();
        this.marketType = source.readString();
        this.marketValue = source.readString();
        this.matchHandicapStatus = source.readInt();
        this.matchInfoId = source.readString();
        this.matchOver = source.readInt();
        this.matchPeriodId = source.readInt();
        this.matchStatus = source.readInt();
        this.pendingOrderStatus = source.readInt();
        this.placeNum = source.readInt();
        this.playId = source.readInt();
        this.playName = source.readString();
        this.score = source.readString();
        this.status = source.readInt();
        this.tournamentName = source.readString();
        this.marketOddsList = source.createTypedArrayList(BtConfirmOptionInfo.CREATOR);
    }

    public BtConfirmInfo() {
    }

    protected BtConfirmInfo(Parcel in) {
        this.away = in.readString();
        this.home = in.readString();
        this.id = in.readString();
        this.marketType = in.readString();
        this.marketValue = in.readString();
        this.matchHandicapStatus = in.readInt();
        this.matchInfoId = in.readString();
        this.matchOver = in.readInt();
        this.matchPeriodId = in.readInt();
        this.matchStatus = in.readInt();
        this.pendingOrderStatus = in.readInt();
        this.placeNum = in.readInt();
        this.playId = in.readInt();
        this.playName = in.readString();
        this.score = in.readString();
        this.status = in.readInt();
        this.tournamentName = in.readString();
        this.marketOddsList = in.createTypedArrayList(BtConfirmOptionInfo.CREATOR);
    }

    public static final Creator<BtConfirmInfo> CREATOR = new Creator<BtConfirmInfo>() {
        @Override
        public BtConfirmInfo createFromParcel(Parcel source) {
            return new BtConfirmInfo(source);
        }

        @Override
        public BtConfirmInfo[] newArray(int size) {
            return new BtConfirmInfo[size];
        }
    };
}
