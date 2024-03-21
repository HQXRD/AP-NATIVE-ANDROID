package com.xtree.mine.vo.request;

/**
 * Created by KAKA on 2024/3/19.
 * Describe:
 */
public class DividendAgrtCheckRequest {
    private String userid = "";
    private String type = "";
    private String client = "m";

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
