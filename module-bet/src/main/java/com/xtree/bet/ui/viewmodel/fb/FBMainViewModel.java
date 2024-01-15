package com.xtree.bet.ui.viewmodel.fb;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.utils.TimeUtils;
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
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;

import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class FBMainViewModel extends TemplateMainViewModel implements MainViewModel {

    private Map<String, League> mMapLeague = new HashMap<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<League> mGoingOnLeagueList = new ArrayList<>();
    private List<Match> mMatchList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();

    private List<Match> mChampionMatchList = new ArrayList<>();
    private Map<String, Match> mChampionMatchMap = new HashMap<>();

    private Map<String, List<Integer>> sportCountMap = new HashMap<>();
    private int currentPage = 1;
    private int goingOnPageSize = 300;
    private int pageSize = 50;

    public FBMainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
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
     * @param oddType 盘口类型
     * @param isTimedRefresh 是否定时刷新 true-是，false-否
     * @param isRefresh      是否刷新 true-是, false-否
     */
    public void getLeagueList(int sportPos, int sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int searchDatePos, int oddType, boolean isTimedRefresh, boolean isRefresh) {
        int type;
        boolean flag = false;

        if (isRefresh) {
            type = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? 1 : playMethodType;
            flag = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? true : false;
            currentPage = 1;
        } else {
            type = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? 3 : playMethodType;
            currentPage++;
        }

        final int finalType = type;
        final boolean finalFlag = flag;

        FBListReq FBListReq = new FBListReq();
        FBListReq.setSportId(sportId);
        FBListReq.setType(type);
        FBListReq.setOrderBy(orderBy);
        FBListReq.setLeagueIds(leagueIds);
        FBListReq.setMatchIds(matchids);
        FBListReq.setCurrent(currentPage);
        FBListReq.setOddType(oddType);

        String startTime;
        String endTime;


        if (type != 1 && type != 3) {
            if (searchDatePos == dateList.size() - 1) {
                FBListReq.setBeginTime(dateList.get(dateList.size() - 1).getTime() + "");
                FBListReq.setEndTime(TimeUtils.addDays(dateList.get(dateList.size() - 1), 30).getTime() + "");
                startTime = TimeUtils.longFormatString(Long.valueOf(FBListReq.getBeginTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
                endTime = TimeUtils.longFormatString(Long.valueOf(FBListReq.getEndTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
            } else if (searchDatePos != 0) {
                String start = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                String end = TimeUtils.parseTime(TimeUtils.addDays(dateList.get(searchDatePos), 1), TimeUtils.FORMAT_YY_MM_DD) + " 11:59:59";

                FBListReq.setBeginTime(TimeUtils.strFormatDate(start, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime() + "");
                FBListReq.setEndTime(TimeUtils.strFormatDate(end, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime() + "");
                /*startTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getBeginTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
                endTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getEndTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);*/
            }
        }

        if (type == 1) {// 滚球
            FBListReq.setSize(goingOnPageSize);
        } else {
            FBListReq.setSize(pageSize);
        }

        if (isRefresh) {
            mLeagueList.clear();
            mMapLeague.clear();
        }

        Disposable disposable = (Disposable) model.getApiService().getFBList(FBListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MatchListRsp>() {
                    @Override
                    public void onResult(MatchListRsp matchListRsp) {
                        if (isTimedRefresh) {
                            setOptionOddChange(matchListRsp.records);
                            leagueGoingOnTimerListData.postValue(mLeagueList);
                            return;
                        }
                        if (finalType == 1) { // 滚球
                            if(finalFlag) {
                                leagueGoingList(matchListRsp.records);
                                goingOnMatchCountData.postValue(matchListRsp.getTotal());
                                getLeagueList(sportPos, sportId, orderBy, leagueIds, matchids, 3, searchDatePos, oddType, false, isRefresh);
                            }else{
                                leagueAdapterList(matchListRsp.records);
                                leagueGoingOnListData.postValue(mLeagueList);
                            }
                        } else {
                            leagueAdapterList(matchListRsp.records);
                            leagueWaitingListData.postValue(mLeagueList);
                        }

                        if(!finalFlag) {
                            if (isRefresh) {
                                if (matchListRsp != null && currentPage == matchListRsp.getPages()) {
                                    loadMoreWithNoMoreData();
                                } else {
                                    finishRefresh(true);
                                }
                            } else {
                                if (matchListRsp != null && currentPage == matchListRsp.getPages()) {
                                    loadMoreWithNoMoreData();
                                } else {
                                    finishLoadMore(true);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (isRefresh) {
                            finishRefresh(false);
                        } else {
                            finishLoadMore(false);
                        }
                    }
                });
        addSubscribe(disposable);
    }

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
    public void getChampionList(int sportPos, int sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int oddType, boolean isTimedRefresh, boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        FBListReq FBListReq = new FBListReq();
        FBListReq.setSportId(sportId);
        FBListReq.setType(playMethodType);
        FBListReq.setOrderBy(orderBy);
        FBListReq.setLeagueIds(leagueIds);
        FBListReq.setMatchIds(matchids);
        FBListReq.setCurrent(currentPage);
        FBListReq.setSize(300);
        FBListReq.setOddType(oddType);

        if (isRefresh) {
            mChampionMatchList.clear();
            mChampionMatchMap.clear();
        }

        Disposable disposable = (Disposable) model.getApiService().getFBList(FBListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MatchListRsp>() {
                    @Override
                    public void onResult(MatchListRsp matchListRsp) {
                        if (isTimedRefresh) {
                            setChampionOptionOddChange(matchListRsp.records);
                            championMatchTimerListData.postValue(mChampionMatchList);
                            return;
                        }

                        championLeagueList(matchListRsp.records);
                        championMatchListData.postValue(mChampionMatchList);

                        if (isRefresh) {
                            finishRefresh(true);
                        } else {
                            if (matchListRsp != null && matchListRsp.records != null && matchListRsp.records.isEmpty()) {
                                loadMoreWithNoMoreData();
                            } else {
                                finishLoadMore(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (isRefresh) {
                            finishRefresh(false);
                        } else {
                            finishLoadMore(false);
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
                .subscribeWith(new HttpCallBack<StatisticalInfo>() {
                    @Override
                    public void onResult(StatisticalInfo statisticalInfo) {
                        for (MatchTypeInfo matchTypeInfo : statisticalInfo.sl) {
                            Map<String, Integer> sslMap = new HashMap<>();
                            for (MatchTypeStatisInfo matchTypeStatisInfo : matchTypeInfo.ssl) {
                                sslMap.put(String.valueOf(matchTypeStatisInfo.sid), matchTypeStatisInfo.c);
                            }
                            List<Integer> sportCountList = new ArrayList<>();
                            for (String sportId : FBConstants.SPORT_IDS) {
                                sportCountList.add(sslMap.get(sportId));
                            }
                            sportCountMap.put(String.valueOf(matchTypeInfo.ty), sportCountList);
                        }
                        statisticalData.postValue(sportCountMap);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
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
                .subscribeWith(new HttpCallBack<List<LeagueInfo>>() {
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
        return FBConstants.SPORT_IDS;
    }

    private void leagueGoingList(List<MatchInfo> matchInfoList) {

        Map<String, League> mapLeague = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            League league = mapLeague.get(String.valueOf(matchInfo.lg.id));

            if (league == null) {
                league = new LeagueFb(matchInfo.lg);
                mapLeague.put(String.valueOf(matchInfo.lg.id), league);

                mGoingOnLeagueList.add(league);
                //mMapGoingOnLeague.put(String.valueOf(matchInfo.lg.id), league);
            }

            Match match = new MatchFb(matchInfo);
            league.getMatchList().add(match);


            if(mMapMatch.get(String.valueOf(match.getId())) == null){
                mMapMatch.put(String.valueOf(match.getId()), match);
                mMatchList.add(match);
            }else{
                int index = mMatchList.indexOf(mMapMatch.get(String.valueOf(match.getId())));
                if(index > -1) {
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

        Map<String, League> mapLeague = new HashMap<>();

        if(!mGoingOnLeagueList.isEmpty() && mLeagueList.isEmpty()){
            mLeagueList.addAll(mGoingOnLeagueList);
            League league = mLeagueList.get(0).instance();
            league.setHead(true);
            league.setMatchCount(matchInfoList.size());
            mLeagueList.add(league);
            mGoingOnLeagueList.clear();
        }

        for (MatchInfo matchInfo : matchInfoList) {
            League league = mMapLeague.get(String.valueOf(matchInfo.lg.id));

            if (league == null) {
                league = new LeagueFb(matchInfo.lg);
                mapLeague.put(String.valueOf(matchInfo.lg.id), league);

                mLeagueList.add(league);
                mMapLeague.put(String.valueOf(matchInfo.lg.id), league);
            }

            Match match = new MatchFb(matchInfo);
            league.getMatchList().add(match);


            if(mMapMatch.get(String.valueOf(match.getId())) == null){
                mMapMatch.put(String.valueOf(match.getId()), match);
                mMatchList.add(match);
            }else{
                int index = mMatchList.indexOf(mMapMatch.get(String.valueOf(match.getId())));
                if(index > -1) {
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

        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchFb(matchInfo);
            mChampionMatchList.add(match);
            mChampionMatchMap.put(String.valueOf(match.getId()), match);
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
            Match newMatch = new MatchFb(matchInfo);
            newMatchList.add(newMatch);
        }

        List<Option> newOptonList = getMatchOptionList(newMatchList);
        List<Option> oldOptonList = getMatchOptionList(mMatchList);

        for (Option newOption : newOptonList) {
            for (Option oldOption : oldOptonList) {
                if (oldOption != null && newOption != null
                        && oldOption.getOdd() != newOption.getOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getOdd());
                    break;
                }
            }
        }

        for (Match match : newMatchList) {
            Match oldMatch = mMapMatch.get(String.valueOf(match.getId()));

            if (oldMatch != null) {
                int index = mMatchList.indexOf(oldMatch);
                if(index > -1) {
                    mMatchList.set(mMatchList.indexOf(oldMatch), match);
                }
            }
        }

        for (Match match : newMatchList) {
            for (League league : mLeagueList) {
                if (!league.isHead() && league.isExpand()) {
                    Match oldMatch = mMapMatch.get(String.valueOf(match.getId()));
                    if(oldMatch != null) {
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

        List<Option> newOptonList = getMatchOptionList(newMatchList);
        List<Option> oldOptonList = getMatchOptionList(mChampionMatchList);

        for (Option newOption : newOptonList) {
            for (Option oldOption : oldOptonList) {
                if (oldOption != null && newOption != null
                        && oldOption.getOdd() != newOption.getOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getOdd());
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
            PlayGroup newPlayGroup = new PlayGroupFb(match.getPlayTypeList());
            newPlayGroup.getPlayGroupList();

            for (PlayType playType : newPlayGroup.getPlayTypeList()) {
                playType.getOptionLists();
                for (Option option : playType.getOptionList()) {
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

    public void getFBGameTokenApi() {
        Disposable disposable = (Disposable) model.getApiService().getFBGameTokenApi()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<String>() {
                    @Override
                    public void onResult(String vo) {
                        SPUtils.getInstance().put("customer_service_url", vo);
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
