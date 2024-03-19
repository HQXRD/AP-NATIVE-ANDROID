package com.xtree.bet.bean.request.fb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BtRecordReq {

    /**
     * 国际化语言类型
     */
    private String languageType = "CMN";
    /**
     * 是否查询串关
     */
    private boolean isSettled;
    /**
     * 币种id，免转钱包必传
     */
    private int size = 100;
    /**
     * 当前分页页数，从1开始
     */
    private int current = 1;
    /**
     * 时间范围类型，与startTime,endTime配合使用, 1.下单时间,2.结算时间
     */
    private int timeType = 1;
    /**
     * 开始时间，13位数字时间戳，查询已结算列表，该字段必填
     */
    private long startTime;
    /*private long endTime = 1706004803395L;*/

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled) {
        isSettled = settled;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
