package com.xtree.mine.vo.request;

/**
 * Created by KAKA on 2024/3/19.
 * Describe: 请求拼接参数
 */
public class RebateAgrtCreateRuery {
    private String controller = "compact";
    private String action = "create";
    private String type = "";
    private String client = "m";

    public RebateAgrtCreateRuery() {
    }

    public RebateAgrtCreateRuery(String controller, String action, String type, String client) {
        this.controller = controller;
        this.action = action;
        this.type = type;
        this.client = client;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
