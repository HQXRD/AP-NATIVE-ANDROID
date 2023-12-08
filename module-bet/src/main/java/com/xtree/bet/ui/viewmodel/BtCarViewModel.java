package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.BtConfirmInfo;
import com.xtree.bet.bean.BtConfirmOptionInfo;
import com.xtree.bet.bean.CgOddLimitInfo;
import com.xtree.bet.bean.LeagueItem;
import com.xtree.bet.bean.MatchInfo;
import com.xtree.bet.bean.MatchItem;
import com.xtree.bet.bean.MatchListRsp;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.bean.ui.CgOddLimitFb;
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

public class BtCarViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;
    public SingleLiveData<List<BetConfirmOption>> btConfirmInfoDate = new SingleLiveData<>();
    public SingleLiveData<List<CgOddLimit>> cgOddLimitDate = new SingleLiveData<>();

    public BtCarViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }


    public void addSubscription() {

    }

    public void setOptionData(int id) {
        InputStream inputStream = Utils.getContext().getResources().openRawResource(id);
        String json = null;
        try {
            json = readTextFile(inputStream);
        } catch (IOException e) {
            String s = e.getMessage();
            String sq = s;
        }
        BtConfirmInfo btConfirmInfo = new Gson().fromJson(json, BtConfirmInfo.class);
        List<BetConfirmOption> betConfirmOptionList = new ArrayList<>();
        for (BtConfirmOptionInfo btConfirmOptionInfo : btConfirmInfo.bms) {
            betConfirmOptionList.add(new BetConfirmOptionFb(btConfirmOptionInfo, ""));
        }
        btConfirmInfoDate.postValue(betConfirmOptionList);
    }

    public void setCgData(int id) {
        InputStream inputStream = Utils.getContext().getResources().openRawResource(id);
        String json = null;
        try {
            json = readTextFile(inputStream);
        } catch (IOException e) {
            String s = e.getMessage();
            String sq = s;
        }
        BtConfirmInfo btConfirmInfo = new Gson().fromJson(json, BtConfirmInfo.class);
        List<CgOddLimit> cgOddLimitInfoList = new ArrayList<>();
        if(!btConfirmInfo.sos.isEmpty()) {
            int index = 0;
            for (CgOddLimitInfo cgOddLimitInfo : btConfirmInfo.sos) {
                cgOddLimitInfoList.add(new CgOddLimitFb(cgOddLimitInfo, btConfirmInfo.bms.get(index++), btConfirmInfo.bms.size()));
            }
        }else{
            cgOddLimitInfoList.add(new CgOddLimitFb(null, btConfirmInfo.bms.get(0), 0));
        }
        cgOddLimitDate.postValue(cgOddLimitInfoList);
    }

    private String readTextFile(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
