package com.xtree.bet.ui.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.fb.MatchInfo;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.data.BetRepository;

import java.util.HashMap;
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
                            setOptionOddChange(match, null);
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

}
