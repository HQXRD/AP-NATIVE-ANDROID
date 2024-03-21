package com.xtree.mine.ui.rebateagrt.model;

import java.util.Map;

/**
 * Created by KAKA on 2024/3/18.
 * Describe:
 */
public class RebateAgrtSearchUserResultModel {
    //key userid,value userName
    public Map<String, String> user;

    public RebateAgrtSearchUserResultModel() {
    }

    public RebateAgrtSearchUserResultModel(Map<String, String> user) {
        this.user = user;
    }

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }
}
