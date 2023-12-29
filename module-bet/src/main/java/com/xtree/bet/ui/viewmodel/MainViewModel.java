package com.xtree.bet.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.MatchTypeInfo;
import com.xtree.bet.bean.response.MatchTypeStatisInfo;
import com.xtree.bet.bean.response.StatisticalInfo;
import com.xtree.bet.bean.request.PBListReq;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.response.LeagueItem;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.response.MatchInfo;
import com.xtree.bet.bean.response.MatchListRsp;
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

import com.xtree.base.net.HttpCallBack;

import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class MainViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();

    public SingleLiveData<String[]> playMethodTab = new SingleLiveData<>();
    public SingleLiveData<List<Date>> playSearchData = new SingleLiveData<>();
    public SingleLiveData<String[]> sportItemData = new SingleLiveData<>();
    public SingleLiveData<LeagueItem> leagueItemData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueWaitingListData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueWaitingTimerListData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueGoingOnListData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueGoingOnTimerListData = new SingleLiveData<>();
    public SingleLiveData<List<Match>> championMatchTimerListData = new SingleLiveData<>();
    public SingleLiveData<List<Match>> championMatchListData = new SingleLiveData<>();
    public SingleLiveData<BetContract> betContractListData = new SingleLiveData<>();
    /**
     * 赛事统计数据
     */
    public SingleLiveData<Map<String, List<Integer>>> statisticalData = new SingleLiveData<>();

    private List<Match> mMatchList = new ArrayList<>();
    private List<Match> mChampionMatchList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<League> mGoingOnLeagueList = new ArrayList<>();
    Map<String, League> mMapLeague = new HashMap<>();
    private Map<String, List<Integer>> sportCountMap = new HashMap<>();
    private List<Date> dateList = new ArrayList<>();

    private int currentPage = 1;
    private int goingOnPageSize = 300;
    private int pageSize = 50;
    private List<League> mUpdateLeagueList;

    public MainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void setPlayMethodTabData() {
        playMethodTab.setValue(Constants.PLAY_METHOD_NAMES);
    }

    public void setPlaySearchDateData() {
        List<Date> dateList = new ArrayList<>();
        dateList.addAll(TimeUtils.getNextDays(9));
        this.dateList = dateList;
        playSearchData.setValue(dateList);
    }

    public void setSportItems() {
        sportItemData.postValue(SportTypeContants.SPORT_NAMES);
    }

    public void setFbLeagueData() {
        //leagueItemData.setValue(new LeagueItem());
    }

    public void setUpdateLeagueList(List<League> updateLeague) {
        mUpdateLeagueList = updateLeague;
    }

    public String getScore(List<League> leagueList, int matchId) {
        for (League league : leagueList) {
            for (Match match : league.getMatchList()) {
                if (matchId == match.getId()) {
                    if (match.getScore(Constants.SCORE_TYPE_SCORE) != null && match.getScore(Constants.SCORE_TYPE_SCORE).size() > 1) {
                        String scoreMain = String.valueOf(match.getScore(Constants.SCORE_TYPE_SCORE).get(0));
                        String scoreVisitor = String.valueOf(match.getScore(Constants.SCORE_TYPE_SCORE).get(1));
                        return scoreMain + "-" + scoreVisitor;
                    }
                }
            }
        }
        return "";
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
    public void getLeagueList(int sportId, int orderBy, int[] leagueIds, List<Integer> matchids, int playMethodType, int searchDatePos, int oddType, boolean isTimedRefresh, boolean isRefresh) {
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

        PBListReq pbListReq = new PBListReq();
        pbListReq.setSportId(sportId);
        pbListReq.setType(type);
        pbListReq.setOrderBy(orderBy);
        pbListReq.setLeagueIds(leagueIds);
        pbListReq.setMatchIds(matchids);
        pbListReq.setCurrent(currentPage);
        pbListReq.setOddType(oddType);

        String startTime;
        String endTime;


        if (type != 1 && type != 3) {
            if (searchDatePos == dateList.size() - 1) {
                pbListReq.setBeginTime(dateList.get(dateList.size() - 1).getTime() + "");
                pbListReq.setEndTime(TimeUtils.addDays(dateList.get(dateList.size() - 1), 30).getTime() + "");
                startTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getBeginTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
                endTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getEndTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
            } else if (searchDatePos != 0) {
                String start = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
                String end = TimeUtils.parseTime(TimeUtils.addDays(dateList.get(searchDatePos), 1), TimeUtils.FORMAT_YY_MM_DD) + " 11:59:59";

                pbListReq.setBeginTime(TimeUtils.strFormatDate(start, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime() + "");
                pbListReq.setEndTime(TimeUtils.strFormatDate(end, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime() + "");
                /*startTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getBeginTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
                endTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getEndTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);*/
            }
        }

        if (type == 1) {// 滚球
            pbListReq.setSize(goingOnPageSize);
        } else {
            pbListReq.setSize(pageSize);
        }

        if (isRefresh) {
            mLeagueList.clear();
            mMapLeague.clear();
        }

        Disposable disposable = (Disposable) model.getApiService().getFBList(pbListReq)
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
                                getLeagueList(sportId, orderBy, leagueIds, matchids, 3, searchDatePos, oddType, false, isRefresh);
                            }else{
                                leagueAdapterList(matchListRsp.records);
                                leagueGoingOnListData.postValue(mLeagueList);
                            }
                        } else {
                            leagueAdapterList(matchListRsp.records);
                            leagueWaitingListData.postValue(mLeagueList);
                        }



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
    public void getChampionList(int sportId, int orderBy, int[] leagueIds, List<Integer> matchids, int playMethodType, int oddType, boolean isTimedRefresh, boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        PBListReq pbListReq = new PBListReq();
        pbListReq.setSportId(sportId);
        pbListReq.setType(playMethodType);
        pbListReq.setOrderBy(orderBy);
        pbListReq.setLeagueIds(leagueIds);
        pbListReq.setMatchIds(matchids);
        pbListReq.setCurrent(currentPage);
        pbListReq.setSize(300);
        pbListReq.setOddType(oddType);

        if (isRefresh) {
            mChampionMatchList.clear();
        }

        Disposable disposable = (Disposable) model.getApiService().getFBList(pbListReq)
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
    public void statistical() {

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
                            for (String sportId : SportTypeContants.SPORT_IDS) {
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
        }

    }

    /**
     * 设置赔率变化
     *
     * @param matchInfoList
     */
    private void setOptionOddChange(List<MatchInfo> matchInfoList) {
        List<Match> newMatchList = new ArrayList<>();
        Map<String, Match> map = new HashMap<>();

        for (Match match : mMatchList) {
            map.put(String.valueOf(match.getId()), match);
        }

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
            Match oldMatch = map.get(String.valueOf(match.getId()));
            if (oldMatch != null) {
                mMatchList.set(mMatchList.indexOf(oldMatch), match);
            }
        }

        for (League league : mUpdateLeagueList) {
            if (!league.isHead()) {
                for (Match match : newMatchList) {
                    Match oldMatch = map.get(String.valueOf(match.getId()));
                    int index = league.getMatchList().indexOf(oldMatch);
                    if (oldMatch != null && index > -1) {
                        league.getMatchList().set(index, match);
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
            map.put(String.valueOf(match.getId()), match);
        }

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
            Match oldMatch = map.get(String.valueOf(match.getId()));
            if (oldMatch != null) {
                mChampionMatchList.set(mChampionMatchList.indexOf(oldMatch), match);
            }
        }

    }

    private List<Option> getMatchOptionList(List<Match> matchList) {
        List<Option> optionList = new ArrayList<>();
        for (Match match : matchList) {
            PlayGroup newPlayGroup = new PlayGroup(match.getPlayTypeList());
            newPlayGroup.getPlayGroupList();

            for (PlayType playType : newPlayGroup.getPlayTypeList()) {
                playType.getOptionLists();
                for (Option option : playType.getOptionList()) {
                    if (playType.getOptionLists() != null && !playType.getOptionLists().isEmpty()) {
                        StringBuffer code = new StringBuffer();
                        code.append(match.getId());
                        code.append(playType.getPlayType());
                        code.append(playType.getPlayPeriod());
                        code.append(playType.getOptionLists().get(0).getId());
                        code.append(option.getOptionType());
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

    public void addSubscription() {
        mSubscription = RxBus.getDefault().toObservable(BetContract.class)
                .subscribe(betContract -> {
                    betContractListData.postValue(betContract);
                });
        addSubscribe(mSubscription);
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
