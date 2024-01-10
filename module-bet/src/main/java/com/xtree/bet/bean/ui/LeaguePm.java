package com.xtree.bet.bean.ui;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.bet.bean.response.pm.LeagueInfo;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

@SuppressLint("ParcelCreator")
public class LeaguePm implements League{
    private boolean isExpand = true;
    private boolean isHead;
    private boolean isSelected;
    public int sort;
    public LeagueInfo leagueInfo;
    public List<Match> matchList = new ArrayList<>();

    public LeaguePm(){

    }

    public LeaguePm(LeagueInfo leagueInfo){
        this.leagueInfo = leagueInfo;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public LeagueInfo getLeagueInfo() {
        return leagueInfo;
    }

    public void setLeagueInfo(LeagueInfo leagueInfo) {
        this.leagueInfo = leagueInfo;
    }

    @Override
    public String getIcon() {
        String domain = SPUtils.getInstance().getString(SPKeyGlobal.PM_IMG_SERVICE_URL);
        if(domain.endsWith("/") && leagueInfo.picUrlthumb.startsWith("/")){
            return domain.substring(domain.indexOf("/")) + leagueInfo.picUrlthumb;
        } else if (!domain.endsWith("/") && !leagueInfo.picUrlthumb.startsWith("/")) {
            return domain + "/" + leagueInfo.picUrlthumb;
        } else {
            return domain+ leagueInfo.picUrlthumb;
        }
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
        return leagueInfo.num;
    }

    @Override
    public League instance() {
        return new LeaguePm();
    }

    @Override
    public boolean isHot() {
        return leagueInfo.hotStatus == 1;
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
        return leagueInfo.regionName;
    }
    /**
     * 获取联赛区域ID
     * @return
     */
    @Override
    public int getAreaId() {
        return leagueInfo.regionId;
    }

    /**
     * 获取联赛名称
     * @return
     */
    @Override
    public String getLeagueName() {
        return leagueInfo.nameText;
    }

    /**
     * 获取联赛ID
     * @return
     */
    @Override
    public long getId() {
        return leagueInfo.tournamentId;
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
