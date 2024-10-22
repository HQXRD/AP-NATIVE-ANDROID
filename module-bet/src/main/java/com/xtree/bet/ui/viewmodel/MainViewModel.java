package com.xtree.bet.ui.viewmodel;

import com.xtree.bet.constant.SportTypeItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by marquis
 */

public interface MainViewModel {

    /**
     * 获取赛事列表
     *
     * @param sportPos
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchids
     * @param playMethodType
     * @param searchDatePos  查询时间列表中的位置
     * @param oddType 盘口类型
     * @param isTimedRefresh 是否定时刷新 true-是，false-否
     * @param isRefresh      是否刷新 true-是, false-否
     */
    void getLeagueList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int searchDatePos, int oddType, boolean isTimedRefresh, boolean isRefresh);

    /**
     * 获取冠军赛事列表
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchids
     * @param playMethodType
     * @param oddType
     * @param isTimedRefresh
     * @param isRefresh
     */
    void getChampionList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int oddType, boolean isTimedRefresh, boolean isRefresh);

    /**
     * 获取赛事统计数据
     */
    void statistical(int playMethodType);

    /**
     * 获取联赛列表
     */
    void getOnSaleLeagues(int sportId, int type);

    /**
     * 获取赛果配置参数
     */
    void postMerchant();

    /**
     * 获取赛事赛果列表
     */
    void matchResultPage(String beginTime, String endTime, int playMethodPos, String sportId) ;

    /**
     * 获取玩法类型列表
     * @return
     */
    String[] getPlayMethodTypes();

    /**
     * 获取体育彩种所有数据
     * @return
     */
    HashMap<Integer, SportTypeItem> getMatchGames();

    /**
     * 获取用户余额
     */
    void getUserBalance();

    /**
     * 获取热门联赛赛事数量
     * @param leagueIds
     */
    void getHotMatchCount(int playMethodType, List<Long> leagueIds);
    void searchMatch(String searchWord, boolean isChampion);

    /**
     * 获取赛事公告
     */
    void getAnnouncement();
}
