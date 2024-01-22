package com.xtree.bet.ui.viewmodel;

import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FB;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.response.HotLeagueInfo;
import com.xtree.bet.bean.response.fb.LeagueItem;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public abstract class TemplateMainViewModel extends BaseBtViewModel implements MainViewModel{
    public static String[] PLAY_METHOD_NAMES = new String[]{"今日", "滚球", "早盘", "串关", "冠军"};
    public static String[] SPORT_NAMES = new String[]{"热门", "足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
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
    public SingleLiveData<Integer> goingOnMatchCountData = new SingleLiveData<>();

    /**
     * 赛事统计数据
     */
    public SingleLiveData<Map<String, List<Integer>>> statisticalData = new SingleLiveData<>();
    public SingleLiveData<List<League>> settingLeagueData = new SingleLiveData<>();

    public TemplateMainViewModel(@NonNull Application application, BetRepository model) {
        super(application, model);
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

    public void setSportItems() {
        sportItemData.postValue(SPORT_NAMES);
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
    public void getHotLeague(String platform){
        Map<String, String> map = new HashMap<>();
        map.put("fields", TextUtils.equals(platform, PLATFORM_FB) ? "fbxc_popular_leagues" : "obg_popular_leagues");

        Disposable disposable = (Disposable) model.getBaseApiService().getSettings(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<HotLeagueInfo>() {
                    @Override
                    public void onResult(HotLeagueInfo hotLeagueInfo) {
                        List<String> hotLeagues = TextUtils.equals(platform, PLATFORM_FB) ? hotLeagueInfo.fbxc_popular_leagues : hotLeagueInfo.obg_popular_leagues;
                        for (String leagueId : hotLeagues) {
                            hotLeagueList.add(Long.valueOf(leagueId));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /*if(!scrollFlag || mHeaderPosition == 0){
        return;
    }

    long position = binding.rvLeague.getExpandableListPosition(firstVisibleItem);
    int groupPosition = binding.rvLeague.getPackedPositionGroup(position);
                if(groupPosition < 0){
        return;
    }
                Log.e("test", "======groupPosition======" + groupPosition);

    League league = mLeagueList.get(groupPosition);
                if (!league.isHead()) {
        setHeader(league);
    }
                if(groupPosition > mHeaderPosition) { // 上滑
        binding.tvGoingOn.setText(getResources().getString(R.string.bt_game_waiting));
        binding.tvAllLeague.setText(getResources().getString(R.string.bt_game_waiting));
        binding.tvSportName.setText(TemplateMainViewModel.SPORT_NAMES[sportTypePos] + "(" + mLeagueList.get(mHeaderPosition).getMatchCount() + ")");
    }else if(groupPosition < mHeaderPosition){
        binding.tvGoingOn.setText(getResources().getString(R.string.bt_game_going_on));
        binding.tvAllLeague.setText(getResources().getString(R.string.bt_all_league));
        binding.tvSportName.setText(TemplateMainViewModel.SPORT_NAMES[sportTypePos] + "(" + mGoingOnMatchCount + ")");
    }*/

}
