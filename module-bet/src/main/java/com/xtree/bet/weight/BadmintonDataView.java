package com.xtree.bet.weight;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.Constants;

/**
 * 羽毛球相关数据view(第一盘，第二盘比分等)
 */
public class BadmintonDataView extends BaseDetailDataView {

    public BadmintonDataView(@NonNull Context context, Match match) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new Integer[]{47002, 47003, 47004, 47005, 47006};
        scoreType = Constants.SCORE_TYPE_JF;
        setMatch(match);
    }
}
