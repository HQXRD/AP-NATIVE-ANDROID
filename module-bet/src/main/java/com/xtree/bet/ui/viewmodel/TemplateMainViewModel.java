package com.xtree.bet.ui.viewmodel;

import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;
import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FBXC;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.response.HotLeagueInfo;
import com.xtree.bet.bean.response.fb.LeagueItem;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.util.MatchDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * Created by marquis
 */

public abstract class TemplateMainViewModel extends BaseBtViewModel implements MainViewModel {
    public static String[] PLAY_METHOD_NAMES = new String[]{"今日", "滚球", "早盘", "串关", "冠军"};
    /**
     * 体育分类ID，与SPORT_NAMES一一对应
     */
    private Disposable mSubscription;

    public List<Date> dateList = new ArrayList<>();

    public List<Long> hotLeagueList = new ArrayList<>();

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
    public SingleLiveData<Integer> hotMatchCountData = new SingleLiveData<>();
    public SingleLiveData<Void> tokenInvalidEvent = new SingleLiveData<>();

    /**
     * 赛事统计数据
     */
    public SingleLiveData<Map<String, List<Integer>>> statisticalData = new SingleLiveData<>();
    public SingleLiveData<List<League>> settingLeagueData = new SingleLiveData<>();
    public Map<String, League> mMapSportType = new HashMap<>();
    public boolean noLiveMatch;
    public List<League> mLeagueList = new ArrayList<>();
    public List<League> mGoingOnLeagueList = new ArrayList<>();
    public League noLiveheaderLeague;
    public boolean mHasCache;

    public TemplateMainViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
        //SPORT_NAMES = SPORT_NAMES_TODAY_CG;
        Constants.SPORT_ICON = Constants.SPORT_ICON_TODAY_CG;
        //sportItemData.postValue(SPORT_NAMES);
    }

    public void setPlayMethodTabData() {
        playMethodTab.setValue(PLAY_METHOD_NAMES);
    }

    public void setPlaySearchDateData() {
        List<Date> dateList = new ArrayList<>();
        dateList.addAll(TimeUtils.getNextDays(9));
        this.dateList = dateList;
        playSearchData.setValue(dateList);
    }

    public abstract void setSportIds(int playMethodPos);

    public void setSportItems(int playMethodPos, int playMethodType) {

    }

    public void setSportIcons(int playMethodPos) {
        if (playMethodPos == 0 || playMethodPos == 3) {
            Constants.SPORT_ICON = Constants.SPORT_ICON_TODAY_CG;
        } else if (playMethodPos == 1) {
            Constants.SPORT_ICON = Constants.SPORT_ICON_LIVE;
        } else {
            Constants.SPORT_ICON = Constants.SPORT_ICON_NOMAL;
        }
    }

    public String getScore(List<League> leagueList, long matchId) {
        for (League league : leagueList) {
            for (Match match : league.getMatchList()) {
                if (matchId == match.getId()) {
                    List<Integer> scoreList = match.getScore(Constants.getScoreType());
                    if (scoreList != null && scoreList.size() > 1) {
                        String scoreMain = String.valueOf(scoreList.get(0));
                        String scoreVisitor = String.valueOf(scoreList.get(1));
                        return scoreMain + "-" + scoreVisitor;
                    }
                }
            }
        }
        return "";
    }

    public void addSubscription() {
        mSubscription = RxBus.getDefault().toObservable(BetContract.class)
                .subscribe(betContract -> {
                    betContractListData.postValue(betContract);
                });
        addSubscribe(mSubscription);
    }

    /**
     * 获取热门联赛
     */
    public void getHotLeague(String platform) {
        Map<String, String> map = new HashMap<>();
        map.put("fields", !TextUtils.equals(platform, PLATFORM_PM) ? "fbxc_popular_leagues" : "obg_popular_leagues");

        Disposable disposable = (Disposable) model.getBaseApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<HotLeagueInfo>() {
                    @Override
                    public void onResult(HotLeagueInfo hotLeagueInfo) {
                        List<String> hotLeagues = !TextUtils.equals(platform, PLATFORM_PM) ? hotLeagueInfo.fbxc_popular_leagues : hotLeagueInfo.obg_popular_leagues;
                        for (String leagueId : hotLeagues) {
                            hotLeagueList.add(Long.valueOf(leagueId));
                        }
                        getHotMatchCount(!TextUtils.equals(platform, PLATFORM_PM) ? 6 : 3, hotLeagueList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                        getHotLeague(platform);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 新建进行中的联赛分类信息
     *
     * @param liveheaderLeague
     */
    public void buildLiveHeaderLeague(League liveheaderLeague) {
        liveheaderLeague.setHead(true);
        liveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
        liveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_going_on));
        mGoingOnLeagueList.add(liveheaderLeague);
    }

    /**
     * 新建未开赛联赛分类信息
     *
     * @param league
     */
    public void buildNoLiveHeaderLeague(League league, int noLiveMatchSize) {
        if (!mGoingOnLeagueList.isEmpty() && mLeagueList.isEmpty()) {
            mLeagueList.addAll(mGoingOnLeagueList);
            noLiveheaderLeague = league;
            noLiveheaderLeague.setHead(true);
            noLiveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
            noLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
            mLeagueList.add(noLiveheaderLeague);
            mGoingOnLeagueList.clear();
        } else if (noLiveheaderLeague == null) {
            if (noLiveMatchSize > 0) {
                noLiveheaderLeague = league;
                noLiveheaderLeague.setHead(true);
                noLiveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
                if (noLiveMatch) {
                    noLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
                } else {
                    noLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_all_league));
                }
                mLeagueList.add(noLiveheaderLeague);
            }
            noLiveMatch = false;
        }
    }

    /**
     * 新建进行中比赛种类头部信息，足球、篮球等
     *
     * @param mapSportType
     * @param match
     */
    public void buildLiveSportHeader(Map<String, League> mapSportType, Match match, League league) {
        League sportHeader = mapSportType.get(match.getSportId());
        if (sportHeader == null) {
            League sportHeaderLeague = league;
            sportHeaderLeague.setHead(true);
            sportHeaderLeague.setHeadType(League.HEAD_TYPE_SPORT_NAME);
            sportHeaderLeague.setLeagueName(match.getSportName());
            sportHeaderLeague.setMatchCount(1);
            mGoingOnLeagueList.add(sportHeaderLeague);
            mapSportType.put(match.getSportId(), sportHeaderLeague);
        } else {
            sportHeader.setMatchCount(1);
        }
    }

    /**
     * 新建未开赛比赛种类头部信息，足球、篮球等
     *
     * @param match
     * @param league
     */
    public void buildNoLiveSportHeader(Match match, League league) {
        League sportHeader = mMapSportType.get(match.getSportId());
        if (sportHeader == null) {
            League sportHeaderLeague = league;
            sportHeaderLeague.setHead(true);
            sportHeaderLeague.setHeadType(League.HEAD_TYPE_SPORT_NAME);
            sportHeaderLeague.setLeagueName(match.getSportName());
            sportHeaderLeague.setMatchCount(1);
            if (!mGoingOnLeagueList.isEmpty() && mLeagueList.isEmpty()) { // 进行中
                mGoingOnLeagueList.add(sportHeaderLeague);
            } else {  // 未开赛
                mLeagueList.add(sportHeaderLeague);
            }
            mMapSportType.put(match.getSportId(), sportHeaderLeague);
        } else {
            sportHeader.setMatchCount(1);
        }
    }

    public void showCache(String sportId, int playMethodType, int searchDatePos) {
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        String json = SPUtils.getInstance().getString(BT_LEAGUE_LIST_CACHE + playMethodType + searchDatePos + sportId);
        mHasCache = !TextUtils.isEmpty(json);
        json = TextUtils.isEmpty(json) ? "[]" : json;
        Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Match.class, new MatchDeserializer()).create();
        Type listType = TextUtils.equals(platform, PLATFORM_PM) ? new TypeToken<List<LeaguePm>>() { }.getType() : new TypeToken<List<LeagueFb>>() { }.getType();
        List<League> leagueList = gson.fromJson(json, listType);

        if (playMethodType == 1) { // 滚球
            leagueGoingOnListData.postValue(leagueList);
        } else {
            leagueWaitingListData.postValue(leagueList);
        }
        CfLog.e("=========mHasCache=========" + mHasCache);
        /*if(!mHasCache) {
            CfLog.e("=========mHasCache=========" + mHasCache);
        }*/
    }

    public void showChampionCache(String sportId, int playMethodType) {
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        String json = SPUtils.getInstance().getString(BT_LEAGUE_LIST_CACHE + playMethodType + sportId);
        mHasCache = !TextUtils.isEmpty(json);
        json = TextUtils.isEmpty(json) ? "[]" : json;
        Type listType = TextUtils.equals(platform, PLATFORM_PM) ? new TypeToken<List<MatchPm>>() { }.getType() : new TypeToken<List<MatchFb>>() { }.getType();
        List<Match> matchList = new Gson().fromJson(json, listType);

        championMatchListData.postValue(matchList);

        CfLog.e("=========mHasCache=========" + mHasCache);
    }

}
