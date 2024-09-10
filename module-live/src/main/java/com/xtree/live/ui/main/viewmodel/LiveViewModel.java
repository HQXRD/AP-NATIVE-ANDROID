package com.xtree.live.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.ui.main.model.LiveAnchorModel;
import com.xtree.live.ui.main.model.LiveHotModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户viewModel
 */
public class LiveViewModel extends BaseViewModel<LiveRepository> implements TabLayout.OnTabSelectedListener {

    public LiveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }

    private WeakReference<FragmentActivity> mActivity = null;

    public ObservableField<ArrayList<String>> tabs = new ObservableField<>();
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.layout_live_anchor);
                    add(R.layout.layout_live_hot);
                }
            });

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        LiveAnchorModel liveAnchorModel = new LiveAnchorModel();
        liveAnchorModel.setItemType(0);

        LiveHotModel liveHotModel = new LiveHotModel();
        liveHotModel.setItemType(1);

        add(liveAnchorModel);

        add(liveHotModel);
    }};

    public OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        }
    };

    public void initData(FragmentActivity mActivity) {

        setActivity(mActivity);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("直播");
        strings.add("热门");
        strings.add("足球");
        strings.add("篮球");
        strings.add("其他");
        tabs.set(strings);

        datas.setValue(bindModels);
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}

