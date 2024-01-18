package com.xtree.mine.vo;

public class GameBalanceVo implements Comparable<GameBalanceVo> {

    public String gameAlias; // 自己加的,方便区分游戏类别
    public String gameName; // 自己加的, 场馆名称
    public int orderId; // 自己加的, 优先级, (和排序,imageLevel相关)
    public String balance = "0"; // 返回的 "3.5000"

    public GameBalanceVo(String gameAlias, String gameName, int orderId, String balance) {
        this.gameAlias = gameAlias;
        this.gameName = gameName;
        this.orderId = orderId;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "GameBalanceVo {" +
                " orderId=" + orderId +
                ", gameAlias='" + gameAlias + '\'' +
                ", gameName='" + gameName + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

    @Override
    public int compareTo(GameBalanceVo other) {
        //return 0;
        return Integer.compare(this.orderId, other.orderId);
    }

}
