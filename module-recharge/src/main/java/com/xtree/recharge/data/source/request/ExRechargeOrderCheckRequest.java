package com.xtree.recharge.data.source.request;

/**
 * Created by KAKA on 2024/5/29.
 * Describe:
 */
public class ExRechargeOrderCheckRequest {

    //pid=176

    private String pid;

    public ExRechargeOrderCheckRequest(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}