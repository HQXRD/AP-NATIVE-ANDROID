package com.xtree.bet.weight;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;

/**
 * 网球相关数据view(第一盘，第二盘比分等)
 */
public class SnkDataView extends BaseDetailDataView {

    public SnkDataView(@NonNull Context context, Match match) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new Integer[]{13002, 13003, 13004, 13005, 13006, 13007, 13008, 13009};
        scoreType = Constants.SCORE_TYPE_SNK_JF;
        setMatch(match);
    }
}
