package com.xtree.bet.bean.request.pm;

import com.xtree.base.global.SPKeyGlobal;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BtCarCgReq {
    private String cuid = SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID);
    private List<OrderMaxBetMoney> orderMaxBetMoney = new ArrayList<>();

    public void setOrderMaxBetMoney(List<OrderMaxBetMoney> orderMaxBetMoney) {
        this.orderMaxBetMoney = orderMaxBetMoney;
    }

    public static class OrderMaxBetMoney {
        /**
         * 设备类型 1：H5，2：PC，3：Android，4：IOS，5：其他设备
         */
        private int deviceType = 3;
        /**
         * 是否开启 多单关投注模式，1：是，非1（0或者其他）：否
         */
        private int openMiltSingle;
        /**
         * 玩法id
         */
        private String playId;
        /**
         * 投注项id
         */
        private String playOptionId;
        /**
         * 赛事类型标识，传错将导致投注失败：1-早盘赛事，2-滚球盘赛事，3-冠军盘赛事， 4-VR赛事，5-电竞赛种。
         * 取值逻辑： （1）早盘、滚球赛事：从相关赛事API的hl.hmt中获取，hmt=1表示早盘，hmt=0表示滚球 （2）冠军盘、虚拟、电竞赛事：根据菜单相应传3、4、5参数
         */
        private int matchType;
        /**
         * 玩法id
         */
        private String marketId;
        /**
         * 赛事类型标识，传错将导致投注失败：1-早盘赛事，2-滚球盘赛事，3-冠军盘赛事， 4-VR赛事，
         * 5-电竞赛种。 取值逻辑： （1）早盘、滚球赛事：从相关赛事API的hl.hmt中获取，hmt=1表示早盘，hmt=0表示滚球 （2）冠军盘、虚拟、电竞赛事：根据菜单相应传3、4、5参数
         */
        private long matchId;
        /**
         * 欧洲赔率
         */
        private double oddsValue;

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public int getOpenMiltSingle() {
            return openMiltSingle;
        }

        public void setOpenMiltSingle(int openMiltSingle) {
            this.openMiltSingle = openMiltSingle;
        }

        public String getPlayId() {
            return playId;
        }

        public void setPlayId(String playId) {
            this.playId = playId;
        }

        public String getPlayOptionId() {
            return playOptionId;
        }

        public void setPlayOptionId(String playOptionId) {
            this.playOptionId = playOptionId;
        }

        public int getMatchType() {
            return matchType;
        }

        public void setMatchType(int matchType) {
            this.matchType = matchType;
        }

        public String getMarketId() {
            return marketId;
        }

        public void setMarketId(String marketId) {
            this.marketId = marketId;
        }

        public long getMatchId() {
            return matchId;
        }

        public void setMatchId(long matchId) {
            this.matchId = matchId;
        }

        public double getOddsValue() {
            return oddsValue;
        }

        public void setOddsValue(double oddsValue) {
            this.oddsValue = oddsValue;
        }
    }
}
