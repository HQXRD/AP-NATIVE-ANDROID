package com.xtree.bet.ui.viewmodel.fb;


import static com.xtree.base.net.FBHttpCallBack.CodeRule.CODE_14010;
import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.FBHttpCallBack;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.vo.FBService;
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
import com.xtree.base.utils.BtDomainUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.BaseResponse;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis
 */

public class FbBtDetailViewModel extends TemplateBtDetailViewModel {
    private long mMatchId;

    public FbBtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void getMatchDetail(long matchId) {
        mMatchId = matchId;
        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        map.put("matchId", String.valueOf(matchId));

        Disposable disposable = (Disposable) model.getApiService().getMatchDetail(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new FBHttpCallBack<MatchInfo>() {
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
                        if (((ResponseThrowable) t).code == CODE_14010) {
                            getGameTokenApi();
                        }
                    }
                });
        addSubscribe(disposable);

    }

    public List<Category> getCategoryList(MatchInfo matchInfo) {
        Map<String, Category> categoryMap = new HashMap<>();
        List<Category> categoryList = new ArrayList<>();
        if (matchInfo.mg.isEmpty()) {
            return categoryList;
        }

        CategoryFb categoryAll = new CategoryFb(FBMarketTag.getMarketTag("all"));
        categoryMap.put("all", categoryAll);
        categoryList.add(categoryAll);
        for (PlayTypeInfo playTypeInfo : matchInfo.mg) {
            PlayTypeFb playType = new PlayTypeFb(playTypeInfo);
            categoryAll.addPlayTypeList(playType);
            for (String type : playTypeInfo.tps) {
                if (categoryMap.get(type) == null) {
                    Category category = new CategoryFb(FBMarketTag.getMarketTag(type));
                    categoryMap.put(type, category);
                    categoryList.add(category);
                }
                categoryMap.get(type).addPlayTypeList(playType);
            }
        }
        if(mCategoryMap.isEmpty()) {
            mCategoryMap = categoryMap;
            mCategoryList = categoryList;
        }else{
            if(categoryMap.size() <= mCategoryMap.size()) {
                for (String key : mCategoryMap.keySet()) {
                    Category oldCategory = mCategoryMap.get(key);
                    int index = mCategoryList.indexOf(oldCategory);
                    Category newCategory = categoryMap.get(key);
                    if(index > -1) {
                        mCategoryList.set(index, newCategory);
                        mCategoryMap.put(key, newCategory);
                    }
                }
            }
            /*if(!mCategoryList.isEmpty()) {
                mCategoryList.set(mCategoryList.size() - 1, null);
            }*/
        }
        return mCategoryList;
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
                        && oldOption.getRealOdd() != newOption.getRealOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getRealOdd());
                    break;
                }
            }
        }
    }

    private List<Option> getMatchOptionList(Match match) {
        if (match == null) {
            return new ArrayList<>();
        }
        List<Option> optionArrayList = new ArrayList<>();
        for (PlayType playType : match.getPlayTypeList()) {
            if (playType.getOptionLists() != null) {
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

    public void getGameTokenApi() {
        Flowable<BaseResponse<FBService>> flowable;
        String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
            flowable = model.getBaseApiService().getFBXCGameTokenApi();
        } else {
            flowable = model.getBaseApiService().getFBGameTokenApi();
        }
        Disposable disposable = (Disposable) flowable
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<FBService>() {
                    @Override
                    public void onResult(FBService fbService) {
                        if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FBXC_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setDefaultFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.addFbxcDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setFbxcDomainUrl(fbService.getDomains());
                        } else {
                            SPUtils.getInstance().put(SPKeyGlobal.FB_TOKEN, fbService.getToken());
                            SPUtils.getInstance().put(SPKeyGlobal.FB_API_SERVICE_URL, fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setDefaultFbDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.addFbDomainUrl(fbService.getForward().getApiServerAddress());
                            BtDomainUtil.setFbDomainUrl(fbService.getDomains());
                        }

                        getMatchDetail(mMatchId);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
