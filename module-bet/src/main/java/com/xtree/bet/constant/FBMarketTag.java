package com.xtree.bet.constant;

import android.util.ArrayMap;

import java.util.Map;

/**
 * 玩法展示分类
 */
public class FBMarketTag {
    private int code;
    private String desc;

    private static Map<String, String> map = new ArrayMap<>();

    public static String getMarketTag(String code){
        if(map.isEmpty()){
            // 足球
            map.put("all", "全部");
            map.put("p", "热门");
            map.put("h", "让球&大/小");
            map.put("s", "进球");
            map.put("f", "半场");
            map.put("c", "角球");
            map.put("i", "特殊投注");
            map.put("cs", "波胆");
            map.put("b", "罚牌");
            map.put("o", "其它");
            map.put("q", "节");
            map.put("t", "15分钟");
            map.put("j", "赛局");
            map.put("set", "赛盘");
            map.put("qu", "前二组合");
            map.put("z", "准确前二");
            map.put("ps", "点球大战");
            map.put("pro", "晋级球队");
            map.put("1st", "赢得冠军");
            map.put("3rd", "赢得季军");
            map.put("pa", "球员玩法");
        }
        return map.get(code);
    }
}
