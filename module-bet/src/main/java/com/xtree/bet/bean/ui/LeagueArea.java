package com.xtree.bet.bean.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * 联赛所属区域
 */
public class LeagueArea {
    private String name;
    private boolean isSelected;

    private List<League> leagueList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<League> getLeagueList() {
        return leagueList;
    }

    public void addLeagueList(League league) {
        this.leagueList.add(league);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
