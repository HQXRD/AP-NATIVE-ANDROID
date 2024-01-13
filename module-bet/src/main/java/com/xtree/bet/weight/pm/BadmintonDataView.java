package com.xtree.bet.weight.pm;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 羽毛球相关数据view(第一局，第二局比分等)  三局二胜
 */
public class BadmintonDataView extends BaseDetailDataView {

    public BadmintonDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"S120", "S121", "S122"};
        scoreType = periods;
        setMatch(match, isMatchList);
        if(isMatchList) {
            addMatchListAdditional("三局二胜 总分");
        }
    }

    
}
