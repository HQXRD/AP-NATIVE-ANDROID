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
        if(cgOddLimitInfo == null){
            return "";
        }
        if(cgOddLimitInfo.sn == 0){
            return matchCount + "串" + cgOddLimitInfo.in;
        }else {
            return cgOddLimitInfo.sn + "串1";
        }
    }

    @Override
    public int getDMin() {
        if(betConfirmOption == null){
            return 5;
        }
        return betConfirmOption.smin;
    }

    @Override
    public int getDMax() {
        if(betConfirmOption == null){
            return 5;
        }
        return betConfirmOption.smax;
    }

    @Override
    public int getCMin() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return cgOddLimitInfo.mi;
    }

    @Override
    public int getCMax() {
        if(cgOddLimitInfo == null){
            return 5;
        }
        return cgOddLimitInfo.mx;
    }

    @Override
    public double getDOdd() {
        if(betConfirmOption == null){
            return 0;
        }
        return betConfirmOption.op.od;
    }

    @Override
    public double getCOdd() {
        if(cgOddLimitInfo == null){
            return 0;
        }
        return cgOddLimitInfo.sodd;
    }

    @Override
    public double getWin(double amount) {
        if(cgOddLimitInfo == null){
            return 0;
        }
        return cgOddLimitInfo.sodd * amount;
    }

    @Override
    public int getPay(int amount) {
        if(cgOddLimitInfo == null){
            return 0;
        }
        return cgOddLimitInfo.in * amount;
    }

    @Override
    public int getBtCount() {
        if(cgOddLimitInfo == null){
            return 0;
        }
        return cgOddLimitInfo.in;
    }
}
