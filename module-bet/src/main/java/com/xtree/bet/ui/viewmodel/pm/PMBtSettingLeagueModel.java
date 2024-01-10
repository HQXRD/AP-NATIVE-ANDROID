package com.xtree.bet.ui.viewmodel.pm;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.PMHttpCallBack;
import com.xtree.base.utils.StringUtils;
import com.xtree.bet.bean.response.pm.LeagueAreaInfo;
import com.xtree.bet.bean.response.pm.LeagueInfo;
import com.xtree.bet.bean.ui.InitialLeagueArea;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueArea;
import com.xtree.bet.bean.ui.LeaguePm;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.viewmodel.TemplateBtSettingLeagueModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        map.put("euid", String.valueOf(sportId + 20000));
        map.put("device", "v2_h5");
        map.put("type", String.valueOf(type));
        map.put("inputText", "");
        map.put("md", "");

        Disposable disposable = (Disposable) model.getPMApiService().getOnSaleLeagues(map)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribeWith(new PMHttpCallBack<List<LeagueAreaInfo>>() {
                    @Override
                    public void onResult(List<LeagueAreaInfo> leagueAreas) {
                        List<League> leagueList = new ArrayList<>();
                        for (LeagueAreaInfo leagueArea : leagueAreas) {
                            if(leagueArea.sportVOs != null && !leagueArea.sportVOs.isEmpty()) {
                                for (LeagueInfo leagueInfo :leagueArea.sportVOs.get(0).tournamentList){
                                    leagueInfo.regionName = leagueArea.introduction;
                                    if(TextUtils.equals("HOT", leagueArea.spell)){
                                        leagueInfo.regionId = Integer.valueOf(leagueArea.id);
                                    }
                                    leagueList.add(new LeaguePm(leagueInfo));
                                }
                            }
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

    /*@Override
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
            if(TextUtils.equals("R", entry.getKey())){
                for (LeagueArea area : entry.getValue().getLeagueAreaList()){
                    if(TextUtils.equals("热门联赛", area.getName())){
                        entry.getValue().getLeagueAreaList().remove(area);
                        break;
                    }
                }
            }
            if(!entry.getValue().getLeagueAreaList().isEmpty()) {
                initialLeagueAreaList.add(entry.getValue());
            }
        }

        if(!initialLeagueAreaMap.isEmpty()) {
            if(isSearch){
                settingSearchInitialLeagueAreaData.postValue(initialLeagueAreaList);
            }else {
                settingInitialLeagueAreaData.postValue(initialLeagueAreaList);
            }
        }
    }*/

}
