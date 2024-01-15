package com.xtree.bet.weight.pm;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 篮球相关数据view(第一节，第二节比分等)
 */
public class BasketDataView extends BaseDetailDataView {

    public BasketDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"S19", "S20", "S21", "S22", "S7"};
        scoreType = periods;
        setMatch(match, isMatchList);
    }

    
}
