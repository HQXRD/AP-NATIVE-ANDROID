package com.xtree.bet.ui.viewmodel.pm;

import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401038;
import static com.xtree.bet.constant.PMConstants.SPORT_ICON_ADDITIONAL;
import static com.xtree.bet.constant.PMConstants.SPORT_IDS;
import static com.xtree.bet.constant.PMConstants.SPORT_IDS_DEFAULT;
import static com.xtree.bet.constant.PMConstants.SPORT_NAMES;
import static com.xtree.bet.constant.PMConstants.SPORT_NAMES_LIVE;
import static com.xtree.bet.constant.PMConstants.SPORT_NAMES_NOMAL;
import static com.xtree.bet.constant.PMConstants.SPORT_NAMES_TODAY_CG;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.vo.PMService;
import com.xtree.bet.bean.request.pm.PMListReq;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MatchListRsp;
import com.xtree.bet.bean.response.pm.MenuInfo;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.callback.LeagueListCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMLeagueListCallBack;
import com.xtree.bet.ui.viewmodel.callback.PMListCallBack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by marquis
 */

public class PMMainViewModel extends TemplateMainViewModel implements MainViewModel {
    private List<Match> mMatchList = new ArrayList<>();
    private List<Match> mChampionMatchList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    Map<String, League> mMapLeague = new HashMap<>();
    private Map<String, List<Integer>> sportCountMap = new HashMap<>();

    private List<MenuInfo> mMenuInfoList = new ArrayList<>();
    private int mGoingOnPageSize = 300;
    private int mPageSize = 20;

    public void saveLeague(PMListCallBack pmListCallBack){
        mLeagueList = pmListCallBack.getLeagueList();
        mGoingOnLeagueList = pmListCallBack.getGoingOnLeagueList();
        mMapLeague = pmListCallBack.getMapLeague();
        mMatchList = pmListCallBack.getMatchList();
        mMapMatch = pmListCallBack.getMapMatch();
        mMapSportType = pmListCallBack.getMapSportType();
    }

    public void saveLeague(PMLeagueListCallBack pmListCallBack){
        mLeagueList = pmListCallBack.getLeagueList();
        mGoingOnLeagueList = pmListCallBack.getGoingOnLeagueList();
        mMapLeague = pmListCallBack.getMapLeague();
        mMatchList = pmListCallBack.getMatchList();
        mMapMatch = pmListCallBack.getMapMatch();
        mMapSportType = pmListCallBack.getMapSportType();
        mNoLiveheaderLeague = pmListCallBack.getNoLiveheaderLeague();
    }

    public Map<String, League> getMapLeague() {
        return mMapLeague;
    }

    public List<Match> getMatchList() {
        return mMatchList;
    }

    public Map<String, Match> getMapMatch() {
        return mMapMatch;
    }

    public PMMainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
        SPORT_NAMES = SPORT_NAMES_TODAY_CG;
        SPORT_IDS = SPORT_IDS_DEFAULT;
        sportItemData.postValue(SPORT_NAMES);
    }

    @Override
    public void setSportIds(int playMethodPos) {
        if (playMethodPos == 0 || playMethodPos == 3 || playMethodPos == 1) {
            SPORT_IDS = new String[14];
            SPORT_IDS[0] = "0";
        } else {
            SPORT_IDS = new String[13];
        }
        int playMethodType = Integer.valueOf(getPlayMethodTypes()[playMethodPos]);
        for (MenuInfo menuInfo : mMenuInfoList) {
            Map<String, Integer> sslMap = new HashMap<>();
            if (playMethodType == menuInfo.menuType) {
                for (MenuInfo subMenu : menuInfo.subList) {
                    sslMap.put(String.valueOf(subMenu.menuId), subMenu.count);

                    int index = Arrays.asList(SPORT_NAMES).indexOf(subMenu.menuName);
                    if (index != -1) {
                        SPORT_IDS[index] = String.valueOf(subMenu.menuId);
                    }

                }
                break;
            }
        }
    }

    public void setSportItems(int playMethodPos, int playMethodType) {
        if (playMethodPos == 0 || playMethodPos == 3) {
            if (SPORT_NAMES != SPORT_NAMES_TODAY_CG) {
                SPORT_NAMES = SPORT_NAMES_TODAY_CG;
            }
        } else if (playMethodPos == 1) {
            if (SPORT_NAMES != SPORT_NAMES_LIVE) {
                SPORT_NAMES = SPORT_NAMES_LIVE;
            }
        } else {
            if (SPORT_NAMES != SPORT_NAMES_NOMAL) {
                SPORT_NAMES = SPORT_NAMES_NOMAL;
            }
        }
        setSportIds(playMethodPos);

        if (playMethodPos == 4) {
            List<String> additionalIds = new ArrayList<>();
            List<String> additionalNames = new ArrayList<>();
            List<Integer> additionalIcons = new ArrayList<>();
            for (int i = 0; i < SPORT_IDS.length; i++) {
                additionalIds.add(SPORT_IDS[i]);
                additionalNames.add(SPORT_NAMES[i]);
                additionalIcons.add(Constants.SPORT_ICON[i]);
            }

            for (MenuInfo menuInfo : mMenuInfoList) {
                if (playMethodType == menuInfo.menuType) {
                    for (MenuInfo subMenu : menuInfo.subList) {
                        int index = Arrays.asList(PMConstants.SPORT_TYPES_ADDITIONAL).indexOf(subMenu.menuType);
                        if (index != -1 && subMenu.count > 0) {
                            additionalIds.add(String.valueOf(subMenu.menuId));
                            additionalNames.add(subMenu.menuName);
                            additionalIcons.add(SPORT_ICON_ADDITIONAL[index]);
                        }
                    }
                    break;
                }
            }

            String[] ids = new String[additionalIds.size()];
            String[] names = new String[additionalNames.size()];
            int[] icons = new int[additionalIcons.size()];
            additionalIds.toArray(ids);
            additionalNames.toArray(names);
            for (int i = 0; i < additionalIcons.size(); i++) {
                icons[i] = additionalIcons.get(i);
            }
            PMConstants.SPORT_IDS = ids;
            PMConstants.SPORT_NAMES = names;
            Constants.SPORT_ICON = icons;
        }


        sportItemData.postValue(SPORT_NAMES);
    }

    /**
     * 获取热门联赛赛事数量
     *
     * @param leagueIds
     */
    @Override
    public void getHotMatchCount(int playMethodType, List<Long> leagueIds) {

        if (leagueIds.isEmpty()) {
            return;
        }
        PMListReq pmListReq = new PMListReq();
        pmListReq.setCpn(mCurrentPage);
        pmListReq.setCps(mGoingOnPageSize);
        String sportIds = "";
        if (mMenuInfoList.isEmpty()) {
            for (String sportId : SPORT_IDS_DEFAULT) {
                if (!TextUtils.equals("0", sportId)) {
                    sportIds += sportId + ",";
                }
            }
        } else {
            for (MenuInfo menuInfo : mMenuInfoList) {
                if (playMethodType == menuInfo.menuType) {
                    for (MenuInfo subMenu : menuInfo.subList) {
                        sportIds += subMenu.menuId + ",";
                    }
                }
            }
        }
        pmListReq.setEuid(sportIds);
        if (leagueIds != null && !leagueIds.isEmpty()) {
            String leagueids = "";
            for (Long leagueid : leagueIds) {
                leagueids += leagueid + ",";
            }
            pmListReq.setTid(leagueids.substring(0, leagueids.length() - 1));
        }

        pmListReq.setType(3);

        Flowable flowable = model.getPMApiService().matchesPagePB(pmListReq);
        PMHttpCallBack pmHttpCallBack = new PMHttpCallBack<MatchListRsp>() {

            @Override
            public void onResult(MatchListRsp matchListRsp) {
                hotMatchCountData.postValue(matchListRsp.data.size());
            }

            @Override
            public void onError(Throwable t) {

            }
        };

        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(pmHttpCallBack);
        addSubscribe(disposable);
    }

    /**
     * 获取赛事列表
     *
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchidList
     * @param playMethodType
     * @param searchDatePos  查询时间列表中的位置
     * @param oddType        盘口类型
     * @param isTimedRefresh 是否定时刷新 true-是，false-否
     * @param isRefresh      是否刷新 true-是, false-否
     */

    private int mPlayType;

    public void getLeagueList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchidList, int playMethodType, int searchDatePos, int oddType, boolean isTimerRefresh, boolean isRefresh) {
        getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, playMethodType, searchDatePos, oddType, isTimerRefresh, isRefresh, false);
    }

    /**
     * 获取赛事列表
     *
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchidList
     * @param playMethodType
     * @param searchDatePos  查询时间列表中的位置
     * @param oddType        盘口类型
     * @param isRefresh      是否刷新 true-是, false-否
     */

    public void getLeagueList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchidList, int playMethodType, int searchDatePos, int oddType, boolean isTimerRefresh, boolean isRefresh, boolean isStepSecond) {
        int type;
        boolean flag = false;
        if (!isStepSecond) {
            mPlayMethodType = playMethodType;
        }

        if (isRefresh) {
            mCurrentPage = 1;
        } else if (!isTimerRefresh) {
            mCurrentPage++;
        }

        if (mCurrentPage == 1 && !isTimerRefresh && !isStepSecond) {
            showCache(sportId, mPlayMethodType, searchDatePos);
        }

        PMListReq pmListReq = new PMListReq();
        pmListReq.setEuid(String.valueOf(sportId));
        pmListReq.setMids(matchidList);

        if (isRefresh) {
            type = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? 1 : playMethodType;
            flag = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? true : false;
        } else {
            type = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? 2 : playMethodType;
        }

        if (playMethodType != 2) {
            mPlayType = playMethodType;
        }

        if (type == 2) { // 今日和串关分开两次请求，第一次先获取滚球信息，第二次再获取今日赛事信息，type为2时，代表第二次的请求
            type = 3; // 不带时间查询条件时
            if (searchDatePos > 0) { // 带时间查询条件时
                type = 4;
            }
        }

        if (mMenuInfoList.isEmpty()) {
            pmListReq.setEuid(SPORT_IDS_DEFAULT[sportPos]);
        } else {
            for (MenuInfo menuInfo : mMenuInfoList) {
                boolean isFound = false;
                if (mPlayType == menuInfo.menuType) {
                    String sportIds = "";
                    for (MenuInfo subMenu : menuInfo.subList) {
                        if (TextUtils.equals(SPORT_NAMES[sportPos], "热门") || TextUtils.equals(SPORT_NAMES[sportPos], "全部")) {
                            sportIds += subMenu.menuId + ",";
                        } else {
                            if (subMenu.menuName.contains(SPORT_NAMES[sportPos])) {
                                isFound = true;
                                pmListReq.setEuid(String.valueOf(subMenu.menuId));
                                break;
                            }
                        }
                    }
                    if (TextUtils.equals(SPORT_NAMES[sportPos], "热门") || TextUtils.equals(SPORT_NAMES[sportPos], "全部")) {
                        isFound = true;
                        pmListReq.setEuid(sportIds.substring(0, sportIds.length() - 1));
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }

        final int finalType = type;
        /**
         * 是否是今日玩法中，先获取滚球所有赛事列表后，第二步获取今日进行中的赛事列表
         */
        final boolean needSecondStep = flag;

        pmListReq.setType(type);
        if (type == 1 && flag) {
            pmListReq.setType(3);
        }

        pmListReq.setSort(orderBy);
        if (leagueIds != null && !leagueIds.isEmpty()) {
            String leagueids = "";
            for (Long leagueid : leagueIds) {
                leagueids += leagueid + ",";
            }
            pmListReq.setTid(leagueids.substring(0, leagueids.length() - 1));
        }
        pmListReq.setCpn(mCurrentPage);
        pmListReq.setDevice("v2_h5_st");

        if (!dateList.isEmpty()) {
            if (searchDatePos == dateList.size() - 1) {
                String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                pmListReq.setMd(String.valueOf(0 - TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
            } else if (searchDatePos > 0) {
                String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                pmListReq.setMd(String.valueOf(TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
            }
        }

        Flowable flowable = model.getPMApiService().noLiveMatchesPagePB(pmListReq);
        pmListReq.setCps(mPageSize);
        if (type == 1) {// 滚球
            if (needSecondStep) {
                pmListReq.setCps(mGoingOnPageSize);
                flowable = model.getPMApiService().liveMatchesPB(pmListReq);
            }
        }

        if (isTimerRefresh) {
            flowable = model.getPMApiService().getMatchBaseInfoByMidsPB(pmListReq);
        }

        if (isRefresh) {
            mNoLiveheaderLeague = null;
        }
        PMHttpCallBack pmHttpCallBack;


        if ((type == 1 && needSecondStep) // 获取今日中的全部滚球赛事列表
                || isTimerRefresh) { // 定时刷新赔率变更
            pmHttpCallBack = new PMListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mPlayMethodType, sportPos, sportId,
                    orderBy, leagueIds, searchDatePos, oddType, matchidList);
            /*pmHttpCallBack = new PMHttpCallBack<List<MatchInfo>>() {
                @Override
                protected void onStart() {
                    super.onStart();
                    if (!isTimerRefresh && !mHasCache) {
                        getUC().getShowDialogEvent().postValue("");
                    }
                }

                @Override
                public void onResult(List<MatchInfo> data) {
                    if (isTimerRefresh) { // 定时刷新赔率变更
                        if (data.size() != matchidList.size()) {
                            List<Long> matchIdList = new ArrayList<>();
                            getLeagueList(sportPos, sportId, orderBy, leagueIds, matchIdList, playMethodType, searchDatePos, oddType, false, true);
                        } else {
                            setOptionOddChange(data);
                            leagueLiveTimerListData.postValue(mLeagueList);
                        }
                    } else {  // 获取今日中的全部滚球赛事列表
                        if (isRefresh) {
                            mLeagueList.clear();
                            mMapLeague.clear();
                            mMapSportType.clear();
                        }

                        mIsStepSecond = true;
                        leagueGoingList(data);
                        getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, 2, searchDatePos, oddType, false, isRefresh);
                        *//*if (finalType == 1) { // 滚球
                            if (needSecondStep) {
                                isStepSecond = true;
                                leagueGoingList(data);
                                getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, 2, searchDatePos, oddType, false, isRefresh);
                            } else {
                                getUC().getDismissDialogEvent().call();
                                leagueAdapterList(data);
                                leagueGoingOnListData.postValue(mLeagueList);
                            }
                        } else {
                            leagueAdapterList(data);
                            leagueWaitingListData.postValue(mLeagueList);
                        }*//*


                        *//*if (isRefresh) {
                            finishRefresh(true);
                        } else {
                            if (data != null && data.isEmpty()) {
                                loadMoreWithNoMoreData();
                            } else {
                                finishLoadMore(true);
                            }
                        }*//*
                    }
                }

                @Override
                public void onError(Throwable t) {
                    if (!isTimerRefresh) {
                        if (t instanceof ResponseThrowable) {
                            ResponseThrowable error = (ResponseThrowable) t;
                            if (error.code == CODE_401026 || error.code == CODE_401013) {
                                getGameTokenApi();
                            } else if (error.code == CODE_401038) {
                                ToastUtils.showShort(error.getMessage());
                                tooManyRequestsEvent.call();
                            } else {
                                getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, playMethodType, searchDatePos, oddType, isTimerRefresh, isRefresh);
                            }
                        }
                    }
                    //getUC().getDismissDialogEvent().call();
                }
            };*/
        } else {
            pmHttpCallBack = new PMLeagueListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mCurrentPage, mPlayMethodType, sportPos, sportId,
                    orderBy, leagueIds, searchDatePos, oddType, matchidList,
                    finalType, isStepSecond);
            /*pmHttpCallBack = new PMHttpCallBack<MatchListRsp>() {
                @Override
                protected void onStart() {
                    super.onStart();
                    if (!mHasCache && !mIsStepSecond) {
                        getUC().getShowDialogEvent().postValue("");
                    }
                }

                @Override
                public void onResult(MatchListRsp matchListRsp) {
                    getUC().getDismissDialogEvent().call();
                    if (isRefresh) {
                        if (!mIsStepSecond) {
                            mLeagueList.clear();
                            mMapLeague.clear();
                            mMapSportType.clear();
                        }
                        if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                            loadMoreWithNoMoreData();
                        } else {
                            finishRefresh(true);
                        }
                    } else {
                        if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                            loadMoreWithNoMoreData();
                        } else {
                            finishLoadMore(true);
                        }
                    }

                    leagueAdapterList(matchListRsp.data);
                    if (finalType == 1) { // 滚球
                        leagueLiveListData.postValue(mLeagueList);
                    } else {
                        leagueNoLiveListData.postValue(mLeagueList);
                    }
                    if (mCurrentPage == 1) {
                        SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + searchDatePos + sportId, new Gson().toJson(mLeagueList));
                    }
                    mHasCache = false;
                    mIsStepSecond = false;
                    *//*if (finalType == 1) { // 滚球
                        if (needSecondStep) {
                            leagueGoingList(matchListRsp.data);
                            getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, 2, searchDatePos, oddType, false, isRefresh);
                        } else {
                            leagueAdapterList(matchListRsp.data);
                            leagueGoingOnListData.postValue(mLeagueList);
                        }
                    } else {
                        leagueAdapterList(matchListRsp.data);
                        leagueWaitingListData.postValue(mLeagueList);
                    }*//*
                }

                @Override
                public void onError(Throwable t) {
                    if (t instanceof ResponseThrowable) {
                        ResponseThrowable error = (ResponseThrowable) t;
                        if (error.code == CODE_401026 || error.code == CODE_401013) {
                            getGameTokenApi();
                        } else if (error.code == CODE_401038) {
                            ToastUtils.showShort(error.getMessage());
                            tooManyRequestsEvent.call();
                        } else {
                            getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, playMethodType, searchDatePos, oddType, isTimerRefresh, isRefresh);
                        }
                    }
                }
            };*/
        }

        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(pmHttpCallBack);
        addSubscribe(disposable);
    }

    /**
     * 获取冠军赛事列表
     *
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchids
     * @param playMethodType
     * @param oddType
     * @param isTimerRefresh
     * @param isRefresh
     */
    public void getChampionList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int oddType, boolean isTimerRefresh, boolean isRefresh) {
        if (TextUtils.equals("0", sportId)) {
            championMatchListData.postValue(new ArrayList<>());
            return;
        }

        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }

        if (mCurrentPage == 1 && !isTimerRefresh) {
            showChampionCache(sportId, playMethodType);
        }

        PMListReq pmListReq = new PMListReq();
        pmListReq.setType(playMethodType);
        pmListReq.setSort(orderBy);
        pmListReq.setCpn(mCurrentPage);
        pmListReq.setCps(300);
        //pbListReq.setOddType(oddType);

        if (mMenuInfoList.isEmpty()) {
            pmListReq.setEuid(PMConstants.SPORT_IDS_CHAMPION_SPECAIL[sportPos]);
        } else {
            for (MenuInfo menuInfo : mMenuInfoList) {
                boolean isFound = false;
                if (playMethodType == menuInfo.menuType) {
                    for (MenuInfo subMenu : menuInfo.subList) {

                        if (subMenu.menuName.contains(SPORT_NAMES[sportPos])) {
                            isFound = true;
                            pmListReq.setEuid(String.valueOf(subMenu.menuId));
                            break;
                        }
                    }
                }
                if (isFound) {
                    break;
                }
            }
        }

        Disposable disposable = (Disposable) model.getPMApiService().noLiveMatchesPagePB(pmListReq)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<MatchListRsp>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (!isTimerRefresh && !mHasCache) {
                            getUC().getShowDialogEvent().postValue("");
                        }
                    }

                    @Override
                    public void onResult(MatchListRsp matchListRsp) {
                        if (isTimerRefresh) {
                            setChampionOptionOddChange(matchListRsp.data);
                            championMatchTimerListData.postValue(mChampionMatchList);
                            return;
                        }

                        if (isRefresh) {
                            mChampionMatchList.clear();
                        }

                        getUC().getDismissDialogEvent().call();
                        if (isRefresh) {
                            if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                                loadMoreWithNoMoreData();
                            } else {
                                finishRefresh(true);
                            }
                        } else {
                            if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                                loadMoreWithNoMoreData();
                            } else {
                                finishLoadMore(true);
                            }
                        }

                        championLeagueList(matchListRsp.data);
                        championMatchListData.postValue(mChampionMatchList);
                        if (mCurrentPage == 1) {
                            SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + playMethodType + sportId, new Gson().toJson(mChampionMatchList));
                        }
                        mHasCache = false;
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ResponseThrowable) {
                            ResponseThrowable error = (ResponseThrowable) t;
                            if (error.code == CODE_401026 || error.code == CODE_401013) {
                                getGameTokenApi();
                            } else if (error.code == CODE_401038) {
                                super.onError(t);
                                tooManyRequestsEvent.call();
                            } else {
                                getChampionList(sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, oddType, isTimerRefresh, isRefresh);
                            }
                        }
                        /*if (isRefresh) {
                            finishRefresh(false);
                        } else {
                            finishLoadMore(false);
                        }*/
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取赛事统计数据
     */
    public void statistical(int playMethodType) {
        Map<String, String> map = new HashMap<>();
        map.put("cuid", SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID));
        map.put("sys", "7");

        Disposable disposable = (Disposable) model.getPMApiService().initPB(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<MenuInfo>>() {
                    @Override
                    public void onResult(List<MenuInfo> menuInfoList) {
                        mMenuInfoList = menuInfoList;
                        for (MenuInfo menuInfo : menuInfoList) {
                            Map<String, Integer> sslMap = new HashMap<>();

                            for (MenuInfo subMenu : menuInfo.subList) {
                                sslMap.put(String.valueOf(subMenu.menuId), subMenu.count);
                                if (playMethodType == menuInfo.menuType) {
                                    int index = Arrays.asList(SPORT_NAMES).indexOf(subMenu.menuName);
                                    if (index != -1) {
                                        SPORT_IDS[index] = String.valueOf(subMenu.menuId);
                                    }
                                }
                                List<Integer> sportCountList = new ArrayList<>();
                                String[] sportArr = getSportId(playMethodType);
                                for (String sportId : sportArr) {
                                    sportCountList.add(sslMap.get(sportId));
                                }
                                sportCountMap.put(String.valueOf(menuInfo.menuType), sportCountList);
                            }

                        }

                        statisticalData.postValue(sportCountMap);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取联赛列表
     */
    public void getOnSaleLeagues(int sportId, int type) {

    }

    @Override
    public String[] getPlayMethodTypes() {
        return PMConstants.PLAY_METHOD_TYPES;
    }

    @Override
    public String[] getSportId(int playMethodType) {
        return SPORT_IDS;
    }

    @Override
    public String[] getSportName(int playMethodType) {
        return SPORT_NAMES;
    }

    private void leagueGoingList(List<MatchInfo> matchInfoList) {
        if (matchInfoList.isEmpty()) {
            mNoLiveMatch = true;
            return;
        }

        League liveHeaderLeague = new LeaguePm();
        buildLiveHeaderLeague(liveHeaderLeague);

        Map<String, League> mapLeague = new HashMap<>();
        Map<String, League> mapSportType = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchPm(matchInfo);

            buildLiveSportHeader(mapSportType, match, new LeaguePm());

            League league = mapLeague.get(String.valueOf(matchInfo.tid));
            if (league == null) {
                LeagueInfo leagueInfo = new LeagueInfo();
                leagueInfo.picUrlthumb = matchInfo.lurl;
                leagueInfo.nameText = matchInfo.tn;
                leagueInfo.tournamentId = Long.valueOf(matchInfo.tid);
                league = new LeaguePm(leagueInfo);
                mapLeague.put(String.valueOf(matchInfo.tid), league);

                mGoingOnLeagueList.add(league);
                //mMapGoingOnLeague.put(String.valueOf(matchInfo.lg.id), league);
            }
            league.getMatchList().add(match);

            if (mMapMatch.get(String.valueOf(match.getId())) == null) {
                mMapMatch.put(String.valueOf(match.getId()), match);
                mMatchList.add(match);
            } else {
                int index = mMatchList.indexOf(mMapMatch.get(String.valueOf(match.getId())));
                if (index > -1) {
                    mMatchList.set(index, match);
                }
            }

        }

    }

    /**
     * @param matchInfoList
     * @return
     */
    private void leagueAdapterList(List<MatchInfo> matchInfoList) {
        int noLiveMatchSize = matchInfoList == null ? 0 : matchInfoList.size();
        buildNoLiveHeaderLeague(new LeaguePm(), noLiveMatchSize);

        Map<String, League> mapLeague = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchPm(matchInfo);
            buildNoLiveSportHeader(match, new LeaguePm());

            League league = mMapLeague.get(String.valueOf(matchInfo.tid));
            if (league == null) {
                LeagueInfo leagueInfo = new LeagueInfo();
                leagueInfo.picUrlthumb = matchInfo.lurl;
                leagueInfo.nameText = matchInfo.tn;
                leagueInfo.tournamentId = Long.valueOf(matchInfo.tid);
                league = new LeaguePm(leagueInfo);
                mapLeague.put(String.valueOf(matchInfo.tid), league);

                mLeagueList.add(league);
                mMapLeague.put(String.valueOf(matchInfo.tid), league);
            }
            league.getMatchList().add(match);


            if (mMapMatch.get(String.valueOf(match.getId())) == null) {
                mMapMatch.put(String.valueOf(match.getId()), match);
                mMatchList.add(match);
            } else {
                int index = mMatchList.indexOf(mMapMatch.get(String.valueOf(match.getId())));
                if (index > -1) {
                    mMatchList.set(index, match);
                }
            }
        }

    }

    /**
     * @param matchInfoList
     * @return
     */
    private void championLeagueList(List<MatchInfo> matchInfoList) {
        if (!matchInfoList.isEmpty()) {
            Match header = new MatchPm();
            header.setHead(true);
            mChampionMatchList.add(header);
            for (MatchInfo matchInfo : matchInfoList) {
                Match match = new MatchPm(matchInfo);
                mChampionMatchList.add(match);
            }
        }
    }

    /**
     * 设置赔率变化
     *
     * @param matchInfoList
     */
    private void setOptionOddChange(List<MatchInfo> matchInfoList) {
        List<Match> newMatchList = new ArrayList<>();

        for (MatchInfo matchInfo : matchInfoList) {
            Match newMatch = new MatchPm(matchInfo);
            newMatchList.add(newMatch);
        }

        List<Option> newOptonList = getMatchOptionList(newMatchList);
        List<Option> oldOptonList = getMatchOptionList(mMatchList);

        for (Option newOption : newOptonList) {
            for (Option oldOption : oldOptonList) {
                if (oldOption != null && newOption != null
                        && oldOption.getRealOdd() != newOption.getRealOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getRealOdd());
                    break;
                }
            }
        }

        for (Match match : newMatchList) {
            Match oldMatch = mMapMatch.get(String.valueOf(match.getId()));
            if (oldMatch != null) {
                int index = mMatchList.indexOf(oldMatch);
                if (index > -1) {
                    mMatchList.set(mMatchList.indexOf(oldMatch), match);
                }
            }
        }

        for (Match match : newMatchList) {
            for (League league : mLeagueList) {
                if (!league.isHead() && league.isExpand()) {
                    Match oldMatch = mMapMatch.get(String.valueOf(match.getId()));
                    if (oldMatch != null) {
                        int index = league.getMatchList().indexOf(oldMatch);
                        if (index > -1) {
                            league.getMatchList().set(index, match);
                            mMapMatch.put(String.valueOf(match.getId()), match);
                            break;
                        }
                    }
                }
            }

        }
    }

    /**
     * 设置赔率变化
     *
     * @param matchInfoList
     */
    private void setChampionOptionOddChange(List<MatchInfo> matchInfoList) {
        List<Match> newMatchList = new ArrayList<>();
        Map<String, Match> map = new HashMap<>();

        for (Match match : mChampionMatchList) {
            if (!match.isHead()) {
                map.put(String.valueOf(match.getId()), match);
            }
        }

        for (MatchInfo matchInfo : matchInfoList) {
            Match newMatch = new MatchPm(matchInfo);
            newMatchList.add(newMatch);
        }

        List<Option> newOptonList = getChampionMatchOptionList(newMatchList);
        List<Option> oldOptonList = getChampionMatchOptionList(mChampionMatchList);

        for (Option newOption : newOptonList) {
            for (Option oldOption : oldOptonList) {
                if (oldOption != null && newOption != null
                        && oldOption.getRealOdd() != newOption.getRealOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getRealOdd());
                    break;
                }
            }
        }

        for (Match match : newMatchList) {
            Match oldMatch = map.get(String.valueOf(match.getId()));
            if (oldMatch != null) {
                mChampionMatchList.set(mChampionMatchList.indexOf(oldMatch), match);
            }
        }

    }

    private List<Option> getMatchOptionList(List<Match> matchList) {
        List<Option> optionList = new ArrayList<>();
        for (Match match : matchList) {
            PlayGroup newPlayGroup = new PlayGroupPm(match.getPlayTypeList());

            for (PlayType playType : newPlayGroup.getPlayTypeList()) {
                playType.getOptionLists();
                for (Option option : playType.getOptionList(match.getSportId())) {
                    if (option == null) {
                        continue;
                    }
                    StringBuffer code = new StringBuffer();
                    code.append(match.getId());
                    if (option != null) {
                        code.append(option.getId());
                    }
                    option.setCode(code.toString());
                    optionList.add(option);
                }
            }
        }
        return optionList;
    }

    private List<Option> getChampionMatchOptionList(List<Match> matchList) {
        List<Option> optionArrayList = new ArrayList<>();
        for (Match match : matchList) {
            if (match.isHead()) {
                continue;
            }

            for (PlayType playType : match.getPlayTypeList()) {
                if (playType.getOptionLists() != null) {
                    for (OptionList optionList : playType.getOptionLists()) {
                        for (Option option : optionList.getOptionList()) {
                            if (option != null) {
                                StringBuffer code = new StringBuffer();
                                code.append(match.getId());
                                if (option != null) {
                                    code.append(option.getId());
                                }
                                option.setCode(code.toString());
                            }
                            optionArrayList.add(option);
                        }
                    }
                }
            }
        }
        return optionArrayList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
