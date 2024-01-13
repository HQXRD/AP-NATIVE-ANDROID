package com.xtree.bet.constant;

public class PMConstants {

    //public static String[] SPORT_NAMES = new String[]{"足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS = new String[]{"40103", "40104", "40106", "40107", "40112", "40110", "40108", "40113", "40109", "40111", "19", "51", "8"};

    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_SPECAIL = new String[]{"40203", "40204", "40206", "40207", "40212", "40210", "40208", "40213", "40209", "40211", "19", "51", "8"};
    /**
     * 默认冠军体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_CHAMPION_SPECAIL = new String[]{"40603", "40604", "40606", "40607", "40612", "40610", "40608", "40613", "40609", "40611", "19", "51", "8"};
    /**
     * 玩法ID，与PLAY_METHOD_NAMES一一对应
     */
    public static String[] PLAY_METHOD_TYPES = new String[]{"3", "1", "4", "11", "100"};

    /**
     * 比分类型-得分
     */
    public static String SCORE_TYPE_SCORE = "S1";
    /**
     * 比分类型-角球
     */
    public static String SCORE_TYPE_CORNER = "S5";
    /**
     * 比分类型-黄牌
     */
    public static String SCORE_TYPE_YELLOW_CARD = "S12";
    /**
     * 比分类型-红牌
     */
    public static String SCORE_TYPE_RED_CARD = "S11";
    /**
     * 比分类型-得牌(黄牌 + 红牌)
     */
    public static String SCORE_TYPE_BOOKING = "9";
    /**
     * 比分类型-盘分(网球、排球、沙滩排球)
     */
    public static String SCORE_TYPE_PF = "5556";
    /**
     * 比分类型-局分(网球、乒乓球、羽毛球)
     */
    public static String SCORE_TYPE_JF = "5559";
    /**
     * 局分(斯诺克)
     */
    public static String SCORE_TYPE_SNK_JF = "12";

}
