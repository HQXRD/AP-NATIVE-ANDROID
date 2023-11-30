package com.xtree.bet.constant;

import android.util.ArrayMap;

import java.util.Map;
import java.util.SimpleTimeZone;

public class MatchPeriod {
    private int code;
    private String desc;

    private static Map<String, String> map = new ArrayMap<>();

    // todo 继续补充所有的 see enum: match_period
    public static String getMatchPeriod(String code){
        if(map.isEmpty()){
            // 足球
            map.put("1001", "比赛未开始");
            map.put("1002", "上半场");
            map.put("1003", "中场休息");
            map.put("1004", "下半场");
            map.put("1005", "常规时间结束");
            map.put("1006", "加时赛上半场");
            map.put("1007", "加时赛半场时间");
            map.put("1008", "加时赛下半场");
            map.put("1009", "加时赛结束");
            map.put("1010", "点球大战");
            map.put("1011", "比赛结束");
            map.put("1012", "等待加时");
            map.put("1013", "等待点球大战");
            map.put("1014", "点球大战结束");
            map.put("1015", "中断");
            map.put("1016", "腰斩");

            // 篮球
            map.put("3001", "未开始");
            map.put("3002", "上半场");
            map.put("3003", "中场休息");
            map.put("3004", "下半场");
            map.put("3005", "第一节");
            map.put("3006", "中场休息 1/2");
            map.put("3007", "第二节");
            map.put("3008", "中场休息 2/3");
            map.put("3009", "第三节");
            map.put("3010", "中场休息 3/4");
            map.put("3011", "第四节");
            map.put("3012", "加时");
            map.put("3013", "常规时间结束");
            map.put("3014", "比赛结束");
            map.put("3015", "等待加时");
            map.put("3016", "加时赛结束");
            map.put("3017", "延期");
            map.put("3018", "中断");
            map.put("3019", "废弃");

            // 排球
            map.put("13001", "未开始");
            map.put("13002", "第1局");
            map.put("13003", "第1局结束");
            map.put("13004", "第2局");
            map.put("13005", "第2局结束");
            map.put("13006", "第3局");
            map.put("13007", "第3局结束");
            map.put("13008", "第4局");
            map.put("13009", "第4局结束");
            map.put("13010", "第5局");
            map.put("13011", "第5局结束");
            map.put("13012", "第6局");
            map.put("13013", "第6局结束");
            map.put("13014", "第7局");
            map.put("13015", "比赛结束");
            map.put("13016", "等待金局");
            map.put("13017", "金局");
            map.put("13018", "金局结束");
            map.put("13019", "散步结束，选手1胜");
            map.put("13020", "散步结束，选手2胜");
            map.put("13021", "选手1退赛，选手2胜");
            map.put("13022", "选手2退赛，选手1胜");
            map.put("13023", "延期");
            map.put("13024", "中断");
            map.put("13025", "废弃");
        }
        return map.get(code);
    }
}
