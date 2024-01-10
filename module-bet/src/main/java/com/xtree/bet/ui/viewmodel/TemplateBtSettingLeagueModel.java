package com.xtree.bet.ui.viewmodel;

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

public abstract class TemplateBtSettingLeagueModel extends BaseViewModel<BetRepository> implements BtSettingLeagueModel {
    private Disposable mSubscription;
    public SingleLiveData<List<InitialLeagueArea>> settingInitialLeagueAreaData = new SingleLiveData<>();
    public SingleLiveData<List<InitialLeagueArea>> settingSearchInitialLeagueAreaData = new SingleLiveData<>();
    public SingleLiveData<List<League>> settingLeagueData = new SingleLiveData<>();
    /**
     * 检查是否选中所有联赛
     */
    public SingleLiveData<BetContract> betContractIsCheckedAllLeagueData = new SingleLiveData<>();

    public TemplateBtSettingLeagueModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void addSubscription() {
        mSubscription = RxBus.getDefault().toObservable(BetContract.class)
                .subscribe(betContract -> {
                    betContractIsCheckedAllLeagueData.postValue(betContract);
                });
        addSubscribe(mSubscription);
    }

    public void getLeagueAreaList(List<League> leagueList, boolean isSearch, List<Long> leagueIdList){

        // 把后台查询到的联赛列表按地区分组 begin
        List<LeagueArea> leagueAreaList = new ArrayList<>();
        Map<String, LeagueArea> leagueAreaMap = new HashMap<>();

        LeagueArea hotLeagueArea = new LeagueArea();
        hotLeagueArea.setName("热门联赛");

        InitialLeagueArea hotInitialLeagueArea = new InitialLeagueArea();
        hotInitialLeagueArea.setName("热");
        hotInitialLeagueArea.getLeagueAreaList().add(hotLeagueArea);

        LeagueArea leagueArea;
        for (League league : leagueList) {
            if(leagueIdList != null){
                league.setSelected(leagueIdList.contains(league.getId()));
            }
            leagueArea = leagueAreaMap.get(String.valueOf(league.getAreaId()));
            if(leagueArea == null){
                leagueArea = new LeagueArea();
                leagueArea.setName(league.getLeagueAreaName());
                leagueAreaMap.put(String.valueOf(league.getAreaId()), leagueArea);
                leagueAreaList.add(leagueArea);
            }
            leagueArea.addLeagueList(league);
            if(league.isHot()){
                hotLeagueArea.addLeagueList(league);
            }
        }

        for(LeagueArea area : leagueAreaMap.values()){
            boolean isCheckAll = true;
            for (League league : area.getLeagueList()){
                if(!league.isSelected()){
                    isCheckAll = false;
                    break;
                }
            }
            area.setSelected(isCheckAll);
        }

        boolean isCheckAll = true;
        for (League league : hotLeagueArea.getLeagueList()){
            if(!league.isSelected()){
                isCheckAll = false;
                break;
            }
        }
        hotLeagueArea.setSelected(isCheckAll);

        // 把后台查询到的联赛列表按地区分组 end

        // 把得到的地区分组按首字母分组 begin
        TreeMap<String, InitialLeagueArea> initialLeagueAreaMap = new TreeMap<>();
        for (LeagueArea area : leagueAreaList) {

            if(!TextUtils.isEmpty(area.getName())) {

                String initial = StringUtils.getPinYinInitials(area.getName())[0]; // 首字母
                InitialLeagueArea initialLeagueArea = initialLeagueAreaMap.get(initial);

                if(initialLeagueArea == null) {
                    initialLeagueArea = new InitialLeagueArea();
                    initialLeagueArea.setName(initial);
                    initialLeagueAreaMap.put(initial, initialLeagueArea);
                }

                initialLeagueArea.addLeagueList(area);
            }
        }
        // 把得到的地区分组按首字母分组 end

        List<InitialLeagueArea> initialLeagueAreaList = new ArrayList<>();
        initialLeagueAreaList.add(hotInitialLeagueArea);
        for (Map.Entry<String, InitialLeagueArea> entry : initialLeagueAreaMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            initialLeagueAreaList.add(entry.getValue());
        }

        if(!initialLeagueAreaMap.isEmpty()) {
            if(isSearch){
                settingSearchInitialLeagueAreaData.postValue(initialLeagueAreaList);
            }else {
                settingInitialLeagueAreaData.postValue(initialLeagueAreaList);
            }
        }
    }

}
