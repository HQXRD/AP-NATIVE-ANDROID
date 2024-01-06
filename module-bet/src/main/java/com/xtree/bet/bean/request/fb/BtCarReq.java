package com.xtree.bet.bean.request.fb;

import java.util.ArrayList;
import java.util.List;

public class BtCarReq {
    /**
     * 国际化语言类型
     */
    private String languageType;
    /**
     * 是否查询串关
     */
    private boolean isSelectSeries;
    /**
     * 币种id，免转钱包必传
     */
    private int currencyId;
    private List<BetMatchMarket> betMatchMarketList = new ArrayList<>();

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public void setSelectSeries(boolean selectSeries) {
        isSelectSeries = selectSeries;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public void setBetMatchMarketList(List<BetMatchMarket> betMatchMarketList) {
        this.betMatchMarketList = betMatchMarketList;
    }

    public static class BetMatchMarket{
        /**
         * 玩法ID
         */
        private long marketId;
        /**
         * 赛事ID
         */
        private long matchId;
        /**
         * 投注项类型
         */
        private String type;
        /**
         * 赔率类型
         */
        private int oddsType = 1;

        public void setMarketId(long marketId) {
            this.marketId = marketId;
        }

        public void setMatchId(long matchId) {
            this.matchId = matchId;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setOddsType(int oddsType) {
            this.oddsType = oddsType;
        }
    }
}
