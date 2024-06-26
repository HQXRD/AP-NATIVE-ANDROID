package com.xtree.bet.ui.viewmodel.fb;

import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;
import static com.xtree.bet.constant.FBConstants.SPORT_ICON_ADDITIONAL;
import static com.xtree.bet.constant.FBConstants.SPORT_IDS;
import static com.xtree.bet.constant.FBConstants.SPORT_IDS_ADDITIONAL;
import static com.xtree.bet.constant.FBConstants.SPORT_IDS_ALL;
import static com.xtree.bet.constant.FBConstants.SPORT_IDS_NOMAL;
import static com.xtree.bet.constant.FBConstants.SPORT_NAMES;
import static com.xtree.bet.constant.FBConstants.SPORT_NAMES_ADDITIONAL;
import static com.xtree.bet.constant.FBConstants.SPORT_NAMES_LIVE;
import static com.xtree.bet.constant.FBConstants.SPORT_NAMES_NOMAL;
import static com.xtree.bet.constant.FBConstants.SPORT_NAMES_TODAY_CG;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;
import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FBXC;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.vo.FBService;
import com.xtree.bet.bean.response.fb.LeagueInfo;
import com.xtree.bet.bean.response.fb.MatchTypeInfo;
import com.xtree.bet.bean.response.fb.MatchTypeStatisInfo;
import com.xtree.bet.bean.response.fb.StatisticalInfo;
import com.xtree.bet.bean.request.fb.FBListReq;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

import com.xtree.base.net.FBHttpCallBack;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.callback.LeagueListCallBack;

import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis
 */

public class FBMainViewModel extends TemplateMainViewModel implements MainViewModel {
    private LeagueListCallBack mLeagueListCallBack;
    private Map<String, League> mMapLeague = new HashMap<>();
    private List<Match> mMatchList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();

    private List<Match> mChampionMatchList = new ArrayList<>();
    private Map<String, Match> mChampionMatchMap = new HashMap<>();
    private StatisticalInfo mStatisticalInfo;
    private Map<String, List<Integer>> sportCountMap = new HashMap<>();
    private int goingOnPageSize = 300;
    private int pageSize = 50;

    public void saveLeague(LeagueListCallBack leagueListCallBack) {
        mLeagueList = leagueListCallBack.getLeagueList();
        mGoingOnLeagueList = leagueListCallBack.getGoingOnLeagueList();
        mMapLeague = leagueListCallBack.getMapLeague();
        mMatchList = leagueListCallBack.getMatchList();
        mMapMatch = leagueListCallBack.getMapMatch();
        mMapSportType = leagueListCallBack.getMapSportType();
        mNoLiveheaderLeague = leagueListCallBack.getNoLiveheaderLeague();
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

    public FBMainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
        SPORT_NAMES = SPORT_NAMES_TODAY_CG;
        SPORT_IDS = SPORT_IDS_ALL;
        sportItemData.postValue(SPORT_NAMES);
    }

    @Override
    public void setSportIds(int playMethodPos) {
        if (playMethodPos == 0 || playMethodPos == 3 || playMethodPos == 1) {
            SPORT_IDS = SPORT_IDS_ALL;
        } else {
            SPORT_IDS = SPORT_IDS_NOMAL;
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
            MatchTypeInfo matchTypeInfo;
            Map<String, MatchTypeStatisInfo> mapMatchTypeStatisInfo = new HashMap<>();
            List<String> additionalIds = new ArrayList<>();
            List<String> additionalNames = new ArrayList<>();
            List<Integer> additionalIcons = new ArrayList<>();
            for (int i = 0; i < SPORT_IDS.length; i++) {
                additionalIds.add(SPORT_IDS[i]);
                additionalNames.add(FBConstants.SPORT_NAMES[i]);
                additionalIcons.add(Constants.SPORT_ICON[i]);
            }
            if (mStatisticalInfo != null) {
                for (MatchTypeInfo typeInfo : mStatisticalInfo.sl) {
                    if (typeInfo.ty == playMethodType) {
                        matchTypeInfo = typeInfo;
                        for (MatchTypeStatisInfo matchTypeStatisInfo :
                                matchTypeInfo.ssl) {
                            mapMatchTypeStatisInfo.put(String.valueOf(matchTypeStatisInfo.sid), matchTypeStatisInfo);
                        }
                        break;
                    }
                }
            }

            for (int i = 0; i < SPORT_IDS_ADDITIONAL.length; i++) {
                MatchTypeStatisInfo matchTypeStatisInfo = mapMatchTypeStatisInfo.get(SPORT_IDS_ADDITIONAL[i]);
                if (matchTypeStatisInfo != null && matchTypeStatisInfo.c > 0) {
                    additionalIds.add(SPORT_IDS_ADDITIONAL[i]);
                    additionalNames.add(SPORT_NAMES_ADDITIONAL[i]);
                    additionalIcons.add(SPORT_ICON_ADDITIONAL[i]);
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
            SPORT_IDS = ids;
            SPORT_NAMES = names;
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
        FBListReq fBListReq = new FBListReq();
        fBListReq.setCurrent(1);
        fBListReq.setSize(30);
        fBListReq.setType(6);
        fBListReq.setLeagueIds(leagueIds);

        Disposable disposable = (Disposable) model.getApiService().getFBList(fBListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<MatchListRsp>() {

                    @Override
                    public void onResult(MatchListRsp matchListRsp) {
                        hotMatchCountData.postValue(matchListRsp.getTotal());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取赛事列表
     *
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchids
     * @param playMethodType
     * @param searchDatePos  查询时间列表中的位置
     * @param oddType        盘口类型
     * @param isTimerRefresh 是否定时刷新 true-是，false-否
     * @param isRefresh      是否刷新 true-是, false-否
     */
    public void getLeagueList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int searchDatePos, int oddType, boolean isTimerRefresh, boolean isRefresh) {
        getLeagueList(sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, searchDatePos, oddType, isTimerRefresh, isRefresh, false);
    }

    public void getLeagueList(int sportPos, String sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int searchDatePos, int oddType,
                              boolean isTimerRefresh, boolean isRefresh, boolean isStepSecond) {
        if (!isStepSecond) {
            mPlayMethodType = playMethodType;
        }

        if (isRefresh) {
            mCurrentPage = 1;
            mNoLiveheaderLeague = null;
        } else if (!isTimerRefresh) {
            mCurrentPage++;
        }

        if (mCurrentPage == 1 && !isTimerRefresh && !isStepSecond) {
            showCache(sportId, mPlayMethodType, searchDatePos);
        }

        int type;
        boolean flag = false;

        if (isRefresh) {
            type = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? 1 : playMethodType;
            flag = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? true : false;
        } else {
            type = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? 3 : playMethodType;
        }

        final int finalType = type;
        final boolean needSecondStep = flag;

        FBListReq fBListReq = new FBListReq();
        fBListReq.setSportId(sportId);
        fBListReq.setType(type);
        fBListReq.setOrderBy(orderBy);
        fBListReq.setLeagueIds(leagueIds);
        fBListReq.setMatchIds(matchids);
        fBListReq.setCurrent(mCurrentPage);
        fBListReq.setOddType(oddType);

        if (sportPos == -1 || TextUtils.equals(SPORT_NAMES[sportPos], "热门") || TextUtils.equals(SPORT_NAMES[sportPos], "全部")) {
            fBListReq.setSportId(null);
        }

        if (type != 1 && type != 3) {
            if (searchDatePos == dateList.size() - 1) {
                fBListReq.setBeginTime(dateList.get(dateList.size() - 1).getTime() + "");
                fBListReq.setEndTime(TimeUtils.addDays(dateList.get(dateList.size() - 1), 30).getTime() + "");
            } else if (searchDatePos != 0) {
                String start = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                String end = TimeUtils.parseTime(TimeUtils.addDays(dateList.get(searchDatePos), 1), TimeUtils.FORMAT_YY_MM_DD) + " 11:59:59";

                fBListReq.setBeginTime(TimeUtils.strFormatDate(start, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime() + "");
                fBListReq.setEndTime(TimeUtils.strFormatDate(end, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime() + "");
            }
        }

        if (type == 1) {// 滚球
            fBListReq.setSize(goingOnPageSize);
        } else {
            fBListReq.setSize(pageSize);
        }

        LeagueListCallBack leagueListCallBack = new LeagueListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mCurrentPage, mPlayMethodType, sportPos, sportId,
                orderBy, leagueIds, searchDatePos, oddType, matchids,
                needSecondStep, finalType, isStepSecond);

        Disposable disposable = (Disposable) model.getApiService().getFBList(fBListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(leagueListCallBack);
                /*.subscribeWith(new FBHttpCallBack<MatchListRsp>() {
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
                            if (matchListRsp.records.size() != matchids.size()) {
                                List<Long> matchIdList = new ArrayList<>();
                                getLeagueList(sportPos, sportId, orderBy, leagueIds, matchIdList, playMethodType, searchDatePos, oddType, false, true);
                            } else {
                                setOptionOddChange(matchListRsp.records);
                                leagueGoingOnTimerListData.postValue(mLeagueList);
                            }
                            return;
                        }
                        synchronized (this) {
                            CfLog.e("Thread.currentThread().getName()========" + Thread.currentThread().getName());
                            if (isRefresh && !needSecondStep) {
                                mLeagueList.clear();
                                mMapLeague.clear();
                                mMapSportType.clear();
                            }

                            if (!needSecondStep) {
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
                            }

                            if (finalType == 1) { // 滚球
                                if (needSecondStep) {
                                    mIsStepSecond = true;
                                    leagueGoingList(matchListRsp.records);
                                    getLeagueList(sportPos, sportId, orderBy, leagueIds, matchids, 3, searchDatePos, oddType, false, isRefresh);
                                } else {
                                    leagueAdapterList(matchListRsp.records);
                                    leagueGoingOnListData.postValue(mLeagueList);
                                    if (mCurrentPage == 1) {
                                        SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + searchDatePos + sportId, new Gson().toJson(mLeagueList));
                                    }
                                }
                            } else {
                                leagueAdapterList(matchListRsp.records);
                                leagueWaitingListData.postValue(mLeagueList);
                                if (mCurrentPage == 1) {
                                    SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + searchDatePos + sportId, new Gson().toJson(mLeagueList));
                                }
                                mIsStepSecond = false;
                            }
                            mHasCache = false;

                            //CfLog.e("=========getLeagueList========" + sportId + "========" + mLeagueList.size());
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ResponseThrowable) {
                            if (((ResponseThrowable) t).code == CODE_14010) {
                                getGameTokenApi();
                            } else {
                                getLeagueList(sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, searchDatePos, oddType, isTimerRefresh, isRefresh);
                            }
                        }
                        *//*if (isRefresh) {
                            finishRefresh(false);
                        } else {
                            finishLoadMore(false);
                        }*//*
                    }
                });*/
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

        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }

        if (mCurrentPage == 1 && !isTimerRefresh) {
            showChampionCache(sportId, playMethodType);
        }

        FBListReq FBListReq = new FBListReq();
        FBListReq.setSportId(sportId);
        FBListReq.setType(playMethodType);
        FBListReq.setOrderBy(orderBy);
        FBListReq.setLeagueIds(leagueIds);
        FBListReq.setMatchIds(matchids);
        FBListReq.setCurrent(mCurrentPage);
        FBListReq.setSize(300);
        FBListReq.setOddType(oddType);

        if (TextUtils.equals(SPORT_NAMES[sportPos], "热门") || TextUtils.equals(SPORT_NAMES[sportPos], "全部")) {
            String sportIds = "";
            for (int i = 1; i < SPORT_IDS.length; i++) {
                sportIds += SPORT_IDS[i] + ",";
            }
            FBListReq.setSportId(sportIds);
        }


        Disposable disposable = (Disposable) model.getApiService().getFBList(FBListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<MatchListRsp>() {
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
                            setChampionOptionOddChange(matchListRsp.records);
                            championMatchTimerListData.postValue(mChampionMatchList);
                            return;
                        }

                        getUC().getDismissDialogEvent().call();
                        if (isRefresh) {
                            mChampionMatchList.clear();
                            mChampionMatchMap.clear();
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

                        championLeagueList(matchListRsp.records);
                        CfLog.e("=========mChampionMatchList=========" + mChampionMatchList.size());
                        championMatchListData.postValue(mChampionMatchList);
                        if (mCurrentPage == 1) {
                            SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + playMethodType + sportId, new Gson().toJson(mChampionMatchList));
                        }
                        mHasCache = false;
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ResponseThrowable) {
                            if (((ResponseThrowable) t).code == CODE_14010) {
                                getGameTokenApi();
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
        map.put("languageType", "CMN");

        Disposable disposable = (Disposable) model.getApiService().statistical(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<StatisticalInfo>() {
                    @Override
                    public void onResult(StatisticalInfo statisticalInfo) {
                        mStatisticalInfo = statisticalInfo;
                        for (MatchTypeInfo matchTypeInfo : statisticalInfo.sl) {
                            Map<String, Integer> sslMap = new HashMap<>();
                            for (MatchTypeStatisInfo matchTypeStatisInfo : matchTypeInfo.ssl) {
                                sslMap.put(String.valueOf(matchTypeStatisInfo.sid), matchTypeStatisInfo.c);
                            }
                            List<Integer> sportCountList = new ArrayList<>();
                            for (String sportId : SPORT_IDS) {
                                sportCountList.add(sslMap.get(sportId));
                            }
                            sportCountMap.put(String.valueOf(matchTypeInfo.ty), sportCountList);
                        }
                        statisticalData.postValue(sportCountMap);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取联赛列表
     */
    public void getOnSaleLeagues(int sportId, int type) {

        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        map.put("sportId", String.valueOf(sportId));
        map.put("type", String.valueOf(type));

        Disposable disposable = (Disposable) model.getApiService().getOnSaleLeagues(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<List<LeagueInfo>>() {
                    @Override
                    public void onResult(List<LeagueInfo> leagueInfoList) {
                        List<League> leagueList = new ArrayList<>();
                        for (LeagueInfo leagueInfo : leagueInfoList) {
                            leagueList.add(new LeagueFb(leagueInfo));
                        }
                        settingLeagueData.postValue(leagueList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public String[] getPlayMethodTypes() {
        return FBConstants.PLAY_METHOD_TYPES;
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

        League liveheaderLeague = new LeagueFb();
        buildLiveHeaderLeague(liveheaderLeague);

        Map<String, League> mapLeague = new HashMap<>();
        Map<String, League> mapSportType = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchFb(matchInfo);
            buildLiveSportHeader(mapSportType, match, new LeagueFb());

            League league = mapLeague.get(String.valueOf(matchInfo.lg.id));

            if (league == null) {
                league = new LeagueFb(matchInfo.lg);
                mapLeague.put(String.valueOf(matchInfo.lg.id), league);

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
                    mMapMatch.put(String.valueOf(match.getId()), match);
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
        buildNoLiveHeaderLeague(new LeagueFb(), noLiveMatchSize);
        Map<String, League> mapLeague = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            if (FBConstants.getPlayTypeId(String.valueOf(matchInfo.sid)) == null) {
                continue;
            }
            Match match = new MatchFb(matchInfo);
            buildNoLiveSportHeader(match, new LeagueFb());
            League league = mMapLeague.get(String.valueOf(matchInfo.lg.id));
            if (league == null) {
                league = new LeagueFb(matchInfo.lg);
                mapLeague.put(String.valueOf(matchInfo.lg.id), league);

                mLeagueList.add(league);
                mMapLeague.put(String.valueOf(matchInfo.lg.id), league);
            }

            league.getMatchList().add(match);

            if (mMapMatch.get(String.valueOf(match.getId())) == null) {
                mMapMatch.put(String.valueOf(match.getId()), match);
                mMatchList.add(match);
            } else {
                int index = mMatchList.indexOf(mMapMatch.get(String.valueOf(match.getId())));
                if (index > -1) {
                    mMapMatch.put(String.valueOf(match.getId()), match);
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
            Match header = new MatchFb();
            header.setHead(true);
            mChampionMatchList.add(header);
            for (MatchInfo matchInfo : matchInfoList) {
                Match match = new MatchFb(matchInfo);
                mChampionMatchList.add(match);
                mChampionMatchMap.put(String.valueOf(match.getId()), match);
            }
        }
    }

    /**
     * 设置赔率变化
     *
     * @param matchInfoList
     */
    public void setOptionOddChange(List<MatchInfo> matchInfoList) {
        List<Match> newMatchList = new ArrayList<>();

        for (MatchInfo matchInfo : matchInfoList) {
            Match newMatch = new MatchFb(matchInfo);
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

        for (MatchInfo matchInfo : matchInfoList) {
            Match newMatch = new MatchFb(matchInfo);
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
            Match oldMatch = mChampionMatchMap.get(String.valueOf(match.getId()));
            if (oldMatch != null) {
                mChampionMatchList.set(mChampionMatchList.indexOf(oldMatch), match);
                mChampionMatchMap.put(String.valueOf(match.getId()), match);
            }
        }

    }

    private List<Option> getMatchOptionList(List<Match> matchList) {
        List<Option> optionList = new ArrayList<>();
        for (Match match : matchList) {
            if (match.isHead()) {
                continue;
            }
            PlayGroup newPlayGroup = new PlayGroupFb(match.getPlayTypeList());
            newPlayGroup.getPlayGroupList(match.getSportId());

            for (PlayType playType : newPlayGroup.getPlayTypeList()) {
                playType.getOptionLists();
                for (Option option : playType.getOptionList(match.getSportId())) {
                    if (option != null && playType.getOptionLists() != null && !playType.getOptionLists().isEmpty()) {
                        StringBuffer code = new StringBuffer();
                        code.append(match.getId());
                        code.append(playType.getPlayType());
                        code.append(playType.getPlayPeriod());
                        code.append(playType.getOptionLists().get(0).getId());
                        code.append(option.getOptionType());
                        code.append(option.getId());
                        if (!TextUtils.isEmpty(option.getLine())) {
                            code.append(option.getLine());
                        }
                        option.setCode(code.toString());
                    }
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
                                code.append(playType.getPlayType());
                                code.append(playType.getPlayPeriod());
                                code.append(optionList.getId());
                                code.append(option.getOptionType());
                                code.append(option.getId());
                                if (!TextUtils.isEmpty(option.getLine())) {
                                    code.append(option.getLine());
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
