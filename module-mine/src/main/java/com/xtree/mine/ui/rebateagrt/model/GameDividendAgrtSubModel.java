package com.xtree.mine.ui.rebateagrt.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.widget.FilterView;
import com.xtree.mine.vo.StatusVo;

import java.util.List;

/**
 * Created by KAKA on 2024/3/14.
 * Describe:
 */
public class GameDividendAgrtSubModel extends BindModel {

    public interface OnCallBack {
        void autoSend();
        void send();
    }

    private String payoff;
    private String owe;

    private OnCallBack onCallBack;

    public GameDividendAgrtSubModel() {
    }

    public GameDividendAgrtSubModel(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public String getPayoff() {
        return payoff;
    }

    public void setPayoff(String payoff) {
        this.payoff = payoff;
    }

    public String getOwe() {
        return owe;
    }

    public void setOwe(String owe) {
        this.owe = owe;
    }


    /**
     * 一键发放
     */
    public void autoSend() {
        if (onCallBack != null) {
            onCallBack.autoSend();
        }
    }

    /**
     * 手动发放
     */
    public void send() {
        if (onCallBack != null) {
            onCallBack.send();
        }
    }

}
