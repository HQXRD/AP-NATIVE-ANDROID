package com.xtree.mine.ui.rebateagrt.model;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表数据model
 */
public class CommissionsReportsModel {
    //实发佣金
    private String actual;
    //发放时间
    private String sent_at;
    //净输赢
    private String income;
    //上月结余
    private String last_remain;
    //本月结余
    private String remain;
    //佣金比例
    private String ratio;
    //佣金状态
    private String status_name;
    //活跃人数
    private String activity_people;
    //账户调整
    private String adjust_income;
    //实发奖励
    private String actual_incentives;
    //团队亏损额
    private String team_losses;
    //活跃代理
    private String active_agents;
    //奖励比例
    private String incentive_ratio;

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getSent_at() {
        return sent_at;
    }

    public void setSent_at(String sent_at) {
        this.sent_at = sent_at;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getLast_remain() {
        return last_remain;
    }

    public void setLast_remain(String last_remain) {
        this.last_remain = last_remain;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getActivity_people() {
        return activity_people;
    }

    public void setActivity_people(String activity_people) {
        this.activity_people = activity_people;
    }

    public String getAdjust_income() {
        return adjust_income;
    }

    public void setAdjust_income(String adjust_income) {
        this.adjust_income = adjust_income;
    }

    public String getActual_incentives() {
        return actual_incentives;
    }

    public void setActual_incentives(String actual_incentives) {
        this.actual_incentives = actual_incentives;
    }

    public String getTeam_losses() {
        return team_losses;
    }

    public void setTeam_losses(String team_losses) {
        this.team_losses = team_losses;
    }

    public String getActive_agents() {
        return active_agents;
    }

    public void setActive_agents(String active_agents) {
        this.active_agents = active_agents;
    }

    public String getIncentive_ratio() {
        return incentive_ratio;
    }

    public void setIncentive_ratio(String incentive_ratio) {
        this.incentive_ratio = incentive_ratio;
    }
}
