package com.xtree.mine.vo.request;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.utils.UuidUtil;

import java.util.List;

/**
 * Created by KAKA on 2024/4/5.
 * Describe: 彩票契约创建请求体
 */
public class DividendAgrtCreateRequest {

    /**
     * userid
     */
    @SerializedName("userid")
    private List<String> userid;
    /**
     * profit
     */
    @SerializedName("profit")
    private List<String> profit;
    /**
     * people
     */
    @SerializedName("people")
    private List<String> people;
    /**
     * ratio
     */
    @SerializedName("ratio")
    private List<String> ratio;
    /**
     * dayPeople
     */
    @SerializedName("day_people")
    private List<String> day_people;
    /**
     * weekPeople
     */
    @SerializedName("week_people")
    private List<String> week_people;
    /**
     * type
     */
    @SerializedName("type")
    private String type;
    /**
     * netProfit
     */
    @SerializedName("net_profit")
    private List<String> net_profit;
    /**
     * nonce
     */
    @SerializedName("nonce")
    private String nonce;

    public List<String> getUserid() {
        return userid;
    }

    public void setUserid(List<String> userid) {
        this.userid = userid;
    }

    public List<String> getProfit() {
        return profit;
    }

    public void setProfit(List<String> profit) {
        this.profit = profit;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public List<String> getRatio() {
        return ratio;
    }

    public void setRatio(List<String> ratio) {
        this.ratio = ratio;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getNonce() {
        return UuidUtil.getID24();
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public List<String> getDay_people() {
        return day_people;
    }

    public void setDay_people(List<String> day_people) {
        this.day_people = day_people;
    }

    public List<String> getWeek_people() {
        return week_people;
    }

    public void setWeek_people(List<String> week_people) {
        this.week_people = week_people;
    }

    public List<String> getNet_profit() {
        return net_profit;
    }

    public void setNet_profit(List<String> net_profit) {
        this.net_profit = net_profit;
    }
}
