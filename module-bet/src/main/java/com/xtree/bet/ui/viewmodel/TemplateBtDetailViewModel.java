package com.xtree.bet.ui.viewmodel;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.response.fb.PlayTypeInfo;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.CategoryFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayGroupPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.bean.ui.PlayTypeFb;
import com.xtree.bet.constant.MarketTag;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public abstract class TemplateBtDetailViewModel extends BaseViewModel<BetRepository> implements BtDetailViewModel {
    private Disposable mSubscription;
    public SingleLiveData<List<Category>> categoryListData = new SingleLiveData<>();
    public SingleLiveData<Match> matchData = new SingleLiveData<>();
    public SingleLiveData<BetContract> betContractListData = new SingleLiveData<>();
    Match mMatch;
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

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

    List<Category> getCategoryList(MatchInfo matchInfo) {
        Map<String, Category> map = new HashMap<>();
        List<Category> categoryList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo : matchInfo.mg) {
            for (String type : playTypeInfo.tps) {
                if (map.get(type) == null) {
                    map.put(type, new CategoryFb(playTypeInfo, MarketTag.getMarketTag(type)));
                }
                map.get(type).addPlayTypeList(new PlayTypeFb(playTypeInfo));
            }
        }
        categoryList.addAll(map.values());
        return categoryList;
    }

    /**
     * 设置赔率变化
     *
     * @param mewMatch
     */
    void setOptionOddChange(Match mewMatch, List<PlayType> newPlayTypeList) {
        List<Option> newOptonList = getMatchOptionList(mewMatch, newPlayTypeList);
        List<Option> oldOptonList = getMatchOptionList(mMatch, newPlayTypeList);

        for (Option newOption : newOptonList) {
            for (Option oldOption : oldOptonList) {
                if (oldOption != null && newOption != null
                        && oldOption.getOdd() != newOption.getOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getOdd());
                    break;
                }
            }
        }
    }

    private List<Option> getMatchOptionList(Match match, List<PlayType> playTypeList) {
        if(match == null){
            return new ArrayList<>();
        }
        if(playTypeList != null){
            return getOptionList(match, playTypeList);
        }else {
            return getOptionList(match, match.getPlayTypeList());
        }
    }

    private List<Option> getOptionList(Match match, List<PlayType> playTypeList) {
        List<Option> optionList = new ArrayList<>();
        for (PlayType playType : playTypeList) {
            playType.getOptionLists();
            for (Option option : playType.getOptionList()) {
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
        return optionList;
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
