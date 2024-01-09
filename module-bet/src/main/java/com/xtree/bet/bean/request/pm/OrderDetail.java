package com.xtree.bet.bean.request.pm;

public class OrderDetail {
    /**
     * 玩法id
     */
    private long playId;
    /**
     * 最终赔率（电竞赛事，小数直接截取并保留3位小数， 非电竞赛事直接截取并保留2位小数）
     */
    private String oddFinally;
    /**
     * 欧洲赔率
     */
    private long odds;
    /**
     * 投注金额（无需处理，用户输入为准）
     */
    private double betAmount;
    /**
     * 投注项id。预约投注时候，如果预约一个不存在的盘口或者投注项，次值可以为空
     */
    private String playOptionsId;
    /**
     * 最终盘口类型
     */
    private String marketTypeFinally = "EU";
    /**
     * 坑位（盘口位置 1：表示主盘，2：表示第一副盘）
     */
    private int placeNum;
    /**
     * 盘口id
     */
    private String  marketId;
    /**
     *赛事类型标识：1-早盘赛事，2-滚球盘赛事，3-冠军盘赛事， 4-虚拟赛事，5-电竞赛事。
     * 取值逻辑（传错将导致投注失败）： （1）早盘、滚球赛事：从相关赛事API的hl.hmt中获取，hmt=1表示早盘，hmt=0表示滚球 （2）冠军盘、虚拟、电竞赛事：根据菜单相应传3、4、5参数
     */
    private long matchType;
    /**
     * 赛事id
     */
    private long matchId;

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(long playId) {
        this.playId = playId;
    }

    public String getOddFinally() {
        return oddFinally;
    }

    public void setOddFinally(String oddFinally) {
        this.oddFinally = oddFinally;
    }

    public long getOdds() {
        return odds;
    }

    public void setOdds(long odds) {
        this.odds = odds;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public String getPlayOptionsId() {
        return playOptionsId;
    }

    public void setPlayOptionsId(String playOptionsId) {
        this.playOptionsId = playOptionsId;
    }

    public String getMarketTypeFinally() {
        return marketTypeFinally;
    }

    public void setMarketTypeFinally(String marketTypeFinally) {
        this.marketTypeFinally = marketTypeFinally;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public long getMatchType() {
        return matchType;
    }

    public void setMatchType(long matchType) {
        this.matchType = matchType;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public int getPlaceNum() {
        return placeNum;
    }

    public void setPlaceNum(int placeNum) {
        this.placeNum = placeNum;
    }
}
