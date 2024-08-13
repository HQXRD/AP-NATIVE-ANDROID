package com.xtree.mine.ui.rebateagrt.model;

/**
 * Created by KAKA on 2024/8/12.
 * Describe: 搜索用户数据类
 */
public class RebateAgrtSearchUserModel {
    private String usreId;
    private String usreName;

    public RebateAgrtSearchUserModel() {
    }

    public RebateAgrtSearchUserModel(String usreId, String usreName) {
        this.usreId = usreId;
        this.usreName = usreName;
    }

    public String getUsreId() {
        return usreId;
    }

    public void setUsreId(String usreId) {
        this.usreId = usreId;
    }

    public String getUsreName() {
        return usreName;
    }

    public void setUsreName(String usreName) {
        this.usreName = usreName;
    }
}
