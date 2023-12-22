package com.xtree.bet.bean.request;

public class BtOptionReq {
    /**
     * 投注选项类型code，如大小球的 大、小选项类型
     */
    private int optionType;
    /**
     * 是否查询串关
     */
    private int oddsFormat;
    /**
     * 下注赔率（无论前端展示哪种赔率，这里都传欧洲盘赔率）
     */
    private double odds;
    /**
     * 玩法ID
     */
    private int marketId;

    public int getOptionType() {
        return optionType;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public int getOddsFormat() {
        return oddsFormat;
    }

    public void setOddsFormat(int oddsFormat) {
        this.oddsFormat = oddsFormat;
    }

    public double getOdds() {
        return odds;
    }

    public void setOdds(double odds) {
        this.odds = odds;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }
}
