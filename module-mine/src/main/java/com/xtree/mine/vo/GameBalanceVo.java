package com.xtree.mine.vo;

public class GameBalanceVo {

    public String gameAlias; // 自己加的,方便区分游戏类别
    public String balance; // 返回的 "3.5000"

    @Override
    public String toString() {
        return "GameBalanceVo {" +
                "gameAlias='" + gameAlias + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
