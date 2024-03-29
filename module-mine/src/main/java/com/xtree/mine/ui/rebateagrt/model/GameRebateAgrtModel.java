package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.mine.R;

import me.xtree.mvvmhabit.base.BaseApplication;

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
    private String status;
    private String statusString = BaseApplication.getInstance().getString(R.string.txt_unreceived);;
    private int statusColor = R.color.clr_txt_rebateagrt_fail;;

    public void setStatus(String status) {
        if (status != null && !status.isEmpty()) {
            this.status = status;
            switch (status) {
                case "1":
                    statusString = BaseApplication.getInstance().getString(R.string.txt_received);
                    statusColor = R.color.clr_txt_rebateagrt_success;
                    break;
                case "2":
                    statusString = BaseApplication.getInstance().getString(R.string.txt_unreceived);
                    statusColor = R.color.clr_txt_rebateagrt_fail;
                    break;
            }
        }
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public int getStatusColor() {
        return statusColor;
    }
}