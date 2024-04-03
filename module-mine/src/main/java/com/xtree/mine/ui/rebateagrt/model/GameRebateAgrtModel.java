package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆返水列表数据
 */
public class GameRebateAgrtModel extends BindModel {

    //日投注额
    private String betAmoutDay;
    //有效投注额
    private String betAmoutValidity;
    //返水比例
    private String rebatePercentage;
    //返水总额
    private String rebateAmout;
    //亏损总额
    private String lossAmount;
    //活跃人数
    private String people;
    //下级工资
    private String subMoney;
    //自身工资
    private String mineMoney;
    //日期
    private String date;
    //状态
    private String status;
    //场馆类型
    private RebateAreegmentTypeEnum typeEnum;
    private String statusString = BaseApplication.getInstance().getString(R.string.txt_unreceived);
    private int statusColor = BaseApplication.getInstance().getResources().getColor(R.color.clr_txt_rebateagrt_fail);

    public void setTypeEnum(RebateAreegmentTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public void setStatus(String status) {
        if (status != null && !status.isEmpty()) {
            this.status = status;
            switch (status) {
                case "1":
                    statusString = BaseApplication.getInstance().getString(R.string.txt_received);
                    statusColor = BaseApplication.getInstance().getResources().getColor(R.color.clr_txt_rebateagrt_success);
                    break;
                case "3":
                    statusString = BaseApplication.getInstance().getString(R.string.txt_nodividend);
                    statusColor = BaseApplication.getInstance().getResources().getColor(R.color.clr_txt_rebateagrt_fail);
                    break;
                default:
                    statusString = BaseApplication.getInstance().getString(R.string.txt_unreceived);
                    statusColor = BaseApplication.getInstance().getResources().getColor(R.color.clr_txt_rebateagrt_fail);
                    break;
            }
        }
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public int getStatusColor() {
        return statusColor;
    }

    public String getRebatePercentage() {
        switch (typeEnum) {
            case USER:
            case DAYREBATE:
                return rebatePercentage + "元/千";
            default:
                return rebatePercentage + "%";
        }
    }

    public void setRebatePercentage(String rebatePercentage) {
        this.rebatePercentage = rebatePercentage;
    }

    public String getBetAmoutDay() {
        return betAmoutDay;
    }

    public void setBetAmoutDay(String betAmoutDay) {
        this.betAmoutDay = betAmoutDay;
    }

    public String getBetAmoutValidity() {
        return betAmoutValidity;
    }

    public void setBetAmoutValidity(String betAmoutValidity) {
        this.betAmoutValidity = betAmoutValidity;
    }

    public String getRebateAmout() {
        return rebateAmout;
    }

    public void setRebateAmout(String rebateAmout) {
        this.rebateAmout = rebateAmout;
    }

    public String getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(String lossAmount) {
        this.lossAmount = lossAmount;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getSubMoney() {
        return subMoney;
    }

    public void setSubMoney(String subMoney) {
        this.subMoney = subMoney;
    }

    public String getMineMoney() {
        return mineMoney;
    }

    public void setMineMoney(String mineMoney) {
        this.mineMoney = mineMoney;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public RebateAreegmentTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }
}
