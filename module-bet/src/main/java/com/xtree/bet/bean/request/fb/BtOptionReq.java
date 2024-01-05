package com.xtree.bet.bean.request.fb;

public class BtOptionReq {
    /**
     * 投注选项类型code，如大小球的 大、小选项类型
     */
    private String optionType;
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
    private long marketId;

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
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

    public long getMarketId() {
        return marketId;
    }

    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }
}
