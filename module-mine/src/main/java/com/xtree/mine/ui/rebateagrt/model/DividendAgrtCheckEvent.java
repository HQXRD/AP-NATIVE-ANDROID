package com.xtree.mine.ui.rebateagrt.model;

/**
 * Created by KAKA on 2024/4/5.
 * Describe:
 */
public class DividendAgrtCheckEvent {

    private String userid = "";
    private String userName = "";
    private String type = "";
    private String rules = null;
    //0-查看模式 1-创建模式
    private int mode = 0;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

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

}
