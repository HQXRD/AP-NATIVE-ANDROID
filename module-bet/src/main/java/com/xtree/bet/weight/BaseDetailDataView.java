package com.xtree.bet.weight;

import static com.xtree.bet.constant.Constants.SCORE_TYPE_SCORE;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Score;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.constant.SportTypeContants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public class BaseDetailDataView extends ConstraintLayout{
    LinearLayout root;
    /**
     * 要获取比分的阶段
     */
    Integer[] periods;
    /**
     * 要获取的比分类型
     */
    int scoreType = SCORE_TYPE_SCORE;
    public BaseDetailDataView(@NonNull Context context) {
        super(context);
    }

    public BaseDetailDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseDetailDataView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMatch(Match match){
        List<Score> scoreList = match.getScoreList(scoreType);
        List<Score> scores = new ArrayList<>();
        List<Integer> periodList = Arrays.asList(periods);
        for(Score score : scoreList){
            if(periodList.contains(score.getPeriod())){
                scores.add(score);
            }
        }

        for(int i = 0; i < scores.size(); i ++){
            Score score = scores.get(i);
            TextView textView;
            if(root.getChildAt(i) == null){
                textView = new TextView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = ConvertUtils.dp2px(5);
                textView.setLayoutParams(params);
                int color = i == scores.size() - 1 ? R.color.bt_color_car_dialog_hight_line2 : R.color.bt_color_under_bg_primary_text;
                textView.setTextColor(getResources().getColor(color));
                textView.setTextSize(10);
            }else{
                textView = (TextView) root.getChildAt(i);
            }
            if(i == scores.size() - 1){
                Log.e("test", getResources().getString(R.string.bt_detail_score, score.getScores().get(0), score.getScores().get(1)));
            }
            textView.setText(getResources().getString(R.string.bt_detail_score, score.getScores().get(0), score.getScores().get(1)));
            if(root.getChildAt(i) == null) {
                root.addView(textView);
            }
        }
    }

    public static BaseDetailDataView getInstance(Context context, Match match){
        int sportType = SPUtils.getInstance().getInt(SPKey.BT_SPORT_ID);
        String sport = SportTypeContants.SPORT_IDS[sportType];
        if(sport.equals(SportTypeContants.SPORT_ID_FB)){
            return new FbDataView(context, match);
        } else if (sport.equals(SportTypeContants.SPORT_ID_BSB)) {
            return new BasketDataView(context, match);
        } else if (sport.equals(SportTypeContants.SPORT_ID_WQ)) {
            return new NetBallDataView(context, match);
        } else if (sport.equals(SportTypeContants.SPORT_ID_PQ)) {
            return new VolleyballDataView(context, match);
        } else if (sport.equals(SportTypeContants.SPORT_ID_STPQ)) {
            return new StVolleyballDataView(context, match);
        } else if (sport.equals(SportTypeContants.SPORT_ID_YMQ)) {
            return new BadmintonDataView(context, match);
        } else if (sport.equals(SportTypeContants.SPORT_ID_BBQ)) {
            return new TableTennisDataView(context, match);
        } else if (sport.equals(SportTypeContants.SPORT_ID_BQ)) {
            return new IceBallDataView(context, match);
        }/* else if (sport.equals(SportTypeContants.SPORT_ID_SNK)) {
            return new SnkDataView(context, match);
        }*/
        return null;
    }
}
