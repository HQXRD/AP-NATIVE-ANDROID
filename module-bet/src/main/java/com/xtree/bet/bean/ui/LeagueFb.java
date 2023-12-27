package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.response.LeagueInfo;

import java.util.ArrayList;
import java.util.List;

public class LeagueFb implements League{
    private boolean isExpand = true;
    private boolean isHead;
    public int sort;
    public LeagueInfo leagueInfo;
    public List<Match> matchList = new ArrayList<>();

    public LeagueFb(){

    }

    public LeagueFb(LeagueInfo leagueInfo){
        this.leagueInfo = leagueInfo;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String getIcon() {
        return leagueInfo.lurl;
    }

    @Override
    public boolean setExpand(boolean isExpand) {
        return this.isExpand = isExpand;
    }

    @Override
    public boolean isExpand() {
        return this.isExpand;
    }

    @Override
    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }

    @Override
    public League instance() {
        return new LeagueFb();
    }

    @Override
    public boolean isHead() {
        return isHead;
    }

    /**
     * 获取联赛名称
     * @return
     */
    @Override
    public String getLeagueName() {
        return leagueInfo.na;
    }

    /**
     * 获取联赛ID
     * @return
     */
    @Override
    public int getId() {
        return leagueInfo.id;
    }

    @Override
    public List<Match> getMatchList() {
        return matchList;
    }


}
