package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆下级契约列表数据
 */
public class GameSubordinateagrtModel extends BindModel {

    private String sname;
    private String status = "-1";
    private int statusColor = R.color.clr_txt_rebateagrt_default;

    private String userName;
    private String userID;
    private String signTime;
    private String effectDate;
    private String ruleRatio ;
    private String createTime;

    public int getStatusColor() {
        return BaseApplication.getInstance().getResources().getColor(statusColor);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null) {
            this.status = status;
            switch (status) {
                case "1": //1-已签订
                    statusColor = R.color.clr_txt_rebateagrt_profit;
                    break;
                case "4": //4-已终止
                    statusColor = R.color.clr_txt_rebateagrt_default;
                    break;
                case "8": //8-已更新
                    statusColor = R.color.clr_txt_rebateagrt_profit;
                    break;
                default:
                    statusColor = R.color.clr_txt_rebateagrt_default;
                    break;
            }
        }
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getRuleRatio() {
        return ruleRatio + "%";
    }

    public void setRuleRatio(String ruleRatio) {
        if (ruleRatio != null && !ruleRatio.isEmpty()) {
            this.ruleRatio = ruleRatio;
        }
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
