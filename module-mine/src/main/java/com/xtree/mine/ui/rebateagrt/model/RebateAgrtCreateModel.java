package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/18.
 * Describe:
 */
public class RebateAgrtCreateModel extends BindModel {
    public String minBet = "";
    public String minPlayer = "";
    public String ratio = "";

    public ObservableField<String> numText = new ObservableField<>();

    public RebateAgrtCreateModel() {
    }

    public RebateAgrtCreateModel(String minBet, String minPlayer, String ratio) {
        this.minBet = minBet;
        this.minPlayer = minPlayer;
        this.ratio = ratio;
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

}
