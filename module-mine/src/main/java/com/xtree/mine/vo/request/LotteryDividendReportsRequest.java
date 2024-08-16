package com.xtree.mine.vo.request;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 彩票分红报表请求体
 */
public class LotteryDividendReportsRequest {

    //?cycleid=&username=&agency_model=&status=1&p=1&pn=20

    private String cycleid;
    private String agency_model;
    private String username = "";
    private String status = "1";
    private int page =1;
    private int limit = 20;

    public String getCycleid() {
        return cycleid;
    }

    public void setCycleid(String cycleid) {
        this.cycleid = cycleid;
    }

    public String getAgency_model() {
        return agency_model;
    }

    public void setAgency_model(String agency_model) {
        this.agency_model = agency_model;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
