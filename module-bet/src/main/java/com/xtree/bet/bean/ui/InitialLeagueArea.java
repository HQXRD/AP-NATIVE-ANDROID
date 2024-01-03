package com.xtree.bet.bean.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * 联赛所属区域
 */
public class InitialLeagueArea {
    private String name;

    private List<LeagueArea> leagueAreaList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LeagueArea> getLeagueAreaList() {
        return leagueAreaList;
    }

    public void addLeagueList(LeagueArea league) {
        this.leagueAreaList.add(league);
    }

}
