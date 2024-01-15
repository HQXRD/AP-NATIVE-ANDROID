package com.xtree.mine.vo;

import java.util.List;

public class BtDetailVo {

    public String project_userid; // "2888826",
    public String project_username; // "testkite1002",
    public String project_Game_date; // "2024-01-13 13:53:53",
    public String project_Game_name; // "跑得快",
    public String project_Game_code; // "7323457665394597766",
    public String project_bet; // "1.8000",
    public String project_win; // 3.51,
    public String project_BetResult; // "W",

    public String project_transaction_date; // "2024-01-12 16:06:57", (多的)
    public String project_TradeAmount; // "0.0000", (多的)
    public String project_advance_settle; // 0, (多的) 是否提前结算 0-否, 1-是

    public String project_WinLostDateTime; // "2024-01-13 13:56:02",
    public String project_Revenue; // "0.0900",
    public String vip; // "VIP1",
    public String venue; // "OBGQP",
    public Object content; // BtContentVo, "德州扑克_云游"

    public static class BtContentVo {
        public String title; // "跑得快",
        public List<BtContentItemVo> list;
    }

    public static class BtContentItemVo {
        public String game_type = ""; // "跑得快(2024-01-13 13:53:53)",
        public String bet_content = ""; // "跑得快  1.8000"

        // 比赛类
        //public String game_type; // "Basketball(2024-01-12 14:00:00)",
        public String match_name = ""; // "Fujian Sturgeons Reserves vs Zhejiang Guangsha Lions Reserves",
        public String competition_name = ""; // "中国CBDL",
        //public String bet_content; // "全场 滚球 0.83",
        public String SportsName = ""; // "Basketball",
        public String EventName = ""; // "Fujian Sturgeons Reserves vs Zhejiang Guangsha Lions Reserves",
        public String team = ""; // "福建浔兴股份预备队 VS 浙江广厦狮后备队",
        public String EventDateTime = ""; // "2024-01-12 14:00:00 +08:00",
        public String Market = ""; // "Live",
        public String BetType = ""; // "让球",
        public String playcontent = ""; // "客队",
        public String Odds = ""; // 0.83
    }

}
