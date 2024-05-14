package com.xtree.bet.ui.viewmodel.pm;

import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401013;
import static com.xtree.base.net.PMHttpCallBack.CodeRule.CODE_401026;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.vo.PMService;
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
import com.xtree.base.utils.BtDomainUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.http.ResponseThrowable;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by marquis
 */

public class PmBtDetailViewModel extends TemplateBtDetailViewModel {
    private List<Category> mTmpCategoryList = new ArrayList<>();
    private Map<String, Category> mTmpCategoryMap = new HashMap<>();
    private boolean isFirst = true;
    private List<PlayType> mPlayTypeList;
    private MatchInfo mMatchInfo;
    private long mMatchId;
    private String mSportId;

    public PmBtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void getMatchDetail(long matchId) {
        mMatchId = matchId;
        Map<String, String> map = new HashMap<>();
        map.put("mid", String.valueOf(matchId));

        Disposable disposable = (Disposable) model.getPMApiService().getMatchDetail(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<MatchInfo>() {
                    @Override
                    public void onResult(MatchInfo matchInfo) {
                        if(matchInfo == null){
                            return;
                        }
                        mMatchInfo = matchInfo;
                        if(mMatchInfo.mid != null) {
                            videoAnimationUrlPB(Long.valueOf(mMatchInfo.mid), "Video");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        ResponseThrowable error = (ResponseThrowable) t;
                        if (error.code == CODE_401026 || error.code == CODE_401013) {
                            getGameTokenApi();
                        }
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
                        if(mMatchInfo != null && mMatchInfo.mid != null) {
                            if (TextUtils.equals(type, "Video")) {
                                if(videoAnimationInfo.videoUrlVOList != null) {
                                    mMatchInfo.vs = videoAnimationInfo.videoUrlVOList;
                                }
                                videoAnimationUrlPB(Long.valueOf(mMatchInfo.mid), "Animation");
                            }
                            if (TextUtils.equals(type, "Animation")) {
                                if(!TextUtils.isEmpty(videoAnimationInfo.animationUrl)) {
                                    mMatchInfo.as.clear();
                                    mMatchInfo.as.add(videoAnimationInfo.animationUrl);
                                }
                                Match match = new MatchPm(mMatchInfo);
                                mMatch = match;
                                mMatch.setReferUrl(videoAnimationInfo.referUrl);
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
        mSportId = sportId;
        Map<String, String> map = new HashMap<>();
        map.put("mid", matchId);
        map.put("sportId", sportId);

        Disposable disposable = (Disposable) model.getPMApiService().getCategoryList(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<CategoryPm>>() {
                    @Override
                    public void onResult(List<CategoryPm> categoryPms) {
                        Map<String, Category> categoryMap = new HashMap<>();
                        List<Category> categoryList = new ArrayList<>();
                        for (CategoryPm category : categoryPms) {
                            categoryList.add(category);
                            categoryMap.put(category.getId(), category);
                        }
                        if(mCategoryMap.isEmpty()) {
                            mCategoryMap = categoryMap;
                            mCategoryList = categoryList;
                        } else {
                            mTmpCategoryMap = categoryMap;
                            mTmpCategoryList = categoryList;
                        }
                        getMatchOddsInfoPB(matchId, "0");
                    }

                    @Override
                    public void onError(Throwable t) {

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

                        if(playTypeList == null || playTypeList.isEmpty()){
                            mCategoryList.clear();
                            mCategoryMap.clear();
                            isFirst = false;
                            categoryListData.postValue(mCategoryList);
                            return;
                        }

                        Collections.sort(playTypeList);

                        List<PlayType> playTypes = new ArrayList<>();
                        for (PlayTypeInfo playTypeInfo : playTypeList) {
                            playTypes.add(new PlayTypePm(playTypeInfo));
                        }

                        if (isFirst) {
                            for(Category category : mCategoryList){
                                for (PlayType playType : playTypes) {
                                    CategoryPm categoryPm = (CategoryPm) category;
                                    if(categoryPm.getPlays().contains(Integer.valueOf(playType.getId()))){
                                        category.addPlayTypeList(playType);
                                    }
                                }
                            }
                        }else {
                            // 设置赔率变化
                            setOptionOddChange(mid, playTypes);
                            for(Category category : mTmpCategoryList){
                                for (PlayType playType : playTypes) {
                                    CategoryPm categoryPm = (CategoryPm) category;
                                    if(categoryPm.getPlays().contains(Integer.valueOf(playType.getId()))){
                                        category.addPlayTypeList(playType);
                                    }
                                }
                            }
                            if(mTmpCategoryList.size() <= mCategoryMap.size()) {
                                for (String key : mCategoryMap.keySet()) {
                                    Category oldCategory = mCategoryMap.get(key);
                                    int index = mCategoryList.indexOf(oldCategory);
                                    Category newCategory = mTmpCategoryMap.get(key);
                                    if(index > -1) {
                                        mCategoryList.set(index, newCategory);
                                        mCategoryMap.put(key, newCategory);
                                    }
                                }
                            }
                            /*if (!mCategoryList.isEmpty()) {
                                mCategoryList.set(mCategoryList.size() - 1, null);
                            }*/
                        }
                        mPlayTypeList = playTypes;
                        isFirst = false;
                        categoryListData.postValue(mCategoryList);
                    }

                    @Override
                    public void onError(Throwable t) {

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
                        && oldOption.getRealOdd() != newOption.getRealOdd()
                        && TextUtils.equals(oldOption.getCode(), newOption.getCode())) {
                    newOption.setChange(oldOption.getRealOdd());
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

    public void getGameTokenApi() {
        Disposable disposable = (Disposable) model.getBaseApiService().getPMGameTokenApi()
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<PMService>() {
                    @Override
                    public void onResult(PMService pmService) {
                        SPUtils.getInstance().put(SPKeyGlobal.PM_TOKEN, pmService.getToken());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_API_SERVICE_URL, pmService.getApiDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_IMG_SERVICE_URL, pmService.getImgDomain());
                        SPUtils.getInstance().put(SPKeyGlobal.PM_USER_ID, pmService.getUserId());
                        BtDomainUtil.setDefaultPmDomainUrl(pmService.getApiDomain());
                        getMatchDetail(mMatchId);
                        if(TextUtils.isEmpty(mSportId)) {
                            getCategoryList(String.valueOf(mMatchId), mSportId);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        //super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
