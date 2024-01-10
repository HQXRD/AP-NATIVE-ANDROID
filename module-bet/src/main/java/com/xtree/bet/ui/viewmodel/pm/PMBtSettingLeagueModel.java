package com.xtree.bet.ui.viewmodel.pm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.bet.bean.response.pm.LeagueArea;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtSettingLeagueModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.utils.RxUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public class PMBtSettingLeagueModel extends TemplateBtSettingLeagueModel {

    public PMBtSettingLeagueModel(@NonNull Application application, BetRepository repository) {
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

        Disposable disposable = (Disposable) model.getPMApiService().getOnSaleLeagues(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<LeagueArea>>() {
                    @Override
                    public void onResult(List<LeagueArea> leagueAreas) {
                        List<League> leagueList = new ArrayList<>();
                        for (LeagueArea leagueArea : leagueAreas) {
                            if(leagueArea.sportVOs != null && !leagueArea.sportVOs.isEmpty()) {
                                for (LeagueArea.SportVOsBean.LeagueInfo league :leagueArea.sportVOs.get(0).tournamentList){

                                }
                            }
                            leagueList.add(new LeaguePm(leagueInfo));
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
