package com.xtree.bet.weight.fb;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 羽毛球相关数据view(第一局，第二局比分等)
 */
public class BadmintonDataView extends BaseDetailDataView {

    public BadmintonDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"47002", "47003", "47004"/*, "47005", "47006"*/};
        scoreType = new String[]{String.valueOf(FBConstants.SCORE_TYPE_JF)};
        setMatch(match, isMatchList);
        if(isMatchList) {
            addMatchListAdditional("三局二胜 总分");
        }
    }
}
