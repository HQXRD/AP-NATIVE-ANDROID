package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindHead;
import com.xtree.base.mvvm.recyclerview.BindModel;

/**
 * Created by KAKA on 2024/3/11.
 * Describe: 游戏场馆返水列表数据
 */
public class GameRebateAgrtModel extends BindModel{

    //日投注额
    public String betAmoutDay;
    //有效投注额
    public String betAmoutValidity;
    //返水比例
    public String rebatePercentage;
    //返水总额
    public String rebateAmout;
    //下级工资
    public String subMoney;
    //自身工资
    public String mineMoney;
    //日期
    public String date;
    //状态
    public String status;

    public void setStatus(String status) {
        switch (status) {
            case "0":
                status =  "已到账";
                break;
            case "1":
                status =  "已到账";
                break;
            case "2":
                status =  "已到账";
                break;
            default:
                status =  "已到账";
                break;
        }
    }
}
