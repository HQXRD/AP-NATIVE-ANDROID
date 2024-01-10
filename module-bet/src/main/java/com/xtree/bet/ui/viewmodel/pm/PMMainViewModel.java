package com.xtree.bet.ui.viewmodel.pm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.utils.TimeUtils;
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
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.MainViewModel;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class PMMainViewModel extends TemplateMainViewModel implements MainViewModel {
    public static String[] SPORT_NAMES = new String[]{"足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS = new String[]{"20103", "20104", "20106", "20107", "20112", "20110", "20108", "20113", "20109", "20111", "19", "51", "8"};

    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_SPECAIL = new String[]{"20203", "20204", "20206", "20207", "20212", "20210", "20208", "20213", "20209", "20211", "19", "51", "8"};
    /**
     * 默认冠军体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_CHAMPION_SPECAIL = new String[]{"20603", "20604", "20606", "20607", "20612", "20610", "20608", "20613", "20609", "20611", "19", "51", "8"};
    /**
     * 玩法ID，与PLAY_METHOD_NAMES一一对应
     */
    private String[] PLAY_METHOD_TYPES = new String[]{"3", "1", "4", "11", "100"};






    private List<Match> mMatchList = new ArrayList<>();
    private List<Match> mChampionMatchList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<League> mGoingOnLeagueList = new ArrayList<>();
    Map<String, League> mMapLeague = new HashMap<>();
    private Map<String, List<Integer>> sportCountMap = new HashMap<>();

    private List<MenuInfo> mMenuInfoList = new ArrayList<>();

    private int currentPage = 1;
    private int goingOnPageSize = 1;
    private int pageSize = 1;
    private List<League> mUpdateLeagueList = new ArrayList<>();

    public PMMainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
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
    public void getLeagueList(int sportPos, int sportId, int orderBy, List<Long> leagueIds, List<Long> matchidList, int playMethodType, int searchDatePos, int oddType, boolean isTimedRefresh, boolean isRefresh) {
        int type;
        boolean flag = false;
        PMListReq pmListReq = new PMListReq();
        pmListReq.setEuid(String.valueOf(sportId));

        if (isRefresh) {
            type = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? 1 : playMethodType;
            flag = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? true : false;
            currentPage = 1;
        } else {
            type = playMethodType == 3 || (playMethodType == 11 && searchDatePos == 0) ? 2 : playMethodType;
            currentPage++;
        }

        if(playMethodType != 2) {
            mPlayType = playMethodType;
        }

        if (type == 2) { // 今日和串关分开两次请求，第一次先获取滚球信息，第二次再获取今日赛事信息，type为2时，代表第二次的请求
            type = 3; // 不带时间查询条件时
            if(searchDatePos > 0){ // 带时间查询条件时
                type = 4;
            }
        }

        if (mMenuInfoList.isEmpty()) {
            pmListReq.setEuid(SPORT_IDS_SPECAIL[sportPos]);
        } else {
            for (MenuInfo menuInfo : mMenuInfoList) {
                boolean isFound = false;
                for (MenuInfo subMenu : menuInfo.subList) {
                    if (mPlayType == menuInfo.menuType) {
                        if (TextUtils.equals(SPORT_NAMES[sportPos], subMenu.menuName)) {
                            isFound = true;
                            pmListReq.setEuid(String.valueOf(subMenu.menuId));
                            break;
                        }
                    }
                }
                if(isFound){
                    break;
                }
            }
        }

        final int finalType = type;
        final boolean finalFlag = flag;

        pmListReq.setType(type);
        if(type == 1 && flag){
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
        pmListReq.setCpn(currentPage);
        //pmListReq.setOddType(oddType);
        pmListReq.setDevice("v2_h5_st");

        if(searchDatePos == dateList.size() - 1){
            String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
            pmListReq.setMd(String.valueOf(0 - TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
        }else if (searchDatePos > 0) {
            String time = TimeUtils.parseTime(dateList.get(searchDatePos), TimeUtils.FORMAT_YY_MM_DD) + " 12:00:00";
            pmListReq.setMd(String.valueOf(TimeUtils.strFormatDate(time, TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS).getTime()));
        }

        Flowable flowable = model.getPMApiService().noLiveMatchesPagePB(pmListReq);
        if (type == 1) {// 滚球
            pmListReq.setCps(goingOnPageSize);
            flowable = model.getPMApiService().liveMatchesPB(pmListReq);
        } else {
            pmListReq.setCps(pageSize);
        }

        if (isRefresh) {
            mLeagueList.clear();
            mMapLeague.clear();
        }
        PMHttpCallBack pmHttpCallBack;


        if (type == 1) {
            pmHttpCallBack = new PMHttpCallBack<List<MatchInfo>>() {

                @Override
                public void onResult(List<MatchInfo> data) {
                    if (isTimedRefresh) {
                        setOptionOddChange(data);
                        leagueGoingOnTimerListData.postValue(mLeagueList);
                        return;
                    }
                    if (finalType == 1) { // 滚球
                        if (finalFlag) {
                            leagueGoingList(data);
                            getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, 2, searchDatePos, oddType, false, isRefresh);
                        } else {
                            leagueAdapterList(data);
                            leagueGoingOnListData.postValue(mLeagueList);
                        }
                    } else {
                        leagueAdapterList(data);
                        leagueWaitingListData.postValue(mLeagueList);
                    }


                    if (isRefresh) {
                        finishRefresh(true);
                    } else {
                        if (data != null && data.isEmpty()) {
                            loadMoreWithNoMoreData();
                        } else {
                            finishLoadMore(true);
                        }
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                }
            };
        } else {
            pmHttpCallBack = new PMHttpCallBack<MatchListRsp>() {

                @Override
                public void onResult(MatchListRsp matchListRsp) {
                    if (isTimedRefresh) {
                        setOptionOddChange(matchListRsp.data);
                        leagueGoingOnTimerListData.postValue(mLeagueList);
                        return;
                    }
                    if (finalType == 1) { // 滚球
                        if (finalFlag) {
                            leagueGoingList(matchListRsp.data);
                            getLeagueList(sportPos, sportId, orderBy, leagueIds, matchidList, 2, searchDatePos, oddType, false, isRefresh);
                        } else {
                            leagueAdapterList(matchListRsp.data);
                            leagueGoingOnListData.postValue(mLeagueList);
                        }
                    } else {
                        leagueAdapterList(matchListRsp.data);
                        leagueWaitingListData.postValue(mLeagueList);
                    }


                    if (isRefresh) {
                        finishRefresh(true);
                    } else {
                        if (matchListRsp != null && matchListRsp.data != null && matchListRsp.data.isEmpty()) {
                            loadMoreWithNoMoreData();
                        } else {
                            finishLoadMore(true);
                        }
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                }
            };
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
     * @param isTimedRefresh
     * @param isRefresh
     */
    public void getChampionList(int sportPos, int sportId, int orderBy, List<Long> leagueIds, List<Long> matchids, int playMethodType, int oddType, boolean isTimedRefresh, boolean isRefresh) {

        if (isRefresh) {
            currentPage = 1;
        } else {
            currentPage++;
        }

        PMListReq pmListReq = new PMListReq();
        pmListReq.setType(playMethodType);
        pmListReq.setSort(orderBy);
        pmListReq.setCpn(currentPage);
        pmListReq.setCps(300);
        //pbListReq.setOddType(oddType);

        if (mMenuInfoList.isEmpty()) {
            pmListReq.setEuid(SPORT_IDS_CHAMPION_SPECAIL[sportPos]);
        } else {
            for (MenuInfo menuInfo : mMenuInfoList) {
                boolean isFound = false;
                for (MenuInfo subMenu : menuInfo.subList) {
                    if (playMethodType == menuInfo.menuType) {
                        if (TextUtils.equals(SPORT_NAMES[sportPos], subMenu.menuName)) {
                            isFound = true;
                            pmListReq.setEuid(String.valueOf(subMenu.menuId));
                            break;
                        }
                    }
                }
                if(isFound){
                    break;
                }
            }
        }

        if (isRefresh) {
            mChampionMatchList.clear();
        }

        Disposable disposable = (Disposable) model.getPMApiService().noLiveMatchesPagePB(pmListReq)
                .compose(RxUtils.schedulersTransformer()) //&#x7EBF;&#x7A0B;&#x8C03;&#x5EA6;
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<MatchListRsp>() {
                    @Override
                    public void onResult(MatchListRsp matchListRsp) {
                        if (isTimedRefresh) {
                            setChampionOptionOddChange(matchListRsp.data);
                            championMatchTimerListData.postValue(mChampionMatchList);
                            return;
                        }

                        championLeagueList(matchListRsp.data);
                        championMatchListData.postValue(mChampionMatchList);

                        if (isRefresh) {
                            finishRefresh(true);
                        } else {
                            if (matchListRsp.data != null && matchListRsp.data.isEmpty()) {
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


        Disposable disposable = (Disposable) model.getPMApiService().initPB()
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
                            }
                            List<Integer> sportCountList = new ArrayList<>();
                            for (String sportId : getSportId(playMethodType)) {
                                sportCountList.add(sslMap.get(sportId));
                            }
                            sportCountMap.put(String.valueOf(menuInfo.menuType), sportCountList);
                        }

                        /*if (playMethodType == 3 || playMethodType == 11) {
                            for (MenuInfo menuInfo : menuInfoList) {

                                if (menuInfo.menuType == 1) {
                                    for (MenuInfo subMenu : menuInfo.subList) {
                                        int index = Arrays.asList(SPORT_NAMES).indexOf(subMenu.menuName);
                                        if (index != -1) {
                                            SPORT_IDS_SPECAIL_GOINGON[index] = String.valueOf(subMenu.menuId);
                                        }
                                    }
                                } else if (menuInfo.menuType == 3) {
                                    for (MenuInfo subMenu : menuInfo.subList) {
                                        int index = Arrays.asList(SPORT_NAMES).indexOf(subMenu.menuName);
                                        if (index != -1) {
                                            SPORT_IDS_SPECAIL[index] = String.valueOf(subMenu.menuId);
                                        }
                                    }
                                }
                            }
                        }*/

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

        /*Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        map.put("sportId", String.valueOf(sportId));
        map.put("type", String.valueOf(type));

        Disposable disposable = (Disposable) model.getApiService().getOnSaleLeagues(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<LeagueInfo>>() {
                    @Override
                    public void onResult(List<LeagueInfo> leagueInfoList) {
                        List<League> leagueList = new ArrayList<>();
                        for (LeagueInfo leagueInfo : leagueInfoList) {
                            leagueList.add(new MatchPm(leagueInfo));
                        }
                        settingLeagueData.postValue(leagueList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);*/
    }

    @Override
    public String[] getPlayMethodTypes() {
        return PLAY_METHOD_TYPES;
    }

    @Override
    public String[] getSportId(int playMethodType) {
        return SPORT_IDS;
    }

    private void leagueGoingList(List<MatchInfo> matchInfoList) {

        Map<String, League> mapLeague = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
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

            Match match = new MatchPm(matchInfo);
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

        Map<String, League> mapLeague = new HashMap<>();

        if (!mGoingOnLeagueList.isEmpty() && mLeagueList.isEmpty()) {
            mLeagueList.addAll(mGoingOnLeagueList);
            League league = mLeagueList.get(0).instance();
            league.setHead(true);
            mLeagueList.add(league);
            mGoingOnLeagueList.clear();
        }

        for (MatchInfo matchInfo : matchInfoList) {
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

            Match match = new MatchPm(matchInfo);
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

        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchPm(matchInfo);
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
            Match newMatch = new MatchPm(matchInfo);
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
            Match newMatch = new MatchPm(matchInfo);
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
            PlayGroup newPlayGroup = new PlayGroupPm(match.getPlayTypeList());

            for (PlayType playType : newPlayGroup.getPlayTypeList()) {
                playType.getOptionLists();
                for (Option option : playType.getOptionList()) {
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

    public void getFBGameTokenApi() {
        Disposable disposable = (Disposable) model.getApiService().getFBGameTokenApi()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<String>() {
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
