package com.xtree.bet.constant;

public class PMConstants {

    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS;/*{"0", "40103", "40104", "40106", "40107", "40112", "40110", "40108", "40113", "40109", "40111", "19", "51", "8"}*/;

    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_SPECAIL = new String[]{"0", "40203", "40204", "40206", "40207", "40212", "40210", "40208", "40213", "40209", "40211", "19", "51", "8"};
    /**
     * 默认冠军体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_CHAMPION_SPECAIL = new String[]{"40603", "40604", "40606", "40607", "40612", "40610", "40608", "40613", "40609", "40611", "19", "51", "8"};
    /**
     * 玩法ID，与PLAY_METHOD_NAMES一一对应
     */
    public static String[] PLAY_METHOD_TYPES = new String[]{"3", "1", "4", "11", "100"};


    /**
     * 体育分类ID-足球
     */
    public final static String SPORT_ID_FB = "1";
    /**
     * 体育分类ID-篮球
     */
    public static String SPORT_ID_BSB = "2";
    /**
     * 体育分类ID-网球
     */
    public static String SPORT_ID_WQ = "5";
    /**
     * 体育分类ID-斯诺克
     */
    public static String SPORT_ID_SNK = "7";
    /**
     * 体育分类ID-棒球
     */
    public static String SPORT_ID_BQ = "3";
    /**
     * 体育分类ID-排球
     */
    public static String SPORT_ID_PQ = "9";
    /**
     * 体育分类ID-羽毛球
     */
    public static String SPORT_ID_YMQ = "10";
    /**
     * 体育分类ID-美式足球
     */
    public static String SPORT_ID_MSZQ = "6";
    /**
     * 体育分类ID-乒乓球
     */
    public static String SPORT_ID_BBQ = "8";
    /**
     * 体育分类ID-冰球
     */
    public static String SPORT_ID_ICEQ = "4";
    /**
     * 体育分类ID-拳击
     */
    public static String SPORT_ID_QJ = "12";
    /**
     * 体育分类ID-沙滩排球
     */
    public static String SPORT_ID_STPQ = "39";
    /**
     * 体育分类ID-手球
     */
    public static String SPORT_ID_SQ = "11";


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
