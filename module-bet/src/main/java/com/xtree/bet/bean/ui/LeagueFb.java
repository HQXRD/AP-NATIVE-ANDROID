package com.xtree.bet.bean.ui;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.bet.bean.response.fb.LeagueInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class LeagueFb implements League{
    private boolean isExpand = true;
    private boolean isHead;
    private boolean isSelected;
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
    /**
     * 该联赛开售的赛事统计
     * @return
     */
    @Override
    public int getSaleCount() {
        return leagueInfo.mt;
    }

    @Override
    public League instance() {
        return new LeagueFb();
    }

    @Override
    public boolean isHot() {
        return leagueInfo.hot;
    }

    @Override
    public boolean isHead() {
        return isHead;
    }
    /**
     * 获取联赛区域名称
     * @return
     */
    @Override
    public String getLeagueAreaName() {
        return leagueInfo.rnm;
    }
    /**
     * 获取联赛区域ID
     * @return
     */
    @Override
    public int getAreaId() {
        return leagueInfo.rid;
    }

    /**
     * 获取联赛名称
     * @return
     */
    @Override
    public String getLeagueName() {
        if(leagueInfo == null || TextUtils.isEmpty(leagueInfo.na)){
            return "";
        }
        return leagueInfo.na;
    }

    /**
     * 获取联赛ID
     * @return
     */
    @Override
    public long getId() {
        return leagueInfo.id;
    }

    @Override
    public List<Match> getMatchList() {
        return matchList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
