package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.LeagueItem;
import com.xtree.bet.bean.MatchInfo;
import com.xtree.bet.bean.MatchItem;
import com.xtree.bet.bean.MatchListRsp;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.LeagueFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.contract.ExpandContract;
import com.xtree.bet.data.BetRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.bus.RxSubscriptions;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * Created by goldze on 2018/6/21.
 */

public class BtDetailViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;

    public BtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxSubscriptions.remove(mSubscription);
    }
}
