package com.xtree.mine.vo;
/**
 *团队人数活动报表
 */

public class TeamActivityReportVo {
    /*
    *{"status":10000,"message":"success",
    * "data":
    * [{"status":"0","condition":"1","money":"10.0000","created_at":"2024-06-24 16:17:22","month":"2024-07"}],"timestamp":1723459515}
     *
     */
    public String status; // "状态 0 违发送",
    public String condition; // 活跃人数
    public String money; // 应发经理（RMB单位）
    public String created_at; //发奖日期
    public String month; // 月份呢


}
