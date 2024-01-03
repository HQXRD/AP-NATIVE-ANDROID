package com.xtree.bet.bean.request;

import java.util.List;

public class PBListReq {
    private int sportId;
    private String languageType = "CMN";
    private List<Integer> leagueIds;
    private int type;
    private String beginTime;
    private String endTime;
    private List<Integer> matchIds;
    private int current;
    private int size;
    private int orderBy;
    private boolean isPC = true;
    private int oddType = 1;

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public List<Integer> getLeagueIds() {
        return leagueIds;
    }

    public void setLeagueIds(List<Integer> leagueIds) {
        this.leagueIds = leagueIds;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getMatchIds() {
        return matchIds;
    }

    public void setMatchIds(List<Integer> matchIds) {
        this.matchIds = matchIds;
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

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isPC() {
        return isPC;
    }

    public void setPC(boolean PC) {
        isPC = PC;
    }

    public int getOddType() {
        return oddType;
    }

    public void setOddType(int oddType) {
        this.oddType = oddType;
    }
}
