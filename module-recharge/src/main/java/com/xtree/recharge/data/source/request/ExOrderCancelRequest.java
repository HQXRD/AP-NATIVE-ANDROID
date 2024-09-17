package com.xtree.recharge.data.source.request;

/**
 * Created by KAKA on 2024/5/28.
 * Describe:
 */
public class ExOrderCancelRequest {
    //pid=176&platformOrder=59e29da2
    private String pid;
    private String platformOrder;

    public ExOrderCancelRequest(String pid, String platformOrder) {
        this.pid = pid;
        this.platformOrder = platformOrder;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPlatformOrder() {
        return platformOrder;
    }

    public void setPlatformOrder(String platformOrder) {
        this.platformOrder = platformOrder;
    }
}
