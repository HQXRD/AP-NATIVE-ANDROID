package com.xtree.base.widget.lottery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.xtree.base.databinding.LayoutLotteryPickBinding;

import java.util.List;

/**
 * Created by KAKA on 2024/4/22.
 * Describe: 彩票选注
 */
public class LotteryPickView extends LinearLayout {

    private LotteryPickViewModel lotteryPickViewModel = null;

    public interface onPickListener{
        void onPick(List<Integer> pickNums);
    }

    private LayoutLotteryPickBinding binding;

    public LotteryPickView(Context context) {
        super(context);
        binding = LayoutLotteryPickBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    public LotteryPickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutLotteryPickBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    /**
     * 设置号码选中监听
     */
    public void setPickListener(onPickListener pickListener) {
        if (lotteryPickViewModel != null) {
            lotteryPickViewModel.pickListener = pickListener;
        }
    }

    private void initView() {
        lotteryPickViewModel = new LotteryPickViewModel();
        lotteryPickViewModel.layoutManager.set(new GridLayoutManager(getContext(), 5));
        lotteryPickViewModel.initData(null);
        binding.setModel(lotteryPickViewModel);
    }

    public LotteryPickView setCustomData(List<String> list) {
        lotteryPickViewModel.initData(list);
        return this;
    }

    public LotteryPickView toSingleMode() {
        lotteryPickViewModel.setMode(LotteryPickViewModel.SINGLE_MODE);
        return this;
    }

    /**
     * 显示遗漏
     */
    public void showOmission(List<Integer> list) {
        lotteryPickViewModel.showOmission(list);
    }

    /**
     * 显示冷热
     */
    public void showHotCold(List<Integer> list) {
        lotteryPickViewModel.showHotCold(list);
    }

    /**
     * 关闭遗漏、冷热
     */
    public void closeTag() {
        lotteryPickViewModel.closeTag();
    }

    /**
     * 获取所有选中号码
     */
    public List<Integer> getPick() {
        return lotteryPickViewModel.getPick();
    }

    /**
     * 选中所有
     */
    public void pickAll() {
        if (lotteryPickViewModel.getMode() == LotteryPickViewModel.SINGLE_MODE) {
            return;
        }
        lotteryPickViewModel.pickAll();
    }

    /**
     * 选中大数
     */
    public void pickLarge() {
        if (lotteryPickViewModel.getMode() == LotteryPickViewModel.SINGLE_MODE) {
            return;
        }
        lotteryPickViewModel.pickLarge();
    }

    /**
     * 选中小数
     */
    public void pickSmall() {
        if (lotteryPickViewModel.getMode() == LotteryPickViewModel.SINGLE_MODE) {
            return;
        }
        lotteryPickViewModel.pickSmall();
    }

    /**
     * 选中奇数
     */
    public void pickOdd() {
        if (lotteryPickViewModel.getMode() == LotteryPickViewModel.SINGLE_MODE) {
            return;
        }
        lotteryPickViewModel.pickOdd();
    }

    /**
     * 选中偶数
     */
    public void pickEven() {
        if (lotteryPickViewModel.getMode() == LotteryPickViewModel.SINGLE_MODE) {
            return;
        }
        lotteryPickViewModel.pickEven();
    }

    /**
     * 清除所有
     */
    public void pickClear() {
        lotteryPickViewModel.pickClear();
    }
}
