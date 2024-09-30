package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.ClickUtil;
import com.xtree.mine.R;

import io.reactivex.rxjava3.functions.Consumer;
import me.xtree.mvvmhabit.base.BaseApplication;


/**
 * Created by KAKA on 2024/3/14.
 * Describe:
 */
public class GameDividendAgrtModel extends BindModel {

    private String userName = "-";
    private String bet = "-";
    private String netloss = "-";
    private String profitloss = "-";
    private String people = "-";
    private String cycle_percent = "-";
    private String cycle = "-";
    private String subMoney = "-";
    private String selfMoney = "-";
    //契约状态
    private String payStatu = "-1";
    private String contractStatus = "0";
    //契约状态文本
    private String payStatuText = BaseApplication.getInstance().getString(R.string.txt_noagrement);
    private int payStatuColor = R.color.color_rebateagrt_state_bg_nodividend;
    private String userid = "";
    private String checkName = "";
    private String activity_people;
    private String income;
    private String ratio;
    private String actual;
    private String due;

    private Consumer<GameDividendAgrtModel> createDeedCallBack = null;
    private Consumer<GameDividendAgrtModel> checkDeedCallBack = null;

    public void setCheckDeedCallBack(Consumer<GameDividendAgrtModel> callBack) {
        this.checkDeedCallBack = callBack;
    }

    public void setCreateDeedCallBack(Consumer<GameDividendAgrtModel> createDeedCallBack) {
        this.createDeedCallBack = createDeedCallBack;
    }

    /**
     * 查看契约
     */
    public void checkArgt() {
        if (ClickUtil.isFastClick()) {
            return;
        }
        if (contractStatus.equals("1")) {
            if (checkDeedCallBack != null) {
                try {
                    checkDeedCallBack.accept(this);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            if (createDeedCallBack != null) {
                try {
                    createDeedCallBack.accept(this);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String getPayStatu() {
        return payStatu;
    }

    public void setPayStatu(String payStatu) {
        if (payStatu == null) {
            payStatu = "";
        }
        this.payStatu = payStatu;
        switch (payStatu) {
            case "1": //未结清:
                payStatuColor = R.color.color_rebateagrt_state_bg_unsettled;
                break;
            case "2": //已结清
                payStatuColor = R.color.color_rebateagrt_state_bg_settled;
                break;
            case "3": //无分红
            case "4": //分红取消
                payStatuColor = R.color.color_rebateagrt_state_bg_nodividend;
                break;
            default:
                payStatuColor = R.color.bg_main;
                break;
        }
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
        if (contractStatus.equals("1")) {
            checkName = BaseApplication.getInstance().getString(R.string.txt_view_deed);
        } else {
            checkName = BaseApplication.getInstance().getString(R.string.txt_create_deed);
        }
    }

    public int getPayStatuColor() {
        return BaseApplication.getInstance().getResources().getColor(payStatuColor);
    }

    public String getCheckName() {
        return checkName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (bet != null) {
            this.bet = bet;
        }
        this.userName = userName;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        if (bet != null) {
            this.bet = bet;
        }
    }

    public String getActivity_people() {
        return activity_people;
    }

    public void setActivity_people(String activity_people) {
        this.activity_people = activity_people;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getNetloss() {
        return netloss;
    }

    public void setNetloss(String netloss) {
        if (netloss != null) {
            this.netloss = netloss;
        }
    }

    public String getProfitloss() {
        return profitloss;
    }

    public void setProfitloss(String profitloss) {
        if (profitloss != null) {
            this.profitloss = profitloss;
        }
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        if (people != null) {
            this.people = people;
        }
    }

    public String getCycle_percent() {
        return cycle_percent;
    }

    public void setCycle_percent(String cycle_percent) {
        if (cycle_percent != null) {
            this.cycle_percent = cycle_percent;
        }
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        if (cycle != null) {
            this.cycle = cycle;
        }
    }

    public String getSubMoney() {
        return subMoney;
    }

    public void setSubMoney(String subMoney) {
        if (subMoney != null) {
            this.subMoney = subMoney;
        }
    }

    public String getSelfMoney() {
        return selfMoney;
    }

    public void setSelfMoney(String selfMoney) {
        if (selfMoney != null) {
            this.selfMoney = selfMoney;
        }
    }

    public String getPayStatuText() {
        return payStatuText;
    }

    public void setPayStatuText(String payStatuText) {
        if (payStatuText != null) {
            this.payStatuText = payStatuText;
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        if (userid != null) {
            this.userid = userid;
        }
    }
}
