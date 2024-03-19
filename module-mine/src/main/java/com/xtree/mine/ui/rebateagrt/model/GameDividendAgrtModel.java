package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;

import io.reactivex.rxjava3.functions.Consumer;


/**
 * Created by KAKA on 2024/3/14.
 * Describe:
 */
public class GameDividendAgrtModel extends BindModel {

    public String userName;
    public String bet;
    public String netloss;
    public String people;
    public String cycle_percent;
    public String cycle;
    public String subMoney;
    public String userid = "";
    private Consumer<GameDividendAgrtModel> callBack = null;

    public void setCallBack(Consumer<GameDividendAgrtModel> callBack) {
        this.callBack = callBack;
    }

    /**
     * 查看契约
     */
    public void checkArgt() {
        if (callBack != null) {
            try {
                callBack.accept(this);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
