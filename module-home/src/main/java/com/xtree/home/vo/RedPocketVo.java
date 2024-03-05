package com.xtree.home.vo;

/**
 * 红包金额
 */
public class RedPocketVo {
    public boolean assign; // true
    public String award_id; // 27376
    public String aid; // 102
    public String money = ""; // 110
    public String award_cycle; // 2024-02-25 00:00:00
    public String expire_time; // 2024-03-09
    public int status; // 0 or -1
    public String expired; // false

    @Override
    public String toString() {
        return "RedPocketVo{" +
                "assign=" + assign +
                ", award_id='" + award_id + '\'' +
                ", aid='" + aid + '\'' +
                ", money='" + money + '\'' +
                ", award_cycle='" + award_cycle + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", status=" + status +
                ", expired='" + expired + '\'' +
                '}';
    }
}
