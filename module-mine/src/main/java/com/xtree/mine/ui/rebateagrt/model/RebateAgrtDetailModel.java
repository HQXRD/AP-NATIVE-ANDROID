package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.mine.vo.response.GameSubordinateAgrteResponse;

import java.util.List;

/**
 * Created by KAKA on 2024/3/18.
 * Describe: 用户搜索数据模型
 */
public class RebateAgrtDetailModel {
    private GameSubordinateAgrteResponse subData;
    private List<RebateAgrtSearchUserModel> searchUserModel;
    private String checkUserId = null;
    //1 -创建模式 2 -查看模式
    private int mode = 1;

    public RebateAgrtDetailModel(int mode) {
        this.mode = mode;
    }

    public GameSubordinateAgrteResponse getSubData() {
        return subData;
    }

    public void setSubData(GameSubordinateAgrteResponse subData) {
        this.subData = subData;
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public List<RebateAgrtSearchUserModel> getSearchUserModel() {
        return searchUserModel;
    }

    public void setSearchUserModel(List<RebateAgrtSearchUserModel> searchUserModel) {
        this.searchUserModel = searchUserModel;
    }
}
