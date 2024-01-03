package com.xtree.bet.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.request.PBListReq;
import com.xtree.bet.bean.response.LeagueInfo;
import com.xtree.bet.bean.response.LeagueItem;
import com.xtree.bet.bean.response.MatchInfo;
import com.xtree.bet.bean.response.MatchListRsp;
import com.xtree.bet.bean.response.MatchTypeInfo;
import com.xtree.bet.bean.response.MatchTypeStatisInfo;
import com.xtree.bet.bean.response.StatisticalInfo;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public interface MainViewModel {

    /**
     * 获取赛事列表
     *
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
    void getLeagueList(int sportId, int orderBy, List<Integer> leagueIds, List<Integer> matchids, int playMethodType, int searchDatePos, int oddType, boolean isTimedRefresh, boolean isRefresh);

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
    void getChampionList(int sportId, int orderBy, List<Integer> leagueIds, List<Integer> matchids, int playMethodType, int oddType, boolean isTimedRefresh, boolean isRefresh);

    /**
     * 获取赛事统计数据
     */
    void statistical();

    /**
     * 获取联赛列表
     */
    void getOnSaleLeagues(int sportId, int type);
}
