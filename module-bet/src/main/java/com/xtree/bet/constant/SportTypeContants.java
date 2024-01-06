package com.xtree.bet.constant;

/**
 * 体育分类
 */
public class SportTypeContants {
    public static String[] SPORT_NAMES = new String[]{"足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
    /**
     * 体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS = new String[]{"1", "3", "5", "16", "7", "13", "47", "6", "15", "2", "19", "51", "8"};

    /**
     * 体育分类ID-足球
     */
    public final static String SPORT_ID_FB = "1";
    /**
     * 体育分类ID-篮球
     */
    public static String SPORT_ID_BSB = "3";
    /**
     * 体育分类ID-网球
     */
    public static String SPORT_ID_WQ = "5";
    /**
     * 体育分类ID-斯诺克
     */
    public static String SPORT_ID_SNK = "16";
    /**
     * 体育分类ID-棒球
     */
    public static String SPORT_ID_BQ = "7";
    /**
     * 体育分类ID-排球
     */
    public static String SPORT_ID_PQ = "13";
    /**
     * 体育分类ID-羽毛球
     */
    public static String SPORT_ID_YMQ = "47";
    /**
     * 体育分类ID-美式足球
     */
    public static String SPORT_ID_MSZQ = "6";
    /**
     * 体育分类ID-乒乓球
     */
    public static String SPORT_ID_BBQ = "15";
    /**
     * 体育分类ID-冰球
     */
    public static String SPORT_ID_ICEQ = "2";
    /**
     * 体育分类ID-拳击
     */
    public static String SPORT_ID_QJ = "19";
    /**
     * 体育分类ID-沙滩排球
     */
    public static String SPORT_ID_STPQ = "51";
    /**
     * 体育分类ID-手球
     */
    public static String SPORT_ID_SQ = "8";

    public static String getSportId(int sportPos){
        return SPORT_IDS[sportPos];
    }
}
