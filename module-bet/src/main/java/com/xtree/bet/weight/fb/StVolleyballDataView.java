package com.xtree.bet.weight.fb;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 海滩排球相关数据view(第一盘，第二盘比分等)
 */
public class StVolleyballDataView extends BaseDetailDataView {

    public StVolleyballDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"51002", "51003", "51004", "51005", "51006"};
        scoreType = new String[]{String.valueOf(FBConstants.SCORE_TYPE_PF)};
        setMatch(match, isMatchList);
        if(isMatchList) {
            addMatchListAdditional("五局三胜 总分");
        }
    }

    
}
