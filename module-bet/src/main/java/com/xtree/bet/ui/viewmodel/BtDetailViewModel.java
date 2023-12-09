package com.xtree.bet.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.bet.R;
import com.xtree.bet.bean.MatchInfo;
import com.xtree.bet.bean.PlayTypeInfo;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.CategoryFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.MatchFb;
import com.xtree.bet.bean.ui.PlayTypeFb;
import com.xtree.bet.constant.MarketTag;
import com.xtree.bet.data.BetRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxSubscriptions;
import me.xtree.mvvmhabit.bus.event.SingleLiveData;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * Created by goldze on 2018/6/21.
 */

public class BtDetailViewModel extends BaseViewModel<BetRepository> {
    private Disposable mSubscription;

    public SingleLiveData<List<Category>> categoryListData = new SingleLiveData<>();
    public SingleLiveData<Match> matchData = new SingleLiveData<>();

    public BtDetailViewModel(@NonNull Application application, BetRepository repository) {
        super(application, repository);
    }

    public void setCategoryList(){
        InputStream inputStream = Utils.getContext().getResources().openRawResource(R.raw.test_detail);

        String json = null;
        try {
            json = readTextFile(inputStream);
        } catch (IOException e) {
        }
        MatchInfo matchInfo = new Gson().fromJson(json, MatchInfo.class);
        matchData.postValue(new MatchFb(matchInfo));
        categoryListData.postValue(getCategoryList(matchInfo, true));

    }

    public static List<Category> getCategoryList(MatchInfo matchInfo, boolean isFb) {
        Map<String, Category> map = new HashMap<>();
        List<Category> categoryList = new ArrayList<>();
        for (PlayTypeInfo playTypeInfo : matchInfo.mg) {
            for(String type : playTypeInfo.tps){
                if(map.get(type) == null){
                    map.put(type, new CategoryFb(playTypeInfo, MarketTag.getMarketTag(type)));
                }
                map.get(type).addPlayTypeList(new PlayTypeFb(playTypeInfo));
            }
        }
        categoryList.addAll(map.values());
        return categoryList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxSubscriptions.remove(mSubscription);
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
}
