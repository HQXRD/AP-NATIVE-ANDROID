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
    private boolean isHead; //
    private boolean isSelected;
    private int matchCount; //
    public int sort;
    public String leagueName = "";
    private int headType;
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
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
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
    public int getMatchCount() {
        return matchCount;
    }
    @Override
    public void setMatchCount(int matchCount) {
        this.matchCount += matchCount;
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
     * 设置联赛名称
     * @param leagueName
     */
    @Override
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    /**
     * 获取联赛名称
     * @return
     */
    @Override
    public String getLeagueName() {
        if(leagueInfo == null || TextUtils.isEmpty(leagueInfo.na)){
            return leagueName;
        }
        return leagueInfo.na;
    }

    /**
     * 获取头部类型 1-进行中或未开赛 2-球种名称
     * @return
     */
    public int getHeadType() {
        return headType;
    }

    /**
     * 设置头部类型 1-进行中或未开赛 2-球种名称
     * @return
     */
    public void setHeadType(int headType) {
        this.headType = headType;
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
