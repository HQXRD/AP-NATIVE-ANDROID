package com.xtree.bet.weight;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;

/**
 * 海滩排球相关数据view(第一盘，第二盘比分等)
 */
public class StVolleyballDataView extends BaseDetailDataView {

    public StVolleyballDataView(@NonNull Context context, Match match) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new Integer[]{51002, 51003, 51004, 51005, 51006};
        scoreType = Constants.SCORE_TYPE_PF;
        setMatch(match);
    }
}
