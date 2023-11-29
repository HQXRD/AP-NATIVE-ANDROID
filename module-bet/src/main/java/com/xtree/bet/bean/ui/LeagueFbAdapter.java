package com.xtree.bet.bean.ui;

import com.xtree.bet.bean.LeagueInfo;

import java.util.ArrayList;
import java.util.List;

public class LeagueFbAdapter implements League{
    public int sort;
    public LeagueInfo leagueInfo;
    public List<MatchFbAdapter> matchFbAdapterlist = new ArrayList<>();
    public LeagueFbAdapter(LeagueInfo leagueInfo){
        this.leagueInfo = leagueInfo;
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
    public List<MatchFbAdapter> getMatchList() {
        return matchFbAdapterlist;
    }
}
