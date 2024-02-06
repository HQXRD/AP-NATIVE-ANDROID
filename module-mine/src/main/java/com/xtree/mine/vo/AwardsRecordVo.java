package com.xtree.mine.vo;

import java.util.ArrayList;

/** 流水*/
public class AwardsRecordVo {
    public String locked_award_sum;
    public String withdraw_dispensing_money ;
    public ArrayList<AwardsRecordInfo> list ;

    public boolean isShow ; //false 不展示资金流水 true 展示资金流水
    public static class AwardsRecordInfo
    {
        @Override
        public String toString() {
            return "AwardsRecordInfo{" +
                    "id='" + id + '\'' +
                    ", aid='" + aid + '\'' +
                    ", userid='" + userid + '\'' +
                    ", dispensing_money='" + dispensing_money + '\'' +
                    ", deducted_turnover='" + deducted_turnover + '\'' +
                    ", money='" + money + '\'' +
                    ", is_bet_source='" + is_bet_source + '\'' +
                    ", c_time='" + c_time + '\'' +
                    ", title='" + title + '\'' +
                    ", origin_money='" + origin_money + '\'' +
                    ", dispensing_money_left='" + dispensing_money_left + '\'' +
                    ", bet_source_trans='" + bet_source_trans + '\'' +
                    '}';
        }

        public  String id ;
        public  String aid ;
        public  String userid ;
        public  String dispensing_money ;

        public  String deducted_turnover ;
        public  String money ;
        public  String is_bet_source ;
        public  String c_time ;

        public  String title ;//活动名称
        public  String origin_money ;//需求流水

        public  String dispensing_money_left ;//剩余流水
        public  String  bet_source_trans ;//显示的具体文案
        /*"id":"26051",
                "aid":"168",
                "userid":"5228132",
                "dispensing_money":"50.00",
                "deducted_turnover":"4.00",
                "money":"50.00",
                "is_bet_source":"1",
                "c_time":"2024-01-03 16:57:40",
                "title":"每日累计存勿动",
                "origin_money":"50.0000",
                "dispensing_money_left":"46.00",
                "bet_source_trans":*/
    }

}