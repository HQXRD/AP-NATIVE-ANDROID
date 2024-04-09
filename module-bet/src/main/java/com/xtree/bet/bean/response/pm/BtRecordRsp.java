package com.xtree.bet.bean.response.pm;

import android.os.Parcel;

import com.xtree.base.vo.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class BtRecordRsp implements BaseBean {

    /**
     * 总投注额
     */
    public String betTotalAmount;
    /**
     * 每页显示大小
     */
    public String current;
    public boolean hasNext;
    public boolean hasPrevious;
    public boolean optimizeCountSql;
    /**
     * 总页数
     */
    public String pages;
    /**
     * 总盈利
     */
    public String profit;
    /**
     * 查询数量
     */
    public boolean searchCount;
    /**
     * 每页显示大小
     */
    public String size;
    /**
     * 总数条数
     */
    public String total;
    public List<RecordsBean> records;

    public static class RecordsBean implements BaseBean{

        /**
         * 印度尼西亚、马来盘附加金额， 如果大于0需要商户在该字段前加“+”号 (针对印度尼西亚盘和马来盘做的特殊处理 实际付款金额-投注金额)
         */
        public int addition;
        /**
         * 返还金额(orderAmountTotal+profitAmount)
         */
        public Double backAmount;
        /**
         * 投注时间(时间戳)
         */
        public String betTime;
        /**
         * 投注时间(YYYY-MM-DD)
         */
        public String betTimeStr;
        /**
         * 0：关闭提前结算，1：开启提前结算
         */
        public boolean enablePreSettle;
        public String langCode;
        public int managerCode;
        public String marketType;
        /**
         * 最高可赢
         */
        public Double maxWinAmount;
        /**
         * 投注总金额
         */
        public Double orderAmountTotal;
        /**
         * 订单号
         */
        public String orderNo;
        /**
         * 订单状态 0：未结算，1：已结算，2：注单取消，3：确认中，4：投注失败
         */
        public String orderStatus;
        /**
         * 输赢结算状态 2：走水，3：输，4：赢，5：半赢，6：半输， 7：赛事取消，8：赛事延期
         */
        public int outcome;
        /**
         * 已提前结算的金额
         */
        public Double preBetAmount;
        /**
         * 提前结算部分或者全额区分 1：部分，2：全额，0或者null：没有提前结算 3：部分提前结算取消，4：全额提取结算取消
         */
        public int preSettle;
        /**
         * 提前结算最高可盈
         */
        public Double preSettleMaxWin;
        /**
         * 净盈利(输赢多少钱)
         */
        public Double profitAmount;
        /**
         * 串关注数
         */
        public int seriesSum;
        /**
         * 串关类型(单关、串关)
         */
        public String seriesType;
        /**
         * 串关值
         */
        public String seriesValue;
        /**
         * 4：全额提前结算；5：部分提前结算
         */
        public int settleType;
        public List<DetailBean> detailList;
        public BtCashOutPriceInfo pr;

        public static class DetailBean implements BaseBean{

            public String batchNo;
            /**
             * 开始时间
             */
            public String beginTime;
            /**
             * 投注类型id(对应上游的投注项id)
             */
            public String playOptionId;
            /**
             * 投注项结算结果 0：无结果，2：走水，3：输，4：赢，5：赢一半， 6：输一半，7：赛事取消，8：赛事延期， 11：比赛延迟，12：比赛中断，13：未知， 15：比赛放弃，16：异常盘口
             */
            public int betResult;
            /**
             * 注单状态 0：未结算，1：已结算，2：结算异常，3：注单取消 , 其他状态忽略
             */
            public int betStatus;
            /**
             * 取消类型 1：比赛取消，2：比赛延期，3：比赛中断，4：比赛重赛，5：比赛腰斩，6：比赛放弃，17：赛事提前，20：比赛延迟
             */
            public int cancelType;
            public String marketId;
            /**
             * 盘口类型
             */
            public String marketType;
            /**
             * 盘口值
             */
            public String marketValue;
            public String matchDay;
            public String matchId;
            /**
             * 对阵信息
             */
            public String matchInfo;
            /**
             * 投注联赛
             */
            public String matchName;
            /**
             * 赛事类型 1：早盘赛事，2：滚球盘赛事，3：冠军盘赛事
             */
            public int matchType;
            /**
             * 最终赔率
             */
            public String oddFinally;
            /**
             * 投注赔率
             */
            public Double oddsValue;
            public String outrightYear;
            /**
             * 玩法id
             */
            public int playId;
            /**
             * 玩法名称
             */
            public String playName;
            public String playOptionsId;
            /**
             * 基准比分
             */
            public String scoreBenchmark;
            /**
             * 结算比分(可为空)
             */
            public String settleScore;
            /**
             * 体育种类id
             */
            public int sportId;
            /**
             * 体育种类名称
             */
            public String sportName;
            public String tournamentId;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.batchNo);
                dest.writeString(this.beginTime);
                dest.writeString(this.playOptionId);
                dest.writeInt(this.betResult);
                dest.writeInt(this.betStatus);
                dest.writeInt(this.cancelType);
                dest.writeString(this.marketId);
                dest.writeString(this.marketType);
                dest.writeString(this.marketValue);
                dest.writeString(this.matchDay);
                dest.writeString(this.matchId);
                dest.writeString(this.matchInfo);
                dest.writeString(this.matchName);
                dest.writeInt(this.matchType);
                dest.writeString(this.oddFinally);
                dest.writeValue(this.oddsValue);
                dest.writeString(this.outrightYear);
                dest.writeInt(this.playId);
                dest.writeString(this.playName);
                dest.writeString(this.playOptionsId);
                dest.writeString(this.scoreBenchmark);
                dest.writeString(this.settleScore);
                dest.writeInt(this.sportId);
                dest.writeString(this.sportName);
                dest.writeString(this.tournamentId);
            }

            public void readFromParcel(Parcel source) {
                this.batchNo = source.readString();
                this.beginTime = source.readString();
                this.playOptionId = source.readString();
                this.betResult = source.readInt();
                this.betStatus = source.readInt();
                this.cancelType = source.readInt();
                this.marketId = source.readString();
                this.marketType = source.readString();
                this.marketValue = source.readString();
                this.matchDay = source.readString();
                this.matchId = source.readString();
                this.matchInfo = source.readString();
                this.matchName = source.readString();
                this.matchType = source.readInt();
                this.oddFinally = source.readString();
                this.oddsValue = (Double) source.readValue(Double.class.getClassLoader());
                this.outrightYear = source.readString();
                this.playId = source.readInt();
                this.playName = source.readString();
                this.playOptionsId = source.readString();
                this.scoreBenchmark = source.readString();
                this.settleScore = source.readString();
                this.sportId = source.readInt();
                this.sportName = source.readString();
                this.tournamentId = source.readString();
            }

            public DetailBean() {
            }

            protected DetailBean(Parcel in) {
                this.batchNo = in.readString();
                this.beginTime = in.readString();
                this.playOptionId = in.readString();
                this.betResult = in.readInt();
                this.betStatus = in.readInt();
                this.cancelType = in.readInt();
                this.marketId = in.readString();
                this.marketType = in.readString();
                this.marketValue = in.readString();
                this.matchDay = in.readString();
                this.matchId = in.readString();
                this.matchInfo = in.readString();
                this.matchName = in.readString();
                this.matchType = in.readInt();
                this.oddFinally = in.readString();
                this.oddsValue = (Double) in.readValue(Double.class.getClassLoader());
                this.outrightYear = in.readString();
                this.playId = in.readInt();
                this.playName = in.readString();
                this.playOptionsId = in.readString();
                this.scoreBenchmark = in.readString();
                this.settleScore = in.readString();
                this.sportId = in.readInt();
                this.sportName = in.readString();
                this.tournamentId = in.readString();
            }

            public static final Creator<DetailBean> CREATOR = new Creator<DetailBean>() {
                @Override
                public DetailBean createFromParcel(Parcel source) {
                    return new DetailBean(source);
                }

                @Override
                public DetailBean[] newArray(int size) {
                    return new DetailBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.addition);
            dest.writeValue(this.backAmount);
            dest.writeString(this.betTime);
            dest.writeString(this.betTimeStr);
            dest.writeByte(this.enablePreSettle ? (byte) 1 : (byte) 0);
            dest.writeString(this.langCode);
            dest.writeInt(this.managerCode);
            dest.writeString(this.marketType);
            dest.writeValue(this.maxWinAmount);
            dest.writeValue(this.orderAmountTotal);
            dest.writeString(this.orderNo);
            dest.writeString(this.orderStatus);
            dest.writeInt(this.outcome);
            dest.writeValue(this.preBetAmount);
            dest.writeInt(this.preSettle);
            dest.writeValue(this.preSettleMaxWin);
            dest.writeValue(this.profitAmount);
            dest.writeInt(this.seriesSum);
            dest.writeString(this.seriesType);
            dest.writeString(this.seriesValue);
            dest.writeInt(this.settleType);
            dest.writeList(this.detailList);
        }

        public void readFromParcel(Parcel source) {
            this.addition = source.readInt();
            this.backAmount = (Double) source.readValue(Double.class.getClassLoader());
            this.betTime = source.readString();
            this.betTimeStr = source.readString();
            this.enablePreSettle = source.readByte() != 0;
            this.langCode = source.readString();
            this.managerCode = source.readInt();
            this.marketType = source.readString();
            this.maxWinAmount = (Double) source.readValue(Double.class.getClassLoader());
            this.orderAmountTotal = (Double) source.readValue(Double.class.getClassLoader());
            this.orderNo = source.readString();
            this.orderStatus = source.readString();
            this.outcome = source.readInt();
            this.preBetAmount = (Double) source.readValue(Double.class.getClassLoader());
            this.preSettle = source.readInt();
            this.preSettleMaxWin = (Double) source.readValue(Double.class.getClassLoader());
            this.profitAmount = (Double) source.readValue(Double.class.getClassLoader());
            this.seriesSum = source.readInt();
            this.seriesType = source.readString();
            this.seriesValue = source.readString();
            this.settleType = source.readInt();
            this.detailList = new ArrayList<DetailBean>();
            source.readList(this.detailList, DetailBean.class.getClassLoader());
        }

        public RecordsBean() {
        }

        protected RecordsBean(Parcel in) {
            this.addition = in.readInt();
            this.backAmount = (Double) in.readValue(Double.class.getClassLoader());
            this.betTime = in.readString();
            this.betTimeStr = in.readString();
            this.enablePreSettle = in.readByte() != 0;
            this.langCode = in.readString();
            this.managerCode = in.readInt();
            this.marketType = in.readString();
            this.maxWinAmount = (Double) in.readValue(Double.class.getClassLoader());
            this.orderAmountTotal = (Double) in.readValue(Double.class.getClassLoader());
            this.orderNo = in.readString();
            this.orderStatus = in.readString();
            this.outcome = in.readInt();
            this.preBetAmount = (Double) in.readValue(Double.class.getClassLoader());
            this.preSettle = in.readInt();
            this.preSettleMaxWin = (Double) in.readValue(Double.class.getClassLoader());
            this.profitAmount = (Double) in.readValue(Double.class.getClassLoader());
            this.seriesSum = in.readInt();
            this.seriesType = in.readString();
            this.seriesValue = in.readString();
            this.settleType = in.readInt();
            this.detailList = new ArrayList<DetailBean>();
            in.readList(this.detailList, DetailBean.class.getClassLoader());
        }

        public static final Creator<RecordsBean> CREATOR = new Creator<RecordsBean>() {
            @Override
            public RecordsBean createFromParcel(Parcel source) {
                return new RecordsBean(source);
            }

            @Override
            public RecordsBean[] newArray(int size) {
                return new RecordsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.betTotalAmount);
        dest.writeString(this.current);
        dest.writeByte(this.hasNext ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasPrevious ? (byte) 1 : (byte) 0);
        dest.writeByte(this.optimizeCountSql ? (byte) 1 : (byte) 0);
        dest.writeString(this.pages);
        dest.writeString(this.profit);
        dest.writeByte(this.searchCount ? (byte) 1 : (byte) 0);
        dest.writeString(this.size);
        dest.writeString(this.total);
        dest.writeList(this.records);
    }

    public void readFromParcel(Parcel source) {
        this.betTotalAmount = source.readString();
        this.current = source.readString();
        this.hasNext = source.readByte() != 0;
        this.hasPrevious = source.readByte() != 0;
        this.optimizeCountSql = source.readByte() != 0;
        this.pages = source.readString();
        this.profit = source.readString();
        this.searchCount = source.readByte() != 0;
        this.size = source.readString();
        this.total = source.readString();
        this.records = new ArrayList<RecordsBean>();
        source.readList(this.records, RecordsBean.class.getClassLoader());
    }

    public BtRecordRsp() {
    }

    protected BtRecordRsp(Parcel in) {
        this.betTotalAmount = in.readString();
        this.current = in.readString();
        this.hasNext = in.readByte() != 0;
        this.hasPrevious = in.readByte() != 0;
        this.optimizeCountSql = in.readByte() != 0;
        this.pages = in.readString();
        this.profit = in.readString();
        this.searchCount = in.readByte() != 0;
        this.size = in.readString();
        this.total = in.readString();
        this.records = new ArrayList<RecordsBean>();
        in.readList(this.records, RecordsBean.class.getClassLoader());
    }

    public static final Creator<BtRecordRsp> CREATOR = new Creator<BtRecordRsp>() {
        @Override
        public BtRecordRsp createFromParcel(Parcel source) {
            return new BtRecordRsp(source);
        }

        @Override
        public BtRecordRsp[] newArray(int size) {
            return new BtRecordRsp[size];
        }
    };
}
