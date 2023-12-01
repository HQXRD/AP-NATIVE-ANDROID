package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFbAdapter;
import com.xtree.bet.bean.LeagueItem;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFbAdapter;
import com.xtree.bet.bean.MatchInfo;
import com.xtree.bet.bean.MatchItem;
import com.xtree.bet.bean.MatchListRsp;
import com.xtree.bet.contract.ExpandContract;
import com.xtree.bet.data.BetRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.RxSubscriptions;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * Created by goldze on 2018/6/21.
 */

public class MainViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();

    public SingleLiveData<String[]> playMethodTab = new SingleLiveData<>();
    public SingleLiveData<List<String>> playSearchDate = new SingleLiveData<>();
    public SingleLiveData<List<MatchItem>> matchItemDate = new SingleLiveData<>();
    public SingleLiveData<LeagueItem> leagueItemDate = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueWaitingListDate = new SingleLiveData<>();
    public SingleLiveData<List<League>> leagueGoingOnListDate = new SingleLiveData<>();
    public SingleLiveData<ExpandContract> expandContractListDate = new SingleLiveData<>();

    public MainViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void setPlayMethodTabData(){
        playMethodTab.setValue(new String[]{"今日", "滚球", "早盘", "串关", "冠军"});
    }

    public void setplaySearchDateData(){
        playSearchDate.setValue(TimeUtils.getNextDays(8, TimeUtils.FORMAT_MM_DD));
    }

    public void setMatchItems(){
        List<MatchItem> matchItemList = new ArrayList<>();
        String[] sportNames = new String[]{"足球", "篮球", "网球", "斯诺克", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "海滩排球"};
        int[] sportGameCount = new int[]{12, 13, 25, 36, 54, 45, 12, 89, 121, 18};
        for (int i = 0; i < sportNames.length; i ++){
            MatchItem item = new MatchItem();
            item.setName(sportNames[i]);
            item.setMatchCount(sportGameCount[i]);
            matchItemList.add(item);
        }

        matchItemDate.postValue(matchItemList);
    }

    public void setFbLeagueData(){
        leagueItemDate.setValue(new LeagueItem());
    }

    public void setLeagueList(int playMethod, int sportType){
        if(playMethod == 0){
            setGoingLeagueList(playMethod, sportType);
        }else{
            setWaitingLeagueList(sportType);
        }
    }

    public void setWaitingLeagueList(int sportType){
        InputStream inputStream = Utils.getContext().getResources().openRawResource(R.raw.test);
        if(sportType == 0) {
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test);
        }else if(sportType == 1){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_bb);
        }else if(sportType == 2){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_wq);
        }else if(sportType == 3){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_snk);
        }else if(sportType == 4){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_pq);
        }else if(sportType == 5){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_ymq);
        }else if(sportType == 6){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_mszq);
        }else if(sportType == 7){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_bbq);
        }else if(sportType == 8){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_bq);
        }else if(sportType == 9){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_stpq);
        }
        String json = null;
        try {
            json = readTextFile(inputStream);
        } catch (IOException e) {
            String s = e.getMessage();
            String sq = s;
        }
        MatchListRsp matchListRsp = new Gson().fromJson(json, MatchListRsp.class);
        leagueWaitingListDate.postValue(leagueAdapterList(matchListRsp.records, true));

    }

    public void setGoingLeagueList(int playMethod, int sportType){
        InputStream inputStream = Utils.getContext().getResources().openRawResource(R.raw.test);
        if(sportType == 0) {
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test);
        }else if(sportType == 1){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_bb);
        }else if(sportType == 2){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_wq);
        }else if(sportType == 3){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_snk);
        }else if(sportType == 4){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_pq);
        }else if(sportType == 5){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_ymq);
        }else if(sportType == 6){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_mszq);
        }else if(sportType == 7){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_bbq);
        }else if(sportType == 8){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_bq);
        }else if(sportType == 9){
            inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_stpq);
        }
        String json = null;
        try {
            json = readTextFile(inputStream);
        } catch (IOException e) {
            String s = e.getMessage();
            String sq = s;
        }
        MatchListRsp matchListRsp = new Gson().fromJson(json, MatchListRsp.class);
        leagueGoingOnListDate.postValue(leagueAdapterList(matchListRsp.records, true));
        if(playMethod == 0){
            setWaitingLeagueList(sportType);
        }
    }

    /**
     *
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
            if(leagueAdapter == null){
                leagueAdapter = new LeagueFbAdapter(matchInfo.lg);
                leagueAdapter.setSort(index ++);
                map.put(String.valueOf(matchInfo.lg.id), leagueAdapter);
            }

            Match matchFbAdapter = new MatchFbAdapter(matchInfo);
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

    public void addSubscription(){
        mSubscription = RxBus.getDefault().toObservable(ExpandContract.class)
                .subscribe(expandContract -> {
                    expandContractListDate.postValue(new ExpandContract());
                });
        RxSubscriptions.add(mSubscription);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxSubscriptions.remove(mSubscription);
    }
}
