package com.xtree.bet.ui.viewmodel.fb;

import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.fb.FBAnnouncementInfo;
import com.xtree.bet.bean.request.fb.FBListReq;
import com.xtree.bet.bean.response.fb.LeagueInfo;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.bet.bean.response.fb.MatchTypeInfo;
import com.xtree.bet.bean.response.fb.MatchTypeStatisInfo;
import com.xtree.bet.bean.response.fb.StatisticalInfo;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.constant.SportTypeItem;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.callback.LeagueListCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.KLog;
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
    private List<MatchInfo> mChampionMatchInfoList = new ArrayList<>();
    private Map<String, Match> mChampionMatchMap = new HashMap<>();
    private StatisticalInfo mStatisticalInfo;
    private ConcurrentHashMap<String, List<SportTypeItem>> sportCountMap = new ConcurrentHashMap<>();
    private int goingOnPageSize = 300;
    private int pageSize = 50;
    private HashMap<Integer, SportTypeItem> mMatchGames = new HashMap<>();

    public void saveLeague(LeagueListCallBack leagueListCallBack) {
        mLeagueList = leagueListCallBack.getLeagueList();
        mGoingOnLeagueList = leagueListCallBack.getGoingOnLeagueList();
        mMapLeague = leagueListCallBack.getMapLeague();
        mMatchList = leagueListCallBack.getMatchList();
        mMapMatch = leagueListCallBack.getMapMatch();
        mMapSportType = leagueListCallBack.getMapSportType();
        mNoLiveheaderLeague = leagueListCallBack.getNoLiveheaderLeague();
        mLiveMatchList = leagueListCallBack.getLiveMatchList();
        mNoliveMatchList = leagueListCallBack.getNoliveMatchList();
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
        //SPORT_NAMES = SPORT_NAMES_TODAY_CG;
        //SPORT_IDS = SPORT_IDS_ALL;
        sportItemData.postValue(new String[]{});
    }

    //@Override
    //public void setSportIds(int playMethodPos) {
    //    if (playMethodPos == 0 || playMethodPos == 3 || playMethodPos == 1) {//今日滚球串关
    //        SPORT_IDS = SPORT_IDS_ALL;
    //    } else {
    //        SPORT_IDS = SPORT_IDS_NOMAL;
    //    }
    //}

    public void setSportItems(int playMethodPos, int playMethodType) {
        sportItemData.postValue(new String[]{});
        //if (playMethodPos == 0 || playMethodPos == 3) {//今日或串关
        //    if (SPORT_NAMES != SPORT_NAMES_TODAY_CG) {
        //        SPORT_NAMES = SPORT_NAMES_TODAY_CG;
        //    }
        //} else if (playMethodPos == 1) {//滚球
        //    if (SPORT_NAMES != SPORT_NAMES_LIVE) {
        //        SPORT_NAMES = SPORT_NAMES_LIVE;
        //    }
        //} else {//早盘和冠军
        //    if (SPORT_NAMES != SPORT_NAMES_NOMAL) {
        //        SPORT_NAMES = SPORT_NAMES_NOMAL;
        //    }
        //}
        //setSportIds(playMethodPos);
        //if (playMethodPos == 4) {
        //    MatchTypeInfo matchTypeInfo;
        //    Map<String, MatchTypeStatisInfo> mapMatchTypeStatisInfo = new HashMap<>();
        //    List<String> additionalIds = new ArrayList<>();
        //    List<String> additionalNames = new ArrayList<>();
        //    List<Integer> additionalIcons = new ArrayList<>();
        //    for (int i = 0; i < SPORT_IDS.length; i++) {
        //        additionalIds.add(SPORT_IDS[i]);
        //        additionalNames.add(FBConstants.SPORT_NAMES[i]);
        //        additionalIcons.add(Constants.SPORT_ICON[i]);
        //    }
        //    if (mStatisticalInfo != null) {
        //        for (MatchTypeInfo typeInfo : mStatisticalInfo.sl) {
        //            if (typeInfo.ty == playMethodType) {
        //                matchTypeInfo = typeInfo;
        //                for (MatchTypeStatisInfo matchTypeStatisInfo :
        //                        matchTypeInfo.ssl) {
        //                    mapMatchTypeStatisInfo.put(String.valueOf(matchTypeStatisInfo.sid), matchTypeStatisInfo);
        //                }
        //                break;
        //            }
        //        }
        //    }
        //
        //    for (int i = 0; i < SPORT_IDS_ADDITIONAL.length; i++) {
        //        MatchTypeStatisInfo matchTypeStatisInfo = mapMatchTypeStatisInfo.get(SPORT_IDS_ADDITIONAL[i]);
        //        if (matchTypeStatisInfo != null && matchTypeStatisInfo.c > 0) {
        //            additionalIds.add(SPORT_IDS_ADDITIONAL[i]);
        //            additionalNames.add(SPORT_NAMES_ADDITIONAL[i]);
        //            additionalIcons.add(SPORT_ICON_ADDITIONAL[i]);
        //        }
        //    }
        //    String[] ids = new String[additionalIds.size()];
        //    String[] names = new String[additionalNames.size()];
        //    int[] icons = new int[additionalIcons.size()];
        //    additionalIds.toArray(ids);
        //    additionalNames.toArray(names);
        //    for (int i = 0; i < additionalIcons.size(); i++) {
        //        icons[i] = additionalIcons.get(i);
        //    }
        //    SPORT_IDS = ids;
        //    SPORT_NAMES = names;
        //    Constants.SPORT_ICON = icons;
        //}

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

        //HashMap<Integer, SportTypeItem> matchGames = getMatchGames();
        //CfLog.i(sportId+"   "+new Gson().toJson(matchGames));
        //SportTypeItem item = matchGames.get(sportId);
        if (sportPos == -1 || TextUtils.equals(sportId, "0") || TextUtils.equals(sportId, "1111")) {
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

        mLeagueListCallBack = new LeagueListCallBack(this, mHasCache, isTimerRefresh, isRefresh, mCurrentPage, mPlayMethodType, sportPos, sportId,
                orderBy, leagueIds, searchDatePos, oddType, matchids,
                needSecondStep, finalType, isStepSecond);

        Disposable disposable = (Disposable) model.getApiService().getFBList(fBListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(mLeagueListCallBack);
        addSubscribe(disposable);
    }

    public void searchMatch(String searchWord, boolean isChampion) {
        mSearchWord = searchWord;
        mIsChampion = isChampion;
        if (!isChampion) {
            mLeagueListCallBack.searchMatch(searchWord);
        } else {
            mChampionMatchList.clear();
            if (!TextUtils.isEmpty(searchWord)) {
                List<MatchInfo> matchInfoList = new ArrayList<>();
                for (MatchInfo matchInfo : mChampionMatchInfoList) {
                    MatchFb matchFb = new MatchFb(matchInfo);
                    if (matchFb.getLeague().getLeagueName().contains(searchWord)) {
                        matchInfoList.add(matchInfo);
                    }
                }
                championLeagueList(matchInfoList);
            } else {
                championLeagueList(mChampionMatchInfoList);
            }
            championMatchListData.postValue(mChampionMatchList);
        }
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
        //HashMap<Integer, SportTypeItem> matchGames = getMatchGames();
        //SportTypeItem item = matchGames.get(sportId);
        //if (TextUtils.equals(item.name, "热门") || TextUtils.equals(item.name, "全部")) {
        //    String sportIds = "";
        //    for (int i = 1; i < SPORT_IDS.length; i++) {
        //        sportIds += SPORT_IDS[i] + ",";
        //    }
        //    FBListReq.setSportId(sportIds);
        //}

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
                            mChampionMatchInfoList.clear();
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
                        mChampionMatchInfoList.addAll(matchListRsp.records);
                        if (TextUtils.isEmpty(mSearchWord)) {
                            championLeagueList(matchListRsp.records);
                            championMatchListData.postValue(mChampionMatchList);
                        } else {
                            searchMatch(mSearchWord, true);
                        }
                        if (mCurrentPage == 1) {
                            SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + playMethodType + sportId, new Gson().toJson(mChampionMatchList));
                        }
                        mHasCache = false;
                    }

                    @Override
                    public void onError(Throwable t) {
                        getUC().getDismissDialogEvent().call();
                        if (t instanceof ResponseThrowable) {
                            if (((ResponseThrowable) t).code == CODE_14010) {
                                getGameTokenApi();
                            } else {
                                getChampionList(sportPos, sportId, orderBy, leagueIds, matchids, playMethodType, oddType, isTimerRefresh, isRefresh);
                            }
                        }
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
                    public synchronized void onResult(StatisticalInfo statisticalInfo) {

                        mStatisticalInfo = statisticalInfo;
                        if (mMatchGames.isEmpty()) {
                            mMatchGames = FBConstants.getMatchGames();
                        }
                        for (MatchTypeInfo matchTypeInfo : statisticalInfo.sl) {
                            //"6", "1", "4", "2", "7"; 只有"今日", "滚球", "早盘", "串关", "冠军"数据才添加，提升效率
                            if (matchTypeInfo.ty == 6 || matchTypeInfo.ty == 1 || matchTypeInfo.ty == 4 || matchTypeInfo.ty == 2 || matchTypeInfo.ty == 7) {
                                //Map<String, Integer> sslMap = new HashMap<>();
                                ArrayList<SportTypeItem> sportTypeItemList = new ArrayList<>();
                                if (matchTypeInfo.ty == 6 || matchTypeInfo.ty == 2) {//今日 串关 加热门
                                    SportTypeItem item1 = new SportTypeItem();
                                    item1.id = 1111;
                                    item1.num = 0;
                                    sportTypeItemList.add(item1);
                                } else if (matchTypeInfo.ty == 1) {//滚球 加全部
                                    SportTypeItem item2 = new SportTypeItem();
                                    item2.id = 0;
                                    item2.num = matchTypeInfo.tc;
                                    sportTypeItemList.add(item2);
                                }
                                for (MatchTypeStatisInfo matchTypeStatisInfo : matchTypeInfo.ssl) {
                                    SportTypeItem item = new SportTypeItem();
                                    item.id = matchTypeStatisInfo.sid;
                                    item.num = matchTypeStatisInfo.c;
                                    if (item.num <= 0 || mMatchGames.get(item.id) == null) {
                                        continue;
                                    }
                                    sportTypeItemList.add(item);
                                    //sslMap.put(String.valueOf(matchTypeStatisInfo.sid), matchTypeStatisInfo.c);
                                }

                                sportCountMap.put(String.valueOf(matchTypeInfo.ty), sportTypeItemList);
                                //CfLog.i("num223   " + String.valueOf(6) + "  " + new Gson().toJson(sportCountMap.get(String.valueOf(6))));
                            }
                        }
                        //CfLog.i("num22   " + 6 + "  " + new Gson().toJson(sportCountMap.get(String.valueOf(6))));
                        //CfLog.i("num22   " + new Gson().toJson(sportCountMap));
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
    public HashMap<Integer, SportTypeItem> getMatchGames() {
        return FBConstants.getMatchGames();
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

    /**
     * 公告列表集合
     */
    public void getAnnouncement() {

        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");

        Disposable disposable = (Disposable) model.getApiService().getListPage(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<FBAnnouncementInfo>() {
                    @Override
                    public void onResult(FBAnnouncementInfo announcementInfo) {
                        announcementData.postValue(announcementInfo.records);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
