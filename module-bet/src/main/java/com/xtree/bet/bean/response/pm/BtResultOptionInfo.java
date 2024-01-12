package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

public class BtResultOptionInfo implements BaseBean {
    /**
     * addition :
     * batchNum :
     * betMoney : 500.0
     * marketType : EU
     * marketValues :
     * matchDay :
     * matchDetailType : null
     * matchInfo : 维冈竞技 v 曼联
     * matchName : 英格兰足总杯
     * matchStatus : 0
     * matchType : 1
     * maxWinMoney : 4.32
     * oddsType : 2
     * oddsValues : 1.18
     * orderNo : 5114101144335604
     * orderStatusCode : null
     * playName : 全场独赢
     * playOptionName : 曼联
     * playOptionsId : 141127761003422046
     * preOrderDetailStatus : null
     * scoreBenchmark :
     * teamName : 维冈竞技
     */

    /**
     * 附加金额
     */
    public String addition;
    public String batchNum;
    /**
     * 投注金额
     */
    public String betMoney;
    /**
     * 盘口类型
     */
    public String marketType;
    /**
     * 盘口值
     */
    public String marketValues;
    /**
     *
     */
    public String matchDay;
    /**
     * 1：早盘，2：滚球，3：冠军，仅电竞赛事使用
     */
    public int matchDetailType;
    /**
     * 对阵信息
     */
    public String matchInfo;
    public String matchName;
    /**
     * 赛事状态 0：未开赛，1：进行中，4：结束
     */
    public int matchStatus;
    /**
     * 赛事类型 1：早盘赛事，2：滚球盘赛事，3：冠军盘赛事， 4，虚拟赛事，5：电竞赛事
     */
    public int matchType;
    /**
     * 对打可盈金额
     */
    public String maxWinMoney;
    /**
     * 投注项类型，仅虚拟赛事使用
     */
    public String oddsType;
    /**
     * 赔率（不区分欧赔和港赔，取决于marketType的值， 前端对接直接使用这个值即可，无需再次计算）
     */
    public String oddsValues;
    /**
     * 订单号
     */
    public String orderNo;
    /**
     * 订单状态码 0：投注失败，1：接单，2：确认中
     */
    public String orderStatusCode;
    /**
     * 玩法名称
     */
    public String playName;
    /**
     * 投注项名称
     */
    public String playOptionName;
    /**
     * 投注项id
     */
    public String playOptionsId;
    public int preOrderDetailStatus;
    /**
     * 基准分
     */
    public String scoreBenchmark;
    /**
     * 团队名称
     */
    public String teamName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addition);
        dest.writeString(this.batchNum);
        dest.writeString(this.betMoney);
        dest.writeString(this.marketType);
        dest.writeString(this.marketValues);
        dest.writeString(this.matchDay);
        dest.writeInt(this.matchDetailType);
        dest.writeString(this.matchInfo);
        dest.writeString(this.matchName);
        dest.writeInt(this.matchStatus);
        dest.writeInt(this.matchType);
        dest.writeString(this.maxWinMoney);
        dest.writeString(this.oddsType);
        dest.writeString(this.oddsValues);
        dest.writeString(this.orderNo);
        dest.writeString(this.orderStatusCode);
        dest.writeString(this.playName);
        dest.writeString(this.playOptionName);
        dest.writeString(this.playOptionsId);
        dest.writeInt(this.preOrderDetailStatus);
        dest.writeString(this.scoreBenchmark);
        dest.writeString(this.teamName);
    }

    public void readFromParcel(Parcel source) {
        this.addition = source.readString();
        this.batchNum = source.readString();
        this.betMoney = source.readString();
        this.marketType = source.readString();
        this.marketValues = source.readString();
        this.matchDay = source.readString();
        this.matchDetailType = source.readInt();
        this.matchInfo = source.readString();
        this.matchName = source.readString();
        this.matchStatus = source.readInt();
        this.matchType = source.readInt();
        this.maxWinMoney = source.readString();
        this.oddsType = source.readString();
        this.oddsValues = source.readString();
        this.orderNo = source.readString();
        this.orderStatusCode = source.readString();
        this.playName = source.readString();
        this.playOptionName = source.readString();
        this.playOptionsId = source.readString();
        this.preOrderDetailStatus = source.readInt();
        this.scoreBenchmark = source.readString();
        this.teamName = source.readString();
    }

    public BtResultOptionInfo() {
    }

    protected BtResultOptionInfo(Parcel in) {
        this.addition = in.readString();
        this.batchNum = in.readString();
        this.betMoney = in.readString();
        this.marketType = in.readString();
        this.marketValues = in.readString();
        this.matchDay = in.readString();
        this.matchDetailType = in.readInt();
        this.matchInfo = in.readString();
        this.matchName = in.readString();
        this.matchStatus = in.readInt();
        this.matchType = in.readInt();
        this.maxWinMoney = in.readString();
        this.oddsType = in.readString();
        this.oddsValues = in.readString();
        this.orderNo = in.readString();
        this.orderStatusCode = in.readString();
        this.playName = in.readString();
        this.playOptionName = in.readString();
        this.playOptionsId = in.readString();
        this.preOrderDetailStatus = in.readInt();
        this.scoreBenchmark = in.readString();
        this.teamName = in.readString();
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
