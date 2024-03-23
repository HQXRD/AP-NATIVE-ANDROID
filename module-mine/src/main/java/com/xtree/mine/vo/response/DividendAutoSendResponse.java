package com.xtree.mine.vo.response;

/**
 * Created by KAKA on 2024/3/15.
 * Describe: 契约分红一键发放
 */
public class DividendAutoSendResponse {
    private int status;
    private String msg;
    private String servertime;
    private int ts;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServertime() {
        return servertime;
    }

    public void setServertime(String servertime) {
        this.servertime = servertime;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }
}
