package com.xtree.live.ui.main.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.drake.brv.BindingAdapter;
import com.google.android.material.tabs.TabLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.xtree.base.mvvm.recyclerview.BaseDatabindingAdapter;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.live.R;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.ui.main.model.anchor.LiveAnchorModel;
import com.xtree.live.ui.main.model.hot.LiveHotModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/9/9.
 * Describe: 直播门户viewModel
 */
public class LiveViewModel extends BaseViewModel<LiveRepository> implements TabLayout.OnTabSelectedListener {

    private final ArrayList<BindModel> bindModels = new ArrayList<BindModel>() {{
        LiveAnchorModel liveAnchorModel = new LiveAnchorModel();
        liveAnchorModel.setItemType(0);

        LiveHotModel liveHotModel = new LiveHotModel("热门");
        liveHotModel.setItemType(1);

        LiveHotModel liveFootBallModel = new LiveHotModel("足球");
        liveFootBallModel.setItemType(1);

        LiveHotModel liveBasketBallModel = new LiveHotModel("篮球");
        liveBasketBallModel.setItemType(1);

        LiveHotModel liveOtherModel = new LiveHotModel("其他");
        liveOtherModel.setItemType(1);

        add(liveAnchorModel);
        add(liveHotModel);
        add(liveFootBallModel);
        add(liveBasketBallModel);
        add(liveOtherModel);
    }};
    public ObservableField<ArrayList<String>> tabs = new ObservableField<>(new ArrayList<>());
    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.layout_live_anchor);
                    add(R.layout.layout_live_hot);
                }
            });
    private WeakReference<FragmentActivity> mActivity = null;
    public LiveViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveViewModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }


    public void initData(FragmentActivity mActivity) {

        setActivity(mActivity);

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

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}

