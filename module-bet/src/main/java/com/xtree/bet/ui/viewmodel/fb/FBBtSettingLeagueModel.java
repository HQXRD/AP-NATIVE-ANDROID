package com.xtree.bet.ui.viewmodel.fb;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.base.utils.StringUtils;
import com.xtree.bet.bean.response.fb.LeagueInfo;
import com.xtree.bet.bean.ui.InitialLeagueArea;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueArea;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtSettingLeagueModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class FBBtSettingLeagueModel extends TemplateBtSettingLeagueModel {

    public FBBtSettingLeagueModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    /**
     * 获取联赛列表
     */
    public void getOnSaleLeagues(int sportId, int type, List<Long> leagueIdList) {

        Map<String, String> map = new HashMap<>();
        map.put("languageType", "CMN");
        map.put("sportId", String.valueOf(sportId));
        map.put("type", String.valueOf(type));

        Disposable disposable = (Disposable) model.getApiService().getOnSaleLeagues(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new HttpCallBack<List<LeagueInfo>>() {
                    @Override
                    public void onResult(List<LeagueInfo> leagueInfoList) {
                        List<League> leagueList = new ArrayList<>();
                        for (LeagueInfo leagueInfo : leagueInfoList) {
                            leagueList.add(new LeagueFb(leagueInfo));
                        }
                        settingLeagueData.postValue(leagueList);
                        getLeagueAreaList(leagueList, false, leagueIdList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }
                });
        addSubscribe(disposable);
    }

}
