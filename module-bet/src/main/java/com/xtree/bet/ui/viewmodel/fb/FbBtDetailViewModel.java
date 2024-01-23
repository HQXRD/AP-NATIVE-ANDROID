package com.xtree.bet.ui.viewmodel.fb;


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
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.bean.ui.PlayTypeFb;
import com.xtree.bet.constant.FBMarketTag;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class FbBtDetailViewModel extends TemplateBtDetailViewModel {

    public FbBtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void getMatchDetail(long matchId) {

        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        map.put("matchId", String.valueOf(matchId));

        Disposable disposable = (Disposable) model.getApiService().getMatchDetail(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<MatchInfo>() {
                    @Override
                    public void onResult(MatchInfo matchInfo) {
                        Match match = new MatchFb(matchInfo);
                        if (mMatch != null) {
                            setOptionOddChange(match);
                        }
                        mMatch = match;
                        matchData.postValue(match);
                        categoryListData.postValue(getCategoryList(matchInfo));
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);

    }

    public List<Category> getCategoryList(MatchInfo matchInfo) {
        Map<String, Category> map = new HashMap<>();
        List<Category> categoryList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo : matchInfo.mg) {
            for (String type : playTypeInfo.tps) {
                if (map.get(type) == null) {
                    map.put(type, new CategoryFb(playTypeInfo, FBMarketTag.getMarketTag(type)));
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
    void setOptionOddChange(Match mewMatch) {
        List<Option> newOptonList = getMatchOptionList(mewMatch);
        List<Option> oldOptonList = getMatchOptionList(mMatch);

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

    private List<Option> getMatchOptionList(Match match) {
        if(match == null){
            return new ArrayList<>();
        }
        List<Option> optionArrayList = new ArrayList<>();
        for (PlayType playType : match.getPlayTypeList()) {
            if(playType.getOptionLists() != null) {
                for (OptionList optionList : playType.getOptionLists()) {
                    for (Option option : optionList.getOptionList()) {
                        if (option != null) {
                            StringBuffer code = new StringBuffer();
                            code.append(match.getId());
                            code.append(playType.getPlayType());
                            code.append(playType.getPlayPeriod());
                            code.append(optionList.getId());
                            code.append(option.getOptionType());
                            code.append(option.getId());
                            if (!TextUtils.isEmpty(option.getLine())) {
                                code.append(option.getLine());
                            }
                            option.setCode(code.toString());
                        }
                        optionArrayList.add(option);
                    }
                }
            }
        }
        return optionArrayList;
    }

}
