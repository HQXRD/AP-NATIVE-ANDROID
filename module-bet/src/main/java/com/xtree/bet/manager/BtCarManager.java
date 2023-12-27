package com.xtree.bet.manager;

import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.contract.BetContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;

public class BtCarManager {
    private static boolean isCg; //是否串关
    private static List<BetConfirmOption> btCarList = new ArrayList<>();

    private static Map<String, BetConfirmOption> btCarMap = new HashMap<>();

    public static List<BetConfirmOption> getBtCarList() {
        return btCarList;
    }

    public static void addBtCar(BetConfirmOption betConfirmOption){
        for(String key : btCarMap.keySet()){
            if(key.startsWith(String.valueOf(betConfirmOption.getMatch().getId()))){
                btCarList.remove(btCarMap.get(key));
                btCarMap.remove(key);
                break;
            }
        }
        btCarList.add(betConfirmOption);
        btCarMap.put(betConfirmOption.getCode(), betConfirmOption);
        RxBus.getDefault().post(new BetContract(BetContract.ACTION_BTCAR_CHANGE));
    }

    public static void removeBtCar(BetConfirmOption betConfirmOption){
        BetConfirmOption confirmOption = btCarMap.get(betConfirmOption.getCode());
        if(confirmOption != null) {
            btCarList.remove(confirmOption);
            btCarMap.remove(betConfirmOption.getCode());
        }
        RxBus.getDefault().post(new BetContract(BetContract.ACTION_BTCAR_CHANGE));
    }

    public static int size() {
        return btCarList.size();
    }

    public static boolean isEmpty(){
        return btCarList.isEmpty();
    }

    public static boolean has(BetConfirmOption betConfirmOption){
        return btCarMap.get(betConfirmOption.getCode()) != null;
    }

    public static void clearBtCar(){
        btCarList.clear();
        btCarMap.clear();
        RxBus.getDefault().post(new BetContract(BetContract.ACTION_BTCAR_CHANGE));
    }

    /**
     * 是否串关
     * @return
     */
    public static boolean isCg(){
        return isCg;
    }

    public static void setIsCg(boolean cg){
        isCg = cg;
    }

    public static void destroy(){
        clearBtCar();
        setIsCg(false);
    }
}
