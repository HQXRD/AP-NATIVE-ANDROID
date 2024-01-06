package com.xtree.bet.ui.viewmodel;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.base.net.HttpCallBack;
import com.xtree.bet.bean.response.fb.PlayTypeInfo;
import com.xtree.bet.bean.response.pm.MatchInfo;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.CategoryFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayGroupPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.bean.ui.PlayTypeFb;
import com.xtree.bet.constant.MarketTag;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.data.BetRepository;
import com.xtree.bet.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2018/6/21.
 */

public interface BtDetailViewModel{

    void getMatchDetail(long matchId);
}
