package com.xtree.bet.bean.request.pm;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.text.TextUtils;

import com.xtree.base.global.SPKeyGlobal;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BtCarReq {
    private String cuid = SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID);
    private List<BetMatchMarket> idList = new ArrayList<>();

    public void setCuid() {
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if(TextUtils.equals(platform, PLATFORM_PMXC)){
            this.cuid = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_USER_ID);
        }
    }

    public void setIdList(List<BetMatchMarket> betMatchMarketList) {
        this.idList = betMatchMarketList;
    }

    public static class BetMatchMarket{
        /**
         * 赛事ID
         */
        private long matchInfoId;
        /**
         * 玩法ID
         */
        private long marketId;
        /**
         * 投注项ID
         */
        private String oddsId;
        /**
         * 投注项（对应ot）/ 坑位玩法传值
         */
        private String oddsType;
        /**
         * 玩法id
         */
        private String playId;
        /**
         * 玩法id
         */
        private int placeNum;
        /**
         * 赛事类型标识，传错将导致投注失败：1-早盘赛事，2-滚球盘赛事，3-冠军盘赛事， 4-VR赛事，
         * 5-电竞赛种。 取值逻辑： （1）早盘、滚球赛事：从相关赛事API的hl.hmt中获取，hmt=1表示早盘，hmt=0表示滚球 （2）冠军盘、虚拟、电竞赛事：根据菜单相应传3、4、5参数
         */
        private int matchType;
        /**
         * 球种ID
         */
        private int sportId;

        public long getMatchInfoId() {
            return matchInfoId;
        }

        public void setMatchInfoId(long matchInfoId) {
            this.matchInfoId = matchInfoId;
        }

        public long getMarketId() {
            return marketId;
        }

        public void setMarketId(long marketId) {
            this.marketId = marketId;
        }

        public String getOddsId() {
            return oddsId;
        }

        public void setOddsId(String oddsId) {
            this.oddsId = oddsId;
        }

        public String getOddsType() {
            return oddsType;
        }

        public void setOddsType(String oddsType) {
            this.oddsType = oddsType;
        }

        public String getPlayId() {
            return playId;
        }

        public void setPlayId(String playId) {
            this.playId = playId;
        }

        public int getPlaceNum() {
            return placeNum;
        }

        public void setPlaceNum(int placeNum) {
            this.placeNum = placeNum;
        }

        public int getMatchType() {
            return matchType;
        }

        public void setMatchType(int matchType) {
            this.matchType = matchType;
        }

        public int getSportId() {
            return sportId;
        }

        public void setSportId(int sportId) {
            this.sportId = sportId;
        }
    }
}
