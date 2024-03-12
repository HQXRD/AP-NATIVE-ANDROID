package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.tabs.TabLayout;
import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.widget.impl.FilterViewOnClickListerner;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;
import com.xtree.mine.ui.rebateagrt.model.GameRebateAgrtHeadModel;
import com.xtree.mine.ui.rebateagrt.model.GameSubordinateagrtHeadModel;
import com.xtree.mine.ui.rebateagrt.model.GameSubordinaterebateHeadModel;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/3/9.
 * Describe: 游戏场馆返水契约viewmodel
 */
public class GameRebateAgrtViewModel extends BaseViewModel<MineRepository> implements ToolbarModel,FilterViewOnClickListerner, TabLayout.OnTabSelectedListener {

    public GameRebateAgrtViewModel(@NonNull Application application) {
        super(application);
    }

    public GameRebateAgrtViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);
    }

    private final MutableLiveData<String> titleData = new MutableLiveData<>(
            getApplication().getString(R.string.rebate_agrt_title)
    );

    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_game_rebateagrt);
                    add(R.layout.item_game_rebateagrt_head);
                    add(R.layout.item_game_subordinateagrt);
                    add(R.layout.item_game_subordinateagrt_head);
                    add(R.layout.item_game_subordinaterebate);
                    add(R.layout.item_game_subordinaterebate_head);
                }
            });

    private ArrayList<BindModel> subordinateRebateDatas  = new ArrayList<BindModel>() {{
        BindModel bindModel = new BindModel();
        bindModel.setItemType(4);
        add(bindModel);
        GameSubordinaterebateHeadModel headModel = new GameSubordinaterebateHeadModel();
        headModel.setItemType(5);
        add(headModel);
        add(new BindModel());
        add(new BindModel());

    }};
    private ArrayList<BindModel> gameRebateDatas = new ArrayList<BindModel>() {{
        GameRebateAgrtHeadModel headModel = new GameRebateAgrtHeadModel(GameRebateAgrtViewModel.this);
        headModel.setItemType(1);
        add(headModel);
        add(new BindModel());
        add(new BindModel());
        add(new BindModel());
        add(new BindModel());
    }};
    private ArrayList<BindModel> subordinateAgrtDatas  = new ArrayList<BindModel>() {{
        GameSubordinateagrtHeadModel headModel = new GameSubordinateagrtHeadModel();
        headModel.setItemType(3);
        add(headModel);
        BindModel bindModel1 = new BindModel();
        bindModel1.setItemType(2);
        add(bindModel1);
    }};

    @Override
    public void onBack() {

    }

    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }

    @Override
    public void onClick(String startDate, String endDate, String startTime, String endTime, String typeId, String typeId2, String statusId) {
        getRebateData();
    }

    private void getRebateData() {
        gameRebateDatas.add(new BindModel());
        datas.setValue(gameRebateDatas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                datas.setValue(gameRebateDatas);
                break;
            case 1:
                datas.setValue(subordinateAgrtDatas);
                break;
            case 2:
                datas.setValue(subordinateRebateDatas);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
