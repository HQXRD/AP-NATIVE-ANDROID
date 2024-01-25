package com.xtree.bet.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.FBHttpCallBack;
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

public interface BtSettingLeagueModel {

    /**
     * 获取联赛列表
     */
    void getOnSaleLeagues(int sportId, int type, List<Long> leagueIdList);

    void getLeagueAreaList(List<League> leagueList, boolean isSearch, List<Long> leagueIdList);

}
