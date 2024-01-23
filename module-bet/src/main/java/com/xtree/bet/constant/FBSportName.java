package com.xtree.bet.constant;

import android.util.ArrayMap;

import java.util.Map;

/**
 * 赛种名称
 */
public class FBSportName {
    private static Map<String, String> map = new ArrayMap<>();

    public static String getSportName(String code){
        if(map.isEmpty()){
            // 足球
            map.put("1", "足球");
            map.put("3", "篮球");
            map.put("5", "网球");
            map.put("16", "斯诺克");
            map.put("7", "棒球");
            map.put("13", "排球");
            map.put("47", "羽毛球");
            map.put("6", "美式足球");
            map.put("15", "乒乓球");
            map.put("2", "拳击");
            map.put("51", "沙滩排球");
            map.put("8", "手球");
        }
        return map.get(code);
    }
}
