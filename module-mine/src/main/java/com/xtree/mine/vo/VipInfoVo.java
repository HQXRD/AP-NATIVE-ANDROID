package com.xtree.mine.vo;

public class VipInfoVo {

    //public HashMap<String, Objects> activity_multiplier;
    public String username;
    public int level;
    public int display_level;
    public int sp;
    public int current_activity;
    //public List<HashMap<String, Objects>> vip_upgrade;
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
}
