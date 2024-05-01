package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;
import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FB;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FBXC;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.FBHttpCallBack;
import com.xtree.bet.R;
import com.xtree.bet.bean.request.UploadExcetionReq;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.response.fb.MatchListRsp;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class LeagueListCallBack extends FBHttpCallBack<MatchListRsp> {
    private FBMainViewModel mViewModel;
    private boolean mHasCache;
    private boolean mIsTimerRefresh;
    private boolean mIsRefresh;
    private int mCurrentPage;
    private int mPlayMethodType;
    private int mSportPos;
    private String mSportId;
    private int mOrderBy;
    private List<Long> mLeagueIds;
    private int mSearchDatePos;
    private int mOddType;
    private List<Long> mMatchids;
    private boolean mNeedSecondStep;
    private int mFinalType;
    /**
     * 是否获取今日中未开赛比赛列表
     */
    private boolean mIsStepSecond;
    private boolean mNoLiveMatch;
    private Map<String, League> mMapSportType = new HashMap<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<League> mGoingOnLeagueList = new ArrayList<>();
    private Map<String, League> mMapLeague = new HashMap<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    private List<Match> mMatchList = new ArrayList<>();
    private League mNoLiveheaderLeague;

    public Map<String, League> getMapSportType() {
        return mMapSportType;
    }

    public List<League> getLeagueList() {
        return mLeagueList;
    }

    public List<League> getGoingOnLeagueList() {
        return mGoingOnLeagueList;
    }

    public Map<String, League> getMapLeague() {
        return mMapLeague;
    }

    public Map<String, Match> getMapMatch() {
        return mMapMatch;
    }

    public List<Match> getMatchList() {
        return mMatchList;
    }

    public League getNoLiveheaderLeague() {
        return mNoLiveheaderLeague;
    }

    public void saveLeague() {
        if (!mIsRefresh || mIsStepSecond) {
            mLeagueList = mViewModel.getmLeagueList();
            mGoingOnLeagueList = mViewModel.getGoingOnLeagueList();
            mMapLeague = mViewModel.getMapLeague();
            mMatchList = mViewModel.getMatchList();
            mMapMatch = mViewModel.getMapMatch();
            mMapSportType = mViewModel.getMapSportType();
            mNoLiveheaderLeague = mViewModel.getNoLiveheaderLeague();
        }
    }

    public LeagueListCallBack(FBMainViewModel viewModel, boolean hasCache, boolean isTimerRefresh, boolean isRefresh,
                              int currentPage, int playMethodType, int sportPos, String sportId,
                              int orderBy, List<Long> leagueIds, int searchDatePos, int oddType, List<Long> matchids,
                              boolean needSecondStep, int finalType, boolean isStepSecond) {
        mViewModel = viewModel;
        mHasCache = hasCache;
        mIsTimerRefresh = isTimerRefresh;
        mIsRefresh = isRefresh;
        mCurrentPage = currentPage;
        mPlayMethodType = playMethodType;
        mSportPos = sportPos;
        mSportId = sportId;
        mOrderBy = orderBy;
        mLeagueIds = leagueIds;
        mSearchDatePos = searchDatePos;
        mOddType = oddType;
        mMatchids = matchids;
        mNeedSecondStep = needSecondStep;
        mFinalType = finalType;
        mIsStepSecond = isStepSecond;
        saveLeague();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mIsTimerRefresh && !mHasCache) {
            mViewModel.getUC().getShowDialogEvent().postValue("");
        }
    }

    @Override
    public void onResult(MatchListRsp matchListRsp) {
        if (mIsTimerRefresh) {
            if (matchListRsp.records.size() != mMatchids.size()) {
                List<Long> matchIdList = new ArrayList<>();
                mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, matchIdList, mPlayMethodType, mSearchDatePos, mOddType, false, true);
            } else {
                setOptionOddChange(matchListRsp.records);
                mViewModel.leagueLiveTimerListData.postValue(mLeagueList);
            }
            return;
        }
        mViewModel.firstNetworkFinishData.call();
        synchronized (this) {
            if(mIsRefresh){
                mNoLiveheaderLeague = null;
            }
            if (mIsRefresh && !mNeedSecondStep) {
                mLeagueList.clear();
                mMapLeague.clear();
                mMapSportType.clear();
            }

            if (!mNeedSecondStep) {
                mViewModel.getUC().getDismissDialogEvent().call();
                if (mIsRefresh) {
                    if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                        mViewModel.loadMoreWithNoMoreData();
                    } else {
                        mViewModel.finishRefresh(true);
                    }
                } else {
                    if (matchListRsp != null && mCurrentPage == matchListRsp.getPages()) {
                        mViewModel.loadMoreWithNoMoreData();
                    } else {
                        mViewModel.finishLoadMore(true);
                    }
                }
            }

            if (mFinalType == 1) { // 滚球
                if (mNeedSecondStep) {
                    mIsStepSecond = true;
                    leagueGoingList(matchListRsp.records);
                    mViewModel.saveLeague(this);
                    mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, 3, mSearchDatePos, mOddType, false, mIsRefresh, mIsStepSecond);
                } else {
                    leagueAdapterList(matchListRsp.records);
                    mViewModel.leagueLiveListData.postValue(mLeagueList);
                    if (mCurrentPage == 1) {
                        SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + mSearchDatePos + mSportId, new Gson().toJson(mLeagueList));
                    }
                    mViewModel.saveLeague(this);
                }
            } else {
                leagueAdapterList(matchListRsp.records);
                mViewModel.leagueNoLiveListData.postValue(mLeagueList);
                mViewModel.saveLeague(this);
                if (mCurrentPage == 1) {
                    SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + mSearchDatePos + mSportId, new Gson().toJson(mLeagueList));
                }
                mIsStepSecond = false;
            }
            mHasCache = false;
        }
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof ResponseThrowable) {
            if(((ResponseThrowable) t).isHttpError){
                UploadExcetionReq uploadExcetionReq = new UploadExcetionReq();
                String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
                String domainUrl = null;
                if (TextUtils.equals(platform, PLATFORM_FBXC)) {
                    domainUrl = SPUtils.getInstance().getString(SPKeyGlobal.FBXC_API_SERVICE_URL);
                    uploadExcetionReq.setLogTag("fbxc_url_error");
                } else if (TextUtils.equals(platform, PLATFORM_FB)) {
                    domainUrl = SPUtils.getInstance().getString(SPKeyGlobal.FB_API_SERVICE_URL);
                    uploadExcetionReq.setLogTag("fb_url_error");
                }
                uploadExcetionReq.setApiUrl(domainUrl + "/v1/match/getList");
                uploadExcetionReq.setLogType("" + ((ResponseThrowable) t).code);
                uploadExcetionReq.setMsg(((ResponseThrowable) t).message);
                mViewModel.firstNetworkExceptionData.postValue(uploadExcetionReq);
            }else if (((ResponseThrowable) t).code == CODE_14010) {
                mViewModel.getGameTokenApi();
            } else {
                mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, mPlayMethodType, mSearchDatePos, mOddType, mIsTimerRefresh, mIsRefresh, mIsStepSecond);
            }
        }
    }

    private void leagueGoingList(List<MatchInfo> matchInfoList) {
        if (matchInfoList.isEmpty()) {
            mViewModel.mNoLiveMatch = true;
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
                if (mViewModel.mNoLiveMatch) {
                    mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
                } else {
                    mNoLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_all_league));
                }
                mLeagueList.add(mNoLiveheaderLeague);
            }
            mViewModel.mNoLiveMatch = false;
        } /*else if(mCurrentPage == 1){
            if (noLiveMatch) {
                noLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_game_waiting));
            } else {
                noLiveheaderLeague.setLeagueName(Utils.getContext().getResources().getString(R.string.bt_all_league));
            }
            mLeagueList.add(noLiveheaderLeague);
            CfLog.e("======noLiveheaderLeague == null========");
            //noLiveMatch = false;
        }*/
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

}
