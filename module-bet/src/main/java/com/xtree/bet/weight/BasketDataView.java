package com.xtree.bet.weight;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Score;
import com.xtree.bet.bean.ui.ScoreFb;
import com.xtree.bet.constant.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;

/**
 * 篮球相关数据view(第一节，第二节比分等)
 */
public class BasketDataView extends BaseDetailDataView {

    public BasketDataView(@NonNull Context context, Match match) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new Integer[]{3005, 3006, 3007, 3008, 3009};
        setMatch(match);
    }
}
