package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.BtConfirmOptionInfo;

public class BetConfirmOptionFb implements BetConfirmOption {
    private BtConfirmOptionInfo btConfirmOptionInfo;
    private String teamName;
    public BetConfirmOptionFb(BtConfirmOptionInfo btConfirmOptionInfo, String teamName){
        this.btConfirmOptionInfo = btConfirmOptionInfo;
        this.teamName = teamName;
    }
    @Override
    public int getPlayTypeId() {
        return btConfirmOptionInfo.mid;
    }

    @Override
    public Option getOption() {
        return new OptionFb(btConfirmOptionInfo.op);
    }

    @Override
    public double getDanMin() {
        return btConfirmOptionInfo.smin;
    }

    @Override
    public double getDanMax() {
        return btConfirmOptionInfo.smax;
    }

    @Override
    public int isClose() {
        return btConfirmOptionInfo.ss;
    }

    @Override
    public String getScore() {
        return btConfirmOptionInfo.re;
    }

    @Override
    public String getTeamName() {
        return teamName;
    }
}
