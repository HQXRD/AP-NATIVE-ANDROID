package com.xtree.bet.constant;

public class Constants {
    public static String[] SPORT_NAMES = new String[]{"足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
    public static String[] PLAY_METHOD_NAMES = new String[]{"今日", "滚球", "早盘", "串关", "冠军"};
    /**
     * 玩法ID，与PLAY_METHOD_NAMES一一对应
     */
    public static String[] PLAY_METHOD_IDS = new String[]{"6", "1", "4", "2", "7"};
    /**
     * 体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS = new String[]{"1", "3", "5", "16", "7", "13", "47", "6", "15", "2", "19", "51", "8"};
    public static int[] SPORT_ICON = new int[]{
            project.tqyb.com.library_res.R.drawable.bt_match_item_zq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_lq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_wq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_snk_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_ymq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_mszq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_bbq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_iceq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_qj_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_pq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_sq_selector};
}
