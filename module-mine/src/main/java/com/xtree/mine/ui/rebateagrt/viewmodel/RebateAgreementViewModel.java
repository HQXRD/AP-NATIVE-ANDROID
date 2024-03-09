package com.xtree.mine.ui.rebateagrt.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.mvvm.model.ToolbarModel;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;
import com.xtree.mine.data.MineRepository;

import java.util.ArrayList;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/3/8.
 * Describe: 返水契约 viewmodel
 */
public class RebateAgreementViewModel extends BaseViewModel<MineRepository> implements ToolbarModel {

    public RebateAgreementViewModel(@NonNull Application application) {
        super(application);
    }

    public RebateAgreementViewModel(@NonNull Application application, MineRepository model) {
        super(application, model);

        ArrayList<Integer> itemTypeList = new ArrayList<>();
        itemTypeList.add(R.layout.item_rebate_agreement);
        itemType.setValue(itemTypeList);

        ArrayList<BindModel> list = new ArrayList<>();
        list.add(new BindModel());
        list.add(new BindModel());
        list.add(new BindModel());
        datas.setValue(list);
    }

    private final MutableLiveData<String> titleData = new MutableLiveData<>(
            getApplication().getString(R.string.rebate_agrt_title)
    );

    public MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>();

    @Override
    public void onBack() {

    }

    @Override
    public MutableLiveData<String> getTitle() {
        return titleData;
    }

}
