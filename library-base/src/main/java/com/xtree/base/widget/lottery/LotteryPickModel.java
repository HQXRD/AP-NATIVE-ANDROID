package com.xtree.base.widget.lottery;

import androidx.databinding.ObservableField;

import com.xtree.base.R;
import com.xtree.base.mvvm.recyclerview.BindModel;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/4/22.
 * Describe:
 */
public class LotteryPickModel extends BindModel {

    public int number = 0;
    public String table = "";
    public ObservableField<String> tag = new ObservableField<>("");
    public ObservableField<Integer> markColor = new ObservableField<>(BaseApplication.getInstance().getResources().getColor(R.color.textColor));
    public ObservableField<Boolean> checked = new ObservableField<>(false);

    public LotteryPickModel(int number,String table) {
        this.number = number;
        this.table = table;
    }

}
