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
public class TableTennisDataView extends BaseDetailDataView {

    public TableTennisDataView(@NonNull Context context, Match match) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new Integer[]{15002, 15003, 15004, 15005, 15006, 15007, 15008};
        scoreType = Constants.SCORE_TYPE_JF;
        setMatch(match);
    }
}
