package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401038;
import static com.xtree.bet.constant.SPKey.BT_LEAGUE_LIST_CACHE;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xtree.base.net.PMHttpCallBack;
import com.xtree.bet.R;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.MatchListRsp;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class PMLeagueListCallBack extends PMHttpCallBack<MatchListRsp> {

    private PMMainViewModel mViewModel;
    private boolean mHasCache;
    private boolean mIsTimerRefresh;
    private boolean mIsRefresh;
    private int mCurrentPage;
    private List<Long> mMatchids;
    private int mSportPos;
    private String mSportId;
    private int mOrderBy;
    private List<Long> mLeagueIds;
    private int mPlayMethodType;
    private int mSearchDatePos;
    private int mOddType;
    private boolean mIsStepSecond;
    private int mFinalType;
    private boolean mNoLiveMatch;
    private League mNoLiveheaderLeague;
    private Map<String, League> mMapSportType = new HashMap<>();
    private Map<String, League> mMapLeague = new HashMap<>();
    private List<League> mLeagueList = new ArrayList<>();
    private List<League> mGoingOnLeagueList = new ArrayList<>();
    private Map<String, Match> mMapMatch = new HashMap<>();
    private List<Match> mMatchList = new ArrayList<>();

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

    public PMLeagueListCallBack(PMMainViewModel viewModel, boolean hasCache, boolean isTimerRefresh, boolean isRefresh,
                                int currentPage, int playMethodType, int sportPos, String sportId, int orderBy, List<Long> leagueIds,
                                int searchDatePos, int oddType, List<Long> matchids, int finalType, boolean isStepSecond) {
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
        mIsStepSecond = isStepSecond;
        mFinalType = finalType;
        saveLeague();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mHasCache && !mIsStepSecond) {
            mViewModel.getUC().getShowDialogEvent().postValue("");
        }
    }

    @Override
    public void onResult(MatchListRsp matchListRsp) {
        mViewModel.getUC().getDismissDialogEvent().call();
        if (mIsRefresh) {
            if (!mIsStepSecond) {
                mLeagueList.clear();
                mMapLeague.clear();
                mMapSportType.clear();
            }
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

        leagueAdapterList(matchListRsp.data);
        if (mFinalType == 1) { // 滚球
            mViewModel.leagueLiveListData.postValue(mLeagueList);
        } else {
            mViewModel.leagueNoLiveListData.postValue(mLeagueList);
        }
        if (mCurrentPage == 1) {
            SPUtils.getInstance().put(BT_LEAGUE_LIST_CACHE + mPlayMethodType + mSearchDatePos + mSportId, new Gson().toJson(mLeagueList));
        }
        mViewModel.saveLeague(this);
        mHasCache = false;
        mIsStepSecond = false;
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof ResponseThrowable) {
            ResponseThrowable error = (ResponseThrowable) t;
            if (error.code == CODE_401026 || error.code == CODE_401013) {
                mViewModel.getGameTokenApi();
            } else if (error.code == CODE_401038) {
                ToastUtils.showShort(error.getMessage());
                mViewModel.tooManyRequestsEvent.call();
            } else {
                mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, mPlayMethodType, mSearchDatePos, mOddType, mIsTimerRefresh, mIsRefresh);
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

}
