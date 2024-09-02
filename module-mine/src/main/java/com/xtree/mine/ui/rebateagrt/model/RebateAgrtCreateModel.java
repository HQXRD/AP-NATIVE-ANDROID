package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.base.BaseApplication;

/**
 * Created by KAKA on 2024/3/18.
 * Describe: 创建契约条目数据模型
 */
public class RebateAgrtCreateModel extends BindModel {
    public String minBet = "";
    public String minPlayer = "";
    public String ratio = "";
    private String type = "";
    private String subTitle1 = "";
    private String subTitle2 = "";
    private String subTitle3 = "";

    public ObservableField<String> numText = new ObservableField<>();

    public RebateAgrtCreateModel() {
    }

    public RebateAgrtCreateModel(String minBet, String minPlayer, String ratio) {
        this.minBet = minBet;
        this.minPlayer = minPlayer;
        this.ratio = ratio;
    }

    public void setType(String type) {
        switch (type) {

            case "1": //USER
                subTitle1 = "每日投注额";
                subTitle2 = "活跃人数 ≥";
                subTitle3 = "彩票工资(%)";
                break;
            case "2": //LIVE
            case "3": //SPORT
            case "5": //CHESS
            case "6": // EGAME
                subTitle1 = BaseApplication.getInstance().getString(R.string.txt_daily_effective_bet_amount);
                subTitle2 = BaseApplication.getInstance().getString(R.string.txt_num_active_persons_morethan);
                subTitle3 = BaseApplication.getInstance().getString(R.string.txt_rebate_percent);
                break;
            default:
                break;
        }
        this.type = type;
    }

    public String getMinBet() {
        return minBet;
    }

    public void setMinBet(String minBet) {
        this.minBet = minBet;
    }

    public String getMinPlayer() {
        return minPlayer;
    }

    public void setMinPlayer(String minPlayer) {
        this.minPlayer = minPlayer;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getSubTitle1() {
        return subTitle1;
    }

    public String getSubTitle2() {
        return subTitle2;
    }

    public String getSubTitle3() {
        return subTitle3;
    }
}
