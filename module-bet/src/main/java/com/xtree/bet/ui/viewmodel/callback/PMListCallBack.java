package com.xtree.bet.ui.viewmodel.callback;

import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401026;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401038;

import android.text.TextUtils;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.vo.BaseBean;
import com.xtree.bet.R;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
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

import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.Utils;

public class PMListCallBack extends PMHttpCallBack<List<MatchInfo>> {

    private PMMainViewModel mViewModel;
    private boolean mHasCache;
    private boolean mIsTimerRefresh;
    private boolean mIsRefresh;
    private List<Long> mMatchids;
    private int mSportPos;
    private String mSportId;
    private int mOrderBy;
    private List<Long> mLeagueIds;
    private int mPlayMethodType;
    private int mSearchDatePos;
    private int mOddType;
    private boolean mIsStepSecond;
    private boolean mNoLiveMatch;
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

    /**
     * 正在进行中的比赛
     */
    private List<BaseBean> mLiveMatchList = new ArrayList<>();
    /**
     * 未开始的比赛
     */
    private List<BaseBean> mNoliveMatchList = new ArrayList<>();

    public void saveLeague() {
        mLiveMatchList = mViewModel.getLiveMatchList();
        if (!mIsRefresh) {
            mLeagueList = mViewModel.getLeagueList();
            mGoingOnLeagueList = mViewModel.getGoingOnLeagueList();
            mMapLeague = mViewModel.getMapLeague();
            mMatchList = mViewModel.getMatchList();
            mMapMatch = mViewModel.getMapMatch();
            mMapSportType = mViewModel.getMapSportType();
            mNoliveMatchList = mViewModel.getNoliveMatchList();
        }
    }

    public PMListCallBack(PMMainViewModel viewModel, boolean hasCache, boolean isTimerRefresh, boolean isRefresh,
                          int playMethodType, int sportPos, String sportId, int orderBy, List<Long> leagueIds,
                          int searchDatePos, int oddType, List<Long> matchids/*, boolean isStepSecond*/) {
        mViewModel = viewModel;
        mHasCache = hasCache;
        mIsTimerRefresh = isTimerRefresh;
        mIsRefresh = isRefresh;
        mPlayMethodType = playMethodType;
        mSportPos = sportPos;
        mSportId = sportId;
        mOrderBy = orderBy;
        mLeagueIds = leagueIds;
        mSearchDatePos = searchDatePos;
        mOddType = oddType;
        mMatchids = matchids;
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
    public void onResult(List<MatchInfo> data) {
        if (mIsTimerRefresh) { // 定时刷新赔率变更
            if (data.size() != mMatchids.size()) {
                //List<Long> matchIdList = new ArrayList<>();
                mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, null, mPlayMethodType, mSearchDatePos, mOddType, false, true);
            } else {
                setOptionOddChange(data);
                mViewModel.leagueLiveTimerListData.postValue(mLeagueList);
            }
        } else {  // 获取今日中的全部滚球赛事列表
            if (mIsRefresh) {
                mLeagueList.clear();
                mMapLeague.clear();
                mMapSportType.clear();
                mLiveMatchList.clear();
            }
            mViewModel.firstNetworkFinishData.call();
            mIsStepSecond = true;
            mLiveMatchList.addAll(data);
            if(TextUtils.isEmpty(mViewModel.mSearchWord)) {
                leagueGoingList(data);
            }
            mViewModel.saveLeague(this);
            mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, 2, mSearchDatePos, mOddType, false, mIsRefresh, mIsStepSecond);
        }
    }

    @Override
    public void onError(Throwable t) {
        mViewModel.getUC().getDismissDialogEvent().call();
        //if (!mIsTimerRefresh) {
            if (t instanceof ResponseThrowable) {
                ResponseThrowable error = (ResponseThrowable) t;
                if (error.code == CODE_401026 || error.code == CODE_401013) {
                    mViewModel.getGameTokenApi();
                } else if (error.code == CODE_401038) {
                    super.onError(t);
                    mViewModel.tooManyRequestsEvent.call();
                } else {
                    //mViewModel.getLeagueList(mSportPos, mSportId, mOrderBy, mLeagueIds, mMatchids, mPlayMethodType, mSearchDatePos, mOddType, mIsTimerRefresh, mIsRefresh);
                }
            }
        //}
    }

    /**
     * 设置赔率变化
     *
     * @param matchInfoList
     */
    private void setOptionOddChange(List<MatchInfo> matchInfoList) {
        List<Match> newMatchList = new ArrayList<>();

        for (MatchInfo matchInfo : matchInfoList) {
            Match newMatch = new MatchPm(matchInfo);
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

    private void leagueGoingList(List<MatchInfo> matchInfoList) {
        if (matchInfoList.isEmpty()) {
            mViewModel.mNoLiveMatch = true;
            return;
        }

        League liveHeaderLeague = new LeaguePm();
        buildLiveHeaderLeague(liveHeaderLeague);

        Map<String, League> mapLeague = new HashMap<>();
        Map<String, League> mapSportType = new HashMap<>();
        for (MatchInfo matchInfo : matchInfoList) {
            Match match = new MatchPm(matchInfo);

            buildLiveSportHeader(mapSportType, match, new LeaguePm());

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

    private List<Option> getMatchOptionList(List<Match> matchList) {
        List<Option> optionList = new ArrayList<>();
        for (Match match : matchList) {
            PlayGroup newPlayGroup = new PlayGroupPm(match.getPlayTypeList());

            for (PlayType playType : newPlayGroup.getPlayTypeList()) {
                playType.getOptionLists();
                for (Option option : playType.getOptionList(match.getSportId())) {
                    if (option == null) {
                        continue;
                    }
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
}
