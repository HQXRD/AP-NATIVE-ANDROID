package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.LeagueInfo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.bus.RxSubscriptions;

public class LeagueFbAdapter implements League{
    private boolean isExpand = true;
    private boolean isHead;
    public int sort;
    public LeagueInfo leagueInfo;
    public List<Match> matchList = new ArrayList<>();

    public LeagueFbAdapter(){

    }

    public LeagueFbAdapter(LeagueInfo leagueInfo){
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
    public boolean getExpand() {
        return this.isExpand;
    }

    @Override
    public void setHead(boolean isHead) {
        this.isHead = isHead;
    }

    @Override
    public League instance() {
        return new LeagueFbAdapter();
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
