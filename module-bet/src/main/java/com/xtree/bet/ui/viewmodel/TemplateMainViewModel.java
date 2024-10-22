package com.xtree.bet.ui.viewmodel;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.TimeUtils;
import com.xtree.base.vo.BaseBean;
import com.xtree.bet.R;
import com.xtree.bet.bean.request.UploadExcetionReq;
import com.xtree.bet.bean.response.HotLeagueInfo;
import com.xtree.bet.bean.response.fb.FBAnnouncementInfo;
import com.xtree.bet.bean.response.fb.HotLeague;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SportTypeItem;
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
    public MutableLiveData<Void> reNewViewModel = new MutableLiveData<>();
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();

    public SingleLiveData<String[]> playMethodTab = new SingleLiveData<>();
    public SingleLiveData<List<Date>> playSearchData = new SingleLiveData<>();
    public SingleLiveData<String[]> sportItemData = new SingleLiveData<>();
    public SingleLiveData<List<HotLeague>> leagueItemData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueNoLiveListData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueNoLiveTimerListData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueLiveListData = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueLiveTimerListData = new SingleLiveData<>();
    public SingleLiveData<List<Match>> championMatchTimerListData = new SingleLiveData<>();
    public SingleLiveData<List<Match>> championMatchListData = new SingleLiveData<>();
    public SingleLiveData<BetContract> betContractListData = new SingleLiveData<>();
    public SingleLiveData<Integer> hotMatchCountData = new SingleLiveData<>();
    public SingleLiveData<Integer> hotEmptyMatchCountData = new SingleLiveData<>();
    /**
     * 请求量过多
     */
    public SingleLiveData<Void> tooManyRequestsEvent = new SingleLiveData<>();

    /**
     * 赛事统计数据
     */
    public SingleLiveData<Map<String, List<SportTypeItem>>> statisticalData = new SingleLiveData<>();
    public SingleLiveData<List<League>> settingLeagueData = new SingleLiveData<>();
    public SingleLiveData<List<League>> resultLeagueData = new SingleLiveData<>();
    public SingleLiveData<Map<String, List<SportTypeItem>>> resultData = new SingleLiveData<>();

    /**
     * 赛事公告
     */
    public SingleLiveData<List<FBAnnouncementInfo.RecordsDTO>> announcementData = new SingleLiveData<>();
    /**
     * 第一次进入主页时获取列表数据完成
     */
    public SingleLiveData<Void> firstNetworkFinishData = new SingleLiveData<>();
    /**
     * 第一次进入主页时获取列表数据发生异常
     */
    public SingleLiveData<UploadExcetionReq> firstNetworkExceptionData = new SingleLiveData<>();
    public SingleLiveData<Map<String, String>> agentSwitchData = new SingleLiveData<>();
    public Map<String, League> mMapSportType = new HashMap<>();
    public boolean mNoLiveMatch;
    public List<League> mLeagueList = new ArrayList<>();
    public List<League> mGoingOnLeagueList = new ArrayList<>();
    public League mNoLiveheaderLeague;
    /**
     * 正在进行中的比赛
     */
    public List<BaseBean> mLiveMatchList = new ArrayList<>();
    /**
     * 未开始的比赛
     */
    public List<BaseBean> mNoliveMatchList = new ArrayList<>();
    public boolean mHasCache;
    public int mCurrentPage = 1;
    /**
     * 是否获取今日中未开赛比赛列表
     */
    public boolean mIsStepSecond;
    /**
     * 当前选择的玩法
     */
    public int mPlayMethodType;
    public String mSearchWord;
    public boolean mIsChampion;

    public Map<String, League> getMapSportType() {
        return mMapSportType;
    }

    public League getNoLiveheaderLeague() {
        return mNoLiveheaderLeague;
    }

    public List<League> getLeagueList() {
        return mLeagueList;
    }

    public List<League> getGoingOnLeagueList() {
        return mGoingOnLeagueList;
    }

    public List<BaseBean> getLiveMatchList() {
        return mLiveMatchList;
    }

    public List<BaseBean> getNoliveMatchList() {
        return mNoliveMatchList;
    }

    public TemplateMainViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
        //SPORT_NAMES = SPORT_NAMES_TODAY_CG;
        //Constants.SPORT_ICON = Constants.SPORT_ICON_TODAY_CG;
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


    public void setSportItems(int playMethodPos, int playMethodType) {

    }

    public void setHotLeagueList(String sportName) {
        if (TextUtils.equals("足球", sportName)) {
            leagueItemData.postValue(Constants.getHotFootBallLeagueTopList());
        } else if (TextUtils.equals("篮球", sportName)) {
            leagueItemData.postValue(Constants.getHotBasketBallLeagueTopList());
        } else {
            leagueItemData.postValue(null);
        }
    }

    public void setSportIcons(int playMethodPos) {
        //if (playMethodPos == 0 || playMethodPos == 3) {
        //    Constants.SPORT_ICON = Constants.SPORT_ICON_TODAY_CG;
        //} else if (playMethodPos == 1) {
        //    Constants.SPORT_ICON = Constants.SPORT_ICON_LIVE;
        //} else {
        //    Constants.SPORT_ICON = Constants.SPORT_ICON_NOMAL;
        //}
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
     * 获取场馆代理开关
     */
    public void getGameSwitch() {
        Disposable disposable = (Disposable) model.getBaseApiService().getGameSwitch()
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<Map<String, String>>() {
                    @Override
                    public void onResult(Map<String, String> map) {
                        agentSwitchData.postValue(map);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 获取热门联赛
     */
    public void getHotLeague(String platform) {
        Map<String, String> map = new HashMap<>();
        map.put("fields", !TextUtils.equals(platform, PLATFORM_PM) && !TextUtils.equals(platform, PLATFORM_PMXC) ? "fbxc_popular_leagues" : "obg_popular_leagues");

        Disposable disposable = (Disposable) model.getBaseApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<HotLeagueInfo>() {
                    @Override
                    public void onResult(HotLeagueInfo hotLeagueInfo) {
                        List<String> hotLeagues = !TextUtils.equals(platform, PLATFORM_PM) && !TextUtils.equals(platform, PLATFORM_PMXC) ? hotLeagueInfo.fbxc_popular_leagues : hotLeagueInfo.obg_popular_leagues;
                        for (String leagueId : hotLeagues) {
                            hotLeagueList.add(Long.valueOf(leagueId));
                        }
                        getHotMatchCount(!TextUtils.equals(platform, PLATFORM_PM) && !TextUtils.equals(platform, PLATFORM_PMXC) ? 6 : 3, hotLeagueList);
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
    /*public void buildNoLiveHeaderLeague(League league, int noLiveMatchSize) {
        if (!mGoingOnLeagueList.isEmpty() && mLeagueList.isEmpty()) {
            mLeagueList.addAll(mGoingOnLeagueList);
            mNoLiveheaderLeague = league;
            mNoLiveheaderLeague.setHead(true);
            mNoLiveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
            mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
            mLeagueList.add(mNoLiveheaderLeague);
            mGoingOnLeagueList.clear();
        } else if (mNoLiveheaderLeague == null) {
            if (noLiveMatchSize > 0) {
                mNoLiveheaderLeague = league;
                mNoLiveheaderLeague.setHead(true);
                mNoLiveheaderLeague.setHeadType(League.HEAD_TYPE_LIVE_OR_NOLIVE);
                if (mNoLiveMatch) {
                    mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
                } else {
                    mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_all_league));
                }
                mLeagueList.add(mNoLiveheaderLeague);
            }
            mNoLiveMatch = false;
        }
    }*/

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
        if (TextUtils.isEmpty(mSearchWord)) {
            String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
            String json = SPUtils.getInstance().getString(BT_LEAGUE_LIST_CACHE + playMethodType + searchDatePos + sportId);
            mHasCache = !TextUtils.isEmpty(json);
            json = TextUtils.isEmpty(json) ? "[]" : json;
            Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Match.class, new MatchDeserializer()).create();
            Type listType = TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC) ? new TypeToken<List<LeaguePm>>() {
            }.getType() : new TypeToken<List<LeagueFb>>() {
            }.getType();
            List<League> leagueList = gson.fromJson(json, listType);

            if (playMethodType == 1) { // 滚球
                leagueLiveListData.postValue(leagueList);
            } else {
                leagueNoLiveListData.postValue(leagueList);
            }
        }
    }

    public void showChampionCache(String sportId, int playMethodType) {
        if (TextUtils.isEmpty(mSearchWord)) {
            String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
            String json = SPUtils.getInstance().getString(BT_LEAGUE_LIST_CACHE + playMethodType + sportId);
            mHasCache = !TextUtils.isEmpty(json);
            json = TextUtils.isEmpty(json) ? "[]" : json;
            Type listType = TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC) ? new TypeToken<List<MatchPm>>() {
            }.getType() : new TypeToken<List<MatchFb>>() {
            }.getType();
            List<Match> matchList = new Gson().fromJson(json, listType);

            championMatchListData.postValue(matchList);
        }
    }

}
