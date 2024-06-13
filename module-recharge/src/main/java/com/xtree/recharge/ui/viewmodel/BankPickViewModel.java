package com.xtree.recharge.ui.viewmodel;

import android.app.Application;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.recharge.R;
import com.xtree.recharge.data.RechargeRepository;
import com.xtree.recharge.ui.fragment.BankPickDialogFragment;
import com.xtree.recharge.ui.model.BankPickGroupModel;
import com.xtree.recharge.ui.model.BankPickModel;
import com.xtree.recharge.vo.RechargeVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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

    private final MutableLiveData<List<BindModel>> mBindDatas = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<BindModel>> lastTimeDatas = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<BindModel>> topTenDatas = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<BindModel>> hotDatas = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<BindModel>> otherDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> searchDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<List<BindModel>> groupDatas = new MutableLiveData<>(new ArrayList<>());
    public final MutableLiveData<Boolean> showSearch = new MutableLiveData<>(false);
    public RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

    private BankPickDialogFragment.onPickListner onPickListner;

    public final MutableLiveData<ArrayList<Integer>> groupItemType = new MutableLiveData<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_rc_choose_bank_group);
                }
            });

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

            Completable.fromRunnable(() -> {

                        ArrayList<BindModel> group = new ArrayList<>();
                        ArrayList<BindModel> hot = new ArrayList<>();
                        ArrayList<BindModel> top = new ArrayList<>();
                        ArrayList<BindModel> other = new ArrayList<>();
                        ArrayList<BindModel> mbind = new ArrayList<>();
                        ArrayList<BindModel> last = new ArrayList<>();

                        if (bankListData.getmBind() != null) {
                            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getmBind()) {
                                BankPickModel m = new BankPickModel();
                                m.setItemType(0);
                                m.setBankId(bankInfoDTO.getBankCode());
                                String value = bankInfoDTO.getBankName();
                                if (value.contains("-")) {
                                    String[] split = value.split("--");
                                    value = split[0];
                                }
                                m.setBankName(value);
                                m.setClick(itemClick);
                                mbind.add(m);
                            }
                            BankPickGroupModel mBindGroup = new BankPickGroupModel();
                            mBindGroup.setTitle("您的绑定卡银行");
                            mBindGroup.setItemTypes(itemType.getValue());
                            mBindGroup.bindModels.set(mbind);
                            mBindGroup.setSpaceCount(2);
                            group.add(mBindGroup);
                        }

                        if (bankListData.getUsed() != null) {
                            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getUsed()) {
                                BankPickModel m = new BankPickModel();
                                m.setItemType(1);
                                m.setBankCode(bankInfoDTO.getBankCode());
                                m.setBankName(bankInfoDTO.getBankName());
                                m.setClick(itemClick);
                                last.add(m);
                            }
                            BankPickGroupModel usedGroup = new BankPickGroupModel();
                            usedGroup.setTitle("您上次选择的银行");
                            usedGroup.setItemTypes(itemType.getValue());
                            usedGroup.bindModels.set(last);
                            usedGroup.setSpaceCount(2);
                            group.add(usedGroup);
                        }

                        if (bankListData.getTop()!=null){
                            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getTop()) {
                                BankPickModel m = new BankPickModel();
                                m.setItemType(3);
                                m.setBankCode(bankInfoDTO.getBankCode());
                                m.setBankName(bankInfoDTO.getBankName());
                                m.setClick(itemClick);
                                top.add(m);
                            }
                            BankPickGroupModel topGroup = new BankPickGroupModel();
                            topGroup.setTitle("十大银行");
                            topGroup.setItemTypes(itemType.getValue());
                            topGroup.bindModels.set(top);
                            group.add(topGroup);
                        }

                        if (bankListData.getHot()!=null){
                            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getHot()) {
                                BankPickModel m = new BankPickModel();
                                m.setItemType(2);
                                m.setBankCode(bankInfoDTO.getBankCode());
                                m.setBankName(bankInfoDTO.getBankName());
                                m.setClick(itemClick);
                                hot.add(m);
                            }
                            BankPickGroupModel hotGroup = new BankPickGroupModel();
                            hotGroup.setTitle("热门银行");
                            hotGroup.setItemTypes(itemType.getValue());
                            hotGroup.bindModels.set(hot);
                            group.add(hotGroup);
                        }

                        if (bankListData.getOthers()!=null){
                            for (RechargeVo.OpBankListDTO.BankInfoDTO bankInfoDTO : bankListData.getOthers()) {
                                BankPickModel m = new BankPickModel();
                                m.setItemType(3);
                                m.setBankCode(bankInfoDTO.getBankCode());
                                m.setBankName(bankInfoDTO.getBankName());
                                m.setClick(itemClick);
                                other.add(m);
                            }
                            BankPickGroupModel othersGroup = new BankPickGroupModel();
                            othersGroup.setTitle("其他银行");
                            othersGroup.setItemTypes(itemType.getValue());
                            othersGroup.bindModels.set(other);
                            group.add(othersGroup);
                        }

                        hotDatas.postValue(hot);
                        otherDatas.postValue(other);
                        topTenDatas.postValue(top);
                        lastTimeDatas.postValue(last);
                        mBindDatas.postValue(mbind);
                        groupDatas.postValue(group);
                    })
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe();
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
        allBanks.addAll(mBindDatas.getValue());
        allBanks.addAll(lastTimeDatas.getValue());

        for (BindModel allBank : allBanks) {
            BankPickModel bank = (BankPickModel) allBank;
            if (bank.getBankName().toLowerCase().contains(sc.toLowerCase())) {
                BankPickModel banknew = new BankPickModel();
                banknew.setItemType(3);
                banknew.setBankId(bank.getBankId());
                banknew.setBankName(bank.getBankName());
                banknew.setBankCode(bank.getBankCode());
                banknew.setClick(itemClick);
                searchList.add(banknew);
            }
        }

        HashSet<BindModel> set = new HashSet<>(searchList);
        List<BindModel> list = new ArrayList<>(set);
        searchDatas.setValue(list);
        showSearch.setValue(!sc.isEmpty());
    }
}
