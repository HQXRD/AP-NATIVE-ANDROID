package com.xtree.live.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.ui.main.model.LiveAnchorModel;
import com.xtree.live.ui.main.model.LiveBannerHeadModel;
import com.xtree.live.ui.main.model.LiveHotModel;
import com.xtree.live.ui.main.model.LiveTabHeadModel;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户viewModel
 */
public class LiveViewModel extends BaseViewModel<LiveRepository> {

    public LiveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }

    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<ArrayList<BindModel>>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_live_banner);
                    add(R.layout.item_live_tab);
                    add(R.layout.item_live_anchor);
                    add(R.layout.item_live_hot);
                }
            });

    private final LiveBannerHeadModel bannerHeadModel = new LiveBannerHeadModel();
    private final LiveTabHeadModel tabHeadModel = new LiveTabHeadModel(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        bannerHeadModel.setItemType(0);
        tabHeadModel.setItemType(1);

        add(tabHeadModel);
        add(bannerHeadModel);
    }};

    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        }
    };

    public void initData() {

        ArrayList<String> strings = new ArrayList<>();
        strings.add("tab");
        strings.add("tab");
        strings.add("tab");
        strings.add("tab");
        strings.add("tab");
        tabHeadModel.setTabs(strings);

        LiveAnchorModel liveAnchorModel = new LiveAnchorModel();
        liveAnchorModel.setItemType(2);

        LiveHotModel liveHotModel = new LiveHotModel();
        liveHotModel.setItemType(3);

        bindModels.add(liveAnchorModel);
        bindModels.add(liveAnchorModel);
        bindModels.add(liveAnchorModel);
        bindModels.add(liveAnchorModel);

        bindModels.add(liveHotModel);
        bindModels.add(liveHotModel);
        bindModels.add(liveHotModel);
        bindModels.add(liveHotModel);
        bindModels.add(liveHotModel);
        bindModels.add(liveHotModel);

        datas.setValue(bindModels);
    }
}

