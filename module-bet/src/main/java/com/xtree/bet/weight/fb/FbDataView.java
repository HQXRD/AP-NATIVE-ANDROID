package com.xtree.bet.weight.fb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

import java.util.List;

/**
 * 足球相关数据view(如红牌，角球等)
 */
public class FbDataView extends BaseDetailDataView implements View.OnClickListener {
    public FbDataView(@NonNull Context context, Match match) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_fb_data, this);
        scoreType = new String[]{String.valueOf(FBConstants.SCORE_TYPE_SCORE)};
        setMatch(match);
    }

    public void setMatch(Match match){
        List<Integer> scoreYellowCard = match.getScore(String.valueOf(FBConstants.SCORE_TYPE_YELLOW_CARD));
        String yellowCard = hasScore(scoreYellowCard) ? getResources().getString(R.string.bt_detail_yellow_card, scoreYellowCard.get(0), scoreYellowCard.get(1)) : "  -";
        ((TextView)findViewById(R.id.tv_yellow_card)).setText(yellowCard);

        List<Integer> scoreRedCard = match.getScore(String.valueOf(FBConstants.SCORE_TYPE_RED_CARD));
        String redCard = hasScore(scoreRedCard) ? getResources().getString(R.string.bt_detail_red_card, scoreRedCard.get(0), scoreRedCard.get(1)) : "  -";
        ((TextView)findViewById(R.id.tv_red_card)).setText(redCard);

        List<Integer> scoreCornor = match.getScore(String.valueOf(FBConstants.SCORE_TYPE_CORNER));
        String cornorCard = hasScore(scoreCornor) ? getResources().getString(R.string.bt_detail_cornor, scoreCornor.get(0), scoreCornor.get(1)) : "  -";
        ((TextView)findViewById(R.id.tv_cornor)).setText(cornorCard);
        findViewById(R.id.iv_cornor).setSelected(match.hasCornor());
    }

    @Override
    public void onClick(View view) {
    }
}
