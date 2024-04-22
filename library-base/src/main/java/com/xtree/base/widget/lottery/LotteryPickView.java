package com.xtree.base.widget.lottery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.xtree.base.databinding.LayoutLotteryPickBinding;

/**
 * Created by KAKA on 2024/4/22.
 * Describe: 彩票选注
 */
public class LotteryPickView extends LinearLayout {

    private LayoutLotteryPickBinding binding;

    public LotteryPickView(Context context) {
        super(context);
        binding = LayoutLotteryPickBinding.inflate(LayoutInflater.from(context), this, false);

        initView();
    }

    public LotteryPickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutLotteryPickBinding.inflate(LayoutInflater.from(context), this, false);

        initView();
    }

    private void initView() {


    }

}
