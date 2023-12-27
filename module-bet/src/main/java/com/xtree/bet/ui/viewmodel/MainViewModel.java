package com.xtree.bet.ui.viewmodel;

import android.app.Application;
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
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SportTypeContants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public SingleLiveData<BetContract> betContractListData = new SingleLiveData<>();
    /**
     * 赛事统计数据
     */
    public SingleLiveData<Map<String, List<Integer>>> statisticalData = new SingleLiveData<>();


    private Map<String, List<Integer>> sportCountMap = new HashMap<>();
    private List<Date> dateList = new ArrayList<>();

    private int currentPage = 1;
    private int goingOnPageSize = 300;
    private int pageSize = 30;

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

    public String getScore(List<League> leagueList, int matchId){
        for(League league : leagueList){
            for (Match match : league.getMatchList()){
                if(matchId == match.getId()){
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
     * @param sportId
     * @param orderBy
     * @param leagueIds
     * @param matchids
     * @param playMethodType
     * @param searchDatePos 查询时间列表中的位置
     * @param isTimedRefresh 是否定时刷新 true-是，false-否
     */
    public void getLeagueList(int sportId, int orderBy, int[] leagueIds, List<Integer> matchids, int playMethodType, int searchDatePos, boolean isTimedRefresh) {
        int type = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? 1 : playMethodType;
        boolean flag = playMethodType == 6 || (playMethodType == 2 && searchDatePos == 0) ? true : false;
        Log.e("test", "=========searchDatePos========" + searchDatePos);
        PBListReq pbListReq = new PBListReq();
        pbListReq.setSportId(sportId);
        pbListReq.setType(type);
        pbListReq.setOrderBy(orderBy);
        pbListReq.setLeagueIds(leagueIds);
        pbListReq.setMatchIds(matchids);
        pbListReq.setCurrent(currentPage);

        String startTime;
        String endTime;



        if(type != 1 && type != 3) {
            if (searchDatePos == dateList.size() - 1) {
                pbListReq.setBeginTime(dateList.get(dateList.size() - 1).getTime() + "");
                pbListReq.setEndTime(TimeUtils.addDays(dateList.get(dateList.size() - 1), 30).getTime() + "");
                startTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getBeginTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
                endTime = TimeUtils.longFormatString(Long.valueOf(pbListReq.getEndTime()), TimeUtils.FORMAT_YY_MM_DD_HH_MM_SS);
            } else if (searchDatePos != 0){
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
        }else {
            pbListReq.setSize(pageSize);
        }

        Disposable disposable = (Disposable) model.getApiService().getFBList(pbListReq)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MatchListRsp>() {
                    @Override
                    public void onResult(MatchListRsp matchListRsp) {
                        if (type == 1) { // 滚球
                            leagueGoingOnListData.postValue(leagueAdapterList(matchListRsp.records, true));
                            if(flag) {
                                getLeagueList(sportId, orderBy, leagueIds, matchids, 3, searchDatePos, isTimedRefresh);
                            }
                        } else {
                            if(isTimedRefresh){
                                leagueWaitingTimerListData.postValue(leagueAdapterList(matchListRsp.records, true));
                            }else{
                                leagueWaitingListData.postValue(leagueAdapterList(matchListRsp.records, true));
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
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

    /**
     * @param matchInfoList
     * @param isFb
     * @return
     */
    public static List<League> leagueAdapterList(List<MatchInfo> matchInfoList, boolean isFb) {
        List<League> leagueList = new ArrayList<>();
        Map<String, League> map = new HashMap<>();

        int index = 0;
        for (MatchInfo matchInfo : matchInfoList) {

            League leagueAdapter = map.get(String.valueOf(matchInfo.lg.id));
            if (leagueAdapter == null) {
                leagueAdapter = new LeagueFb(matchInfo.lg);
                leagueAdapter.setSort(index++);
                map.put(String.valueOf(matchInfo.lg.id), leagueAdapter);
            }

            Match matchFbAdapter = new MatchFb(matchInfo);
            leagueAdapter.getMatchList().add(matchFbAdapter);
        }
        leagueList.addAll(map.values());
        Collections.sort(leagueList, new Comparator<League>() {
            @Override
            public int compare(League league, League t1) {
                return league.getSort() - t1.getSort();
            }
        });
        return leagueList;
    }

    public void addSubscription() {
        mSubscription = RxBus.getDefault().toObservable(BetContract.class)
                .subscribe(betContract -> {
                    betContractListData.postValue(betContract);
                });
        addSubscribe(mSubscription);
    }

    private String readTextFile(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toString();
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
