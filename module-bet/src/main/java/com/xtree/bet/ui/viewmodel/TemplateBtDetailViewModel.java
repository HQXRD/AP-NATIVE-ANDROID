package com.xtree.bet.ui.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;

import java.util.List;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;

/**
 * Created by goldze on 2018/6/21.
 */

public abstract class TemplateBtDetailViewModel extends BaseViewModel<BetRepository> implements BtDetailViewModel {
    private Disposable mSubscription;
    public SingleLiveData<List<Category>> categoryListData = new SingleLiveData<>();
    public SingleLiveData<Match> matchData = new SingleLiveData<>();
    public SingleLiveData<BetContract> betContractListData = new SingleLiveData<>();
    public Match mMatch;

    public TemplateBtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void addSubscription() {
        mSubscription = RxBus.getDefault().toObservable(BetContract.class)
                .subscribe(betContract -> {
                    betContractListData.postValue(betContract);
                });
        addSubscribe(mSubscription);
    }

    public void videoAnimationUrlPB(long matchId, String type) {

    }

    /**
     * 获取赛事玩法分类，仅PM用
     * @param matchId
     * @param sportId
     */
    public void getCategoryList(String matchId, String sportId){

    }

    /**
     * 获取赛事玩法集合，仅PM用
     * @param mid
     * @param mcid
     */
    public void getMatchOddsInfoPB(String mid, String mcid){

    }
}
