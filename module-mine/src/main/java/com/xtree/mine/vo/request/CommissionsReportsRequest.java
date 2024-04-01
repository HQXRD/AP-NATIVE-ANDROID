package com.xtree.mine.vo.request;

/**
 * Created by KAKA on 2024/4/1.
 * Describe: 佣金报表请求体
 */
public class CommissionsReportsRequest {
    //开始时间
    private String start_time;
    //结束时间
    private String end_time;
    private String client = "m";


    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
