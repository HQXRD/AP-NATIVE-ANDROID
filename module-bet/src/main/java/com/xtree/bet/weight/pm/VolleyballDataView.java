package com.xtree.bet.weight.pm;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 排球相关数据view(第一盘，第二盘比分等)
 */
public class VolleyballDataView extends BaseDetailDataView {

    public VolleyballDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"S120", "S121", "S122", "S123", "S124"};
        scoreType = periods;
        setMatch(match, isMatchList);
        if(isMatchList) {
            addMatchListAdditional(match.getFormat() + " 总分");
        }
    }

    
}
