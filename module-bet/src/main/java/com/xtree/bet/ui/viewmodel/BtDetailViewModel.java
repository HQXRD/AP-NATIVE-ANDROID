package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.MatchInfo;
import com.xtree.bet.bean.response.PlayTypeInfo;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.CategoryFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.PlayTypeFb;
import com.xtree.bet.constant.MarketTag;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class BtDetailViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;
    public SingleLiveData<List<Category>> categoryListData = new SingleLiveData<>();
    public SingleLiveData<Match> matchData = new SingleLiveData<>();
    public SingleLiveData<BetContract> betContractListData = new SingleLiveData<>();

    public BtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void addSubscription() {
        mSubscription = RxBus.getDefault().toObservable(BetContract.class)
                .subscribe(betContract -> {
                    betContractListData.postValue(betContract);
                });
        addSubscribe(mSubscription);
    }

    public void getMatchDetail(int matchId){

        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        map.put("matchId", String.valueOf(matchId));

        Disposable disposable = (Disposable) model.getApiService().getMatchDetail(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MatchInfo>() {
                    @Override
                    public void onResult(MatchInfo matchInfo) {
                        matchData.postValue(new MatchFb(matchInfo));
                        categoryListData.postValue(getCategoryList(matchInfo, true));
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);

    }

    public static List<Category> getCategoryList(MatchInfo matchInfo, boolean isFb) {
        Map<String, Category> map = new HashMap<>();
        List<Category> categoryList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo : matchInfo.mg) {
            for(String type : playTypeInfo.tps){
                if(map.get(type) == null){
                    map.put(type, new CategoryFb(playTypeInfo, MarketTag.getMarketTag(type)));
                }
                map.get(type).addPlayTypeList(new PlayTypeFb(playTypeInfo));
            }
        }
        categoryList.addAll(map.values());
        return categoryList;
    }
}
