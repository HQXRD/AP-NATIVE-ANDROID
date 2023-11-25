package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.bean.LeagueItem;
import com.xtree.bet.bean.MatchItem;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.http.ApiCallBack;
import me.xtree.mvvmhabit.http.ApiSubscriber;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class MainViewModel extends BaseViewModel<BetRepository> {
    public SingleLiveData<String> itemClickEvent = new SingleLiveData<>();

    public SingleLiveData<String[]> playMethodTab = new SingleLiveData<>();
    public SingleLiveData<List<String>> playSearchDate = new SingleLiveData<>();
    public SingleLiveData<List<MatchItem>> matchItemDate = new SingleLiveData<>();
    public SingleLiveData<LeagueItem> leagueItemDate = new SingleLiveData<>();

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
        MatchItem item = new MatchItem();
        item.setName("足球");
        item.setMatchCount(15);
        matchItemList.add(item);

        MatchItem itemb = new MatchItem();
        itemb.setName("篮球");
        itemb.setMatchCount(150);
        matchItemList.add(itemb);

        matchItemDate.postValue(matchItemList);
    }

    public void setFbLeagueData(){
        leagueItemDate.setValue(new LeagueItem());
    }

}
