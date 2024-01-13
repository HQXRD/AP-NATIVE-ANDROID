package com.xtree.bet.weight.pm;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 网球相关数据view(第一盘，第二盘比分等)
 */
public class NetBallDataView extends BaseDetailDataView {

    public NetBallDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"S23", "S39", "S55", "S71", "S87"};
        scoreType = periods;
        setMatch(match, isMatchList);
        if(isMatchList) {
            addMatchListAdditional("三盘二胜 总分");
        }
    }

    
}
