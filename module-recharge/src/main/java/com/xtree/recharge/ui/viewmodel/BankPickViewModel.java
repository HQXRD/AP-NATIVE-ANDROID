package com.xtree.recharge.ui.viewmodel;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.recharge.R;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.ui.model.BankPickModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/5/27.
 * Describe:
 */
public class BankPickViewModel extends BaseViewModel<RechargeRepository> implements TextWatcher {
    public BankPickViewModel(@NonNull Application application) {
        super(application);
    }

    public BankPickViewModel(@NonNull Application application, RechargeRepository model) {
        super(application, model);
    }

    public final MutableLiveData<List<BindModel>> mBindDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<BankPickModel> lastTimeDatas = new MutableLiveData<>();
    public final MutableLiveData<List<BindModel>> topTenDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> hotDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> otherDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> searchDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<Boolean> showSearch = new MutableLiveData<>(false);

    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_rc_choose_bank);
                }
            });

    public void initData(FragmentActivity fragmentActivity) {
        BankPickModel m = new BankPickModel("光大银行");
        mBindDatas.getValue().add(m);
        lastTimeDatas.setValue(m);
        List<BindModel> bindModels = topTenDatas.getValue();
        bindModels.add(m);
        bindModels.add(m);
        bindModels.add(m);
        bindModels.add(m);
        hotDatas.getValue().add(m);
        otherDatas.getValue().add(m);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //模糊搜索
        String sc = s.toString().trim();

        ArrayList<BindModel> searchList = new ArrayList<>();
        ArrayList<BindModel> allBanks = new ArrayList<>();
        allBanks.addAll(topTenDatas.getValue());
        allBanks.addAll(hotDatas.getValue());
        allBanks.addAll(otherDatas.getValue());

        for (BindModel allBank : allBanks) {
            BankPickModel bank = (BankPickModel) allBank;
            if (bank.getBankName().contains(sc)) {
                searchList.add(bank);
            }
        }

        HashSet<BindModel> set = new HashSet<>(searchList);
        List<BindModel> list = new ArrayList<>(set);
        searchDatas.setValue(list);
        showSearch.setValue(!sc.isEmpty());
    }
}
