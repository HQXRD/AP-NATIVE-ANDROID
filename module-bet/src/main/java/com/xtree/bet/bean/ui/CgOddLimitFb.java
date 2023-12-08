package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.BtConfirmOptionInfo;
import com.xtree.bet.bean.CgOddLimitInfo;

public class CgOddLimitFb implements CgOddLimit{
    /**
     * 比赛场数
     */
    private int matchCount;
    private CgOddLimitInfo cgOddLimitInfo;
    private BtConfirmOptionInfo betConfirmOption;
    public CgOddLimitFb(CgOddLimitInfo cgOddLimitInfo, BtConfirmOptionInfo betConfirmOptionInfo, int matchCount){
        this.cgOddLimitInfo = cgOddLimitInfo;
        this.betConfirmOption = betConfirmOptionInfo;
        this.matchCount = matchCount;
    }
    @Override
    public String getCgName() {
        if(cgOddLimitInfo.sn == 0){
            return matchCount + "串" + cgOddLimitInfo.in;
        }else {
            return cgOddLimitInfo.sn + "串1";
        }
    }

    @Override
    public int getDMin() {
        return betConfirmOption.smin;
    }

    @Override
    public int getDMax() {
        return betConfirmOption.smax;
    }

    @Override
    public int getCMin() {
        return cgOddLimitInfo.mi;
    }

    @Override
    public int getCMax() {
        return cgOddLimitInfo.mx;
    }

    @Override
    public double getDOdd() {
        return betConfirmOption.op.od;
    }

    @Override
    public double getCOdd() {
        return cgOddLimitInfo.sodd;
    }

    @Override
    public double getWin(double amount) {
        return cgOddLimitInfo.sodd * amount;
    }

    @Override
    public int getPay(int amount) {
        return cgOddLimitInfo.in * amount;
    }

    @Override
    public int getBtCount() {
        return cgOddLimitInfo.in;
    }
}
