package com.xtree.mine.ui.rebateagrt.model;

import com.xtree.mine.R;

import java.util.Arrays;
import java.util.List;

public enum RebateAreegmentTypeEnum {
    //真人
    LIVE("真人返水契约", R.mipmap.icon_rebateagrt_live, new String[]{"344351", "344352", "344354"}),
    //体育
    SPORT("体育返水契约", R.mipmap.icon_rebateagrt_sport, new String[]{"344359", "344361", "344362"}),
    //棋牌
    CHESS("棋牌返水契约", R.mipmap.icon_rebateagrt_chess, new String[]{"401884", "401886", "401887"}),
    //电竞
    EGAME("电竞返水契约", R.mipmap.icon_rebateagrt_game, new String[]{"403228", "403775", "-18"}),
    //时薪
    USER("时薪", R.mipmap.icon_rebateagrt_user, new String[]{"340726", "340725"}),
    DAYREBATE("日分红", R.mipmap.icon_rebateagrt_dayrebate, new String[]{"403796"}),
    //彩票
    LOTTERIES("彩票契约分红", R.mipmap.icon_rebateagrt_lotteries, new String[]{"347883"}),
    //游戏推荐报表
    GAMEREPORTS("游戏推荐报表", R.mipmap.icon_rebateagrt_gamereports, new String[]{"406322"}),
    //彩票推荐报表
    LOTTERIESREPORTS("彩票推荐报表", R.mipmap.icon_rebateagrt_lotteriesreports, new String[]{"406323"}),
    //游戏分红
    GAMEREBATE("游戏分红", R.mipmap.icon_rebateagrt_gamerebate, new String[]{"406320"}),
    //佣金报表
    COMMISSIONSREPORTS("佣金报表", R.mipmap.icon_rebateagrt_commissions, new String[]{"400870"}),
    ;

    private final String name;
    private final int drawable;
    private final List<String> ids;

    RebateAreegmentTypeEnum(String name, int drawable, String[] ids) {
        this.name = name;
        this.drawable = drawable;
        this.ids = Arrays.asList(ids);
    }

    public String getName() {
        return name;
    }

    public int getDrawable() {
        return drawable;
    }

    public List<String> getIds() {
        return ids;
    }
}
