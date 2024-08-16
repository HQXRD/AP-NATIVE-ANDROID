package com.xtree.mine.vo.request;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表查看请求体
 */
public class CommissionsReports2Request {

    //?sort=month&start_month=2024/08&end_month=2024/08&username=&status=&page=1&limit=20

    //开始时间
    private String start_month;
    //结束时间
    private String end_month;
    private String sort = "month";
    private String username = "";
    private String status = "";
    private int page =1;
    private int limit = 20;

    public String getStart_month() {
        return start_month;
    }

    public void setStart_month(String start_month) {
        this.start_month = start_month;
    }

    public String getEnd_month() {
        return end_month;
    }

    public void setEnd_month(String end_month) {
        this.end_month = end_month;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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
