package com.xtree.bet.bean.request.fb;

import java.util.List;

public class FBListReq {
    private String sportId;
    private String languageType = "CMN";
    private List<Long> leagueIds;
    private int type;
    private String beginTime;
    private String endTime;
    private List<Long> matchIds;
    private List<Integer> sportIds;
    private int current;
    private int size;
    private int orderBy;
    private boolean isPC = true;
    private int oddType = 1;

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public List<Long> getLeagueIds() {
        return leagueIds;
    }

    public void setLeagueIds(List<Long> leagueIds) {
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

    public List<Long> getMatchIds() {
        return matchIds;
    }

    public void setMatchIds(List<Long> matchIds) {
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

    @Override
    public String toString() {
        return "FBListReq{" +
                "sportId='" + sportId + '\'' +
                ", languageType='" + languageType + '\'' +
                ", leagueIds=" + leagueIds +
                ", type=" + type +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", matchIds=" + matchIds +
                ", sportIds=" + sportIds +
                ", current=" + current +
                ", size=" + size +
                ", orderBy=" + orderBy +
                ", isPC=" + isPC +
                ", oddType=" + oddType +
                '}';
    }
}
