package com.xtree.bet.ui.viewmodel.pm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.PMHttpCallBack;
import com.xtree.bet.bean.response.pm.LeagueAreaInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.response.pm.PlayTypeInfo;
import com.xtree.bet.bean.response.pm.VideoAnimationInfo;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.CategoryPm;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchPm;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.bean.ui.PlayTypePm;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class PmBtDetailViewModel extends TemplateBtDetailViewModel {

    private List<Category> categoryList = new ArrayList<>();
    private Map<String, Category> categoryMap = new HashMap<>();
    private boolean isFirst = true;
    private List<PlayType> mPlayTypeList;
    private MatchInfo mMatchInfo;

    public PmBtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void getMatchDetail(long matchId) {

        Map<String, String> map = new HashMap<>();
        map.put("mid", String.valueOf(matchId));

        Disposable disposable = (Disposable) model.getPMApiService().getMatchDetail(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<MatchInfo>() {
                    @Override
                    public void onResult(MatchInfo matchInfo) {
                        mMatchInfo = matchInfo;
                        videoAnimationUrlPB(Long.valueOf(mMatchInfo.mid), "Video");
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);

    }

    @Override
    public void videoAnimationUrlPB(long matchId, String type) {

        Map<String, String> map = new HashMap<>();

        map.put("device", "H5");
        map.put("mid", String.valueOf(matchId));
        map.put("type", type);

        Disposable disposable = (Disposable) model.getPMApiService().videoAnimationUrlPB(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<VideoAnimationInfo>() {
                    @Override
                    public void onResult(VideoAnimationInfo videoAnimationInfo) {
                        if(mMatchInfo != null) {
                            if (TextUtils.equals(type, "Video")) {
                                if(videoAnimationInfo.videoUrlVOList != null) {
                                    mMatchInfo.vs = videoAnimationInfo.videoUrlVOList;
                                }
                                videoAnimationUrlPB(mMatch.getId(), "Animation");
                            }
                            if (TextUtils.equals(type, "Animation")) {
                                if(!TextUtils.isEmpty(videoAnimationInfo.animationUrl)) {
                                    mMatchInfo.as.clear();
                                    mMatchInfo.as.add(videoAnimationInfo.animationUrl);
                                }
                                Match match = new MatchPm(mMatchInfo);
                                mMatch = match;
                                matchData.postValue(match);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if(mMatchInfo != null) {
                            Match match = new MatchPm(mMatchInfo);
                            mMatch = match;
                            matchData.postValue(match);
                        }
                    }
                });
        addSubscribe(disposable);

    }

    @Override
    public void getCategoryList(String matchId, String sportId) {

        Map<String, String> map = new HashMap<>();
        map.put("mid", matchId);
        map.put("sportId", sportId);

        Disposable disposable = (Disposable) model.getPMApiService().getCategoryList(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<CategoryPm>>() {
                    @Override
                    public void onResult(List<CategoryPm> categoryPms) {
                        categoryList.clear();
                        for (CategoryPm category : categoryPms) {
                            categoryList.add(category);
                            categoryMap.put(category.getId(), category);
                        }
                        getMatchOddsInfoPB(matchId, "0");
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void getMatchOddsInfoPB(String mid, String mcid) {
        Map<String, String> map = new HashMap<>();
        map.put("mid", mid);
        map.put("mcid", mcid);
        map.put("cuid", SPUtils.getInstance().getString(SPKeyGlobal.PM_USER_ID));

        Disposable disposable = (Disposable) model.getPMApiService().getMatchOddsInfoPB(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<PlayTypeInfo>>() {
                    @Override
                    public void onResult(List<PlayTypeInfo> playTypeList) {
                        Collections.sort(playTypeList);

                        List<PlayType> playTypes = new ArrayList<>();
                        for (PlayTypeInfo playTypeInfo : playTypeList) {
                            playTypes.add(new PlayTypePm(playTypeInfo));
                        }

                        if (isFirst) {
                            mPlayTypeList = playTypes;
                            for(Category category : categoryList){
                                for (PlayType playType : mPlayTypeList) {
                                    CategoryPm categoryPm = (CategoryPm) category;
                                    if(categoryPm.getPlays().contains(Integer.valueOf(playType.getId()))){
                                        category.addPlayTypeList(playType);
                                    }
                                }
                            }
                        }else { // 设置赔率变化
                            setOptionOddChange(mid, playTypes);
                            CategoryPm categoryPm = (CategoryPm) categoryMap.get(mcid);
                            for (PlayType newPlayType : playTypes) {
                                for (PlayType oldPlayType : categoryPm.getPlayTypeList()) {
                                    if(TextUtils.equals(oldPlayType.getId(), newPlayType.getId()) && TextUtils.equals(oldPlayType.getPlayTypeName(), newPlayType.getPlayTypeName())){

                                        int index = categoryPm.getPlayTypeList().indexOf(oldPlayType);
                                        if(index > -1){
                                            categoryPm.getPlayTypeList().set(index, newPlayType);
                                        }

                                        break;
                                    }
                                }
                            }
                            mPlayTypeList = categoryPm.getPlayTypeList();
                        }
                        isFirst = false;
                        categoryListData.postValue(categoryList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

    /**
     * 设置赔率变化
     */
    void setOptionOddChange(String matchId, List<PlayType> newPlayTypeList) {
        List<Option> newOptonList = getMatchOptionList(matchId, newPlayTypeList);
        List<Option> oldOptonList = getMatchOptionList(matchId, mPlayTypeList);

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

    private List<Option> getMatchOptionList(String matchId, List<PlayType> playTypeList) {
        List<Option> optionList = new ArrayList<>();
        for (PlayType playType : playTypeList) {
            for (OptionList options : playType.getOptionLists()) {
                for (Option option : options.getOptionList()) {
                    if (option != null) {
                        StringBuffer code = new StringBuffer();
                        code.append(matchId);
                        if (option != null) {
                            code.append(option.getId());
                        }
                        option.setCode(code.toString());
                    }
                    optionList.add(option);
                }
            }
        }
        return optionList;
    }

}
