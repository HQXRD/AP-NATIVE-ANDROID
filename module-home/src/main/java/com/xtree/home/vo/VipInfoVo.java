package com.xtree.home.vo;

import java.util.List;

public class VipInfoVo {

    //public HashMap<String, Objects> activity_multiplier;
    public String username; // "test032@as"
    public int level; // 0
    public int display_level; // 0
    public String sp; // "0"
    public int current_activity; // 0
    public List<VipUpgradeVo> vip_upgrade;
    //public String vip_details; // 内容是 html

    @Override
    public String toString() {
        return "VipInfoVo{" +
                "username='" + username + '\'' +
                ", level=" + level +
                ", display_level=" + display_level +
                ", sp=" + sp +
                ", current_activity=" + current_activity +
                '}';
    }

    public static class VipUpgradeVo {

        public int level; // 1,
        public int active; // 6000,
        //public int sports_ratio; // 0.38,
        //public int esport_ratio; // 0.45,
        //public int lottery_ratio; // 0,
        //public int chess_ratio; // 0.6,
        //public int rebate_ratio; // 0,
        //public int gaming_ratio; // 0.48,
        //public String upgrade_bonus; // "8.0000",
        //public String withdrawals_limit; // "200000.0000",
        //public int withdrawals_num; // 5,
        //public String virtual_currency_quota; // "0.00",
        public int display_active; // 6000,
        //public int week_red; // 8,
        //public int birthday_gift; // 0,
        //public int relegation_cycle; // 90,
        //public int new_active; // 4000,
        //public int flow; // 1000
    }

}
