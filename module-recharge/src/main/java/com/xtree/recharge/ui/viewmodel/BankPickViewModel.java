package com.xtree.recharge.ui.viewmodel;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.recharge.R;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.ui.fragment.BankPickDialogFragment;
import com.xtree.recharge.ui.model.BankPickModel;
import com.xtree.recharge.vo.RechargeVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
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
    public final MutableLiveData<List<BindModel>> lastTimeDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> topTenDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> hotDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> otherDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> searchDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<Boolean> showSearch = new MutableLiveData<>(false);

    private BankPickDialogFragment.onPickListner onPickListner;

    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_rc_choose_bank1);
                    add(R.layout.item_rc_choose_bank2);
                    add(R.layout.item_rc_choose_bank3);
                    add(R.layout.item_rc_choose_bank4);
                }
            });

    public RechargeVo.OpBankListDTO bankListData;

    public void initData(RechargeVo.OpBankListDTO bankListData) {
        this.bankListData = bankListData;

        searchDatas.getValue().clear();
        showSearch.setValue(false);

        if (bankListData != null) {

            hotDatas.getValue().clear();
            topTenDatas.getValue().clear();
            otherDatas.getValue().clear();
            mBindDatas.getValue().clear();
            lastTimeDatas.getValue().clear();

            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getHot()) {
                BankPickModel m = new BankPickModel();
                m.setItemType(2);
                m.setBankCode(bankInfoDTO.getBankCode());
                m.setBankName(bankInfoDTO.getBankName());
                m.setClick(itemClick);
                hotDatas.getValue().add(m);
            }
            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getTop()) {
                BankPickModel m = new BankPickModel();
                m.setItemType(3);
                m.setBankCode(bankInfoDTO.getBankCode());
                m.setBankName(bankInfoDTO.getBankName());
                m.setClick(itemClick);
                topTenDatas.getValue().add(m);
            }
            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getOthers()) {
                BankPickModel m = new BankPickModel();
                m.setItemType(3);
                m.setBankCode(bankInfoDTO.getBankCode());
                m.setBankName(bankInfoDTO.getBankName());
                m.setClick(itemClick);
                otherDatas.getValue().add(m);
            }
            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getUsed()) {
                BankPickModel m = new BankPickModel();
                m.setItemType(1);
                m.setBankCode(bankInfoDTO.getBankCode());
                m.setBankName(bankInfoDTO.getBankName());
                m.setClick(itemClick);
                lastTimeDatas.getValue().add(m);
            }
            for (Map.Entry<String, String> entry : bankListData.getmBind().entrySet()) {
                BankPickModel m = new BankPickModel();
                m.setItemType(0);
                m.setBankId(entry.getKey());
                String value = entry.getValue();
                if (value.contains("-")) {
                    String[] split = value.split("--");
                    value = split[0];
                }
                m.setBankName(value);
                m.setClick(itemClick);
                mBindDatas.getValue().add(m);
            }
        }
    }

    private final Consumer<BindModel> itemClick = new Consumer<BindModel>() {
        @Override
        public void accept(BindModel model) throws Exception {
            if (model instanceof BankPickModel && bankListData != null) {
                if (onPickListner != null) {
                    onPickListner.onPick((BankPickModel) model);
                }
                finish();
            }
        }
    };

    public void setOnPickListner(BankPickDialogFragment.onPickListner onPickListner) {
        this.onPickListner = onPickListner;
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
                BankPickModel banknew = new BankPickModel();
                banknew.setItemType(3);
                banknew.setBankId(bank.getBankId());
                banknew.setBankName(bank.getBankName());
                banknew.setBankCode(bank.getBankCode());
                searchList.add(banknew);
            }
        }

        HashSet<BindModel> set = new HashSet<>(searchList);
        List<BindModel> list = new ArrayList<>(set);
        searchDatas.setValue(list);
        showSearch.setValue(!sc.isEmpty());
    }
}
