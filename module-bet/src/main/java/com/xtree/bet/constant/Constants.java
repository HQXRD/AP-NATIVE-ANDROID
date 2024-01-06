package com.xtree.bet.constant;

public class Constants {

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

    /**
     * 比赛详情页顶部背景图
     */
    public static int[] DETAIL_BG_SPORT_ICON = new int[]{
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_lq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_wq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_snk_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_bq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_pq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_ymq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_bbq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_bq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_default_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_pq_top,
            project.tqyb.com.library_res.R.mipmap.bt_detail_bg_mszq_default_top};
    /**
     * 比分类型-得分
     */
    public static int SCORE_TYPE_SCORE = 5;
    /**
     * 比分类型-角球
     */
    public static int SCORE_TYPE_CORNER = 6;
    /**
     * 比分类型-黄牌
     */
    public static int SCORE_TYPE_YELLOW_CARD = 7;
    /**
     * 比分类型-红牌
     */
    public static int SCORE_TYPE_RED_CARD = 8;
    /**
     * 比分类型-得牌(黄牌 + 红牌)
     */
    public static int SCORE_TYPE_BOOKING = 9;
    /**
     * 比分类型-盘分(网球、排球、沙滩排球)
     */
    public static int SCORE_TYPE_PF = 5556;
    /**
     * 比分类型-局分(网球、乒乓球、羽毛球)
     */
    public static int SCORE_TYPE_JF = 5559;
    /**
     * 局分(斯诺克)
     */
    public static int SCORE_TYPE_SNK_JF = 12;


    /**
     * 足球球法编号
     */
    public static String[] PLAY_TYPE_ID_FB = new String[]{"1005/1001", "1000/1001", "1007/1001", "1005/1002", "1000/1002", "1007/1002"};
    /**
     * 足球球法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_FB = new String[]{"全场独赢", "全场让球", "全场大小", "半场独赢", "半场让球", "半场大小"};

    /**
     * 篮球球法编号
     */
    public static String[] PLAY_TYPE_ID_BSB = new String[]{"3004/3001", "3002/3001", "3003/3001"};
    /**
     * 篮球球法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_BSB = new String[]{"全场独赢", "全场让分", "全场大小"};
    /**
     * 网球球法编号
     */
    public static String[] PLAY_TYPE_ID_WQ = new String[]{"5001/5001", "5002/5001", "5003/5001"};
    /**
     * 网球球法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_WQ = new String[]{"全场独赢", "全场让局", "总局数"};
    /**
     * 斯诺克玩法编号
     */
    public static String[] PLAY_TYPE_ID_SNK = new String[]{"16003/16001", "16001/16001", "16002/16001"};
    /**
     * 斯诺克玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_SNK = new String[]{"全场独赢", "全场让局", "总局数"};
    /**
     * 排球玩法编号
     */
    public static String[] PLAY_TYPE_ID_PQ = new String[]{"13001/13001", "13002/13001", "13003/13001"};
    /**
     * 排球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_PQ = new String[]{"全场独赢", "全场让分", "全场总分"};
    /**
     * 美式足球玩法编号
     */
    public static String[] PLAY_TYPE_ID_MSZQ = new String[]{"6003/6001", "6001/6001", "6002/6001"};
    /**
     * 美式足玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_MSZQ = new String[]{"全场独赢", "全场让球", "全场大小"};
    /**
     * 棒球玩法编号
     */
    public static String[] PLAY_TYPE_ID_BQ = new String[]{"7003/13001", "7001/13001", "7002/13001"};
    /**
     * 棒球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_BQ = new String[]{"全场独赢", "全场让分", "全场大小"};
    /**
     * 羽毛球玩法编号
     */
    public static String[] PLAY_TYPE_ID_YMQ = new String[]{"47001/47001", "47002/47001", "47003/47001"};
    /**
     * 羽毛球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_YMQ = new String[]{"全场独赢", "全场让分", "全场总分"};
    /**
     * 乒乓球玩法编号
     */
    public static String[] PLAY_TYPE_ID_BBQ = new String[]{"15001/15001", "15002/15001", "15003/15001"};
    /**
     * 乒乓球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_BBQ = new String[]{"全场独赢", "全场让分", "全场总分"};
    /**
     * 拳击玩法编号
     */
    public static String[] PLAY_TYPE_ID_QJ = new String[]{"19002/19001", "19004/19001", "19001/19001"};
    /**
     * 拳击玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_QJ = new String[]{"全场独赢", "获胜方式", "全场大小"};
    /**
     * 冰球玩法编号
     */
    public static String[] PLAY_TYPE_ID_ICEQ = new String[]{"2003/2001", "2001/2001", "2002/2001"};
    /**
     * 冰球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_ICEQ = new String[]{"全场独赢", "全场让球", "全场大小"};
    /**
     * 沙滩排球玩法编号
     */
    public static String[] PLAY_TYPE_ID_STPQ = new String[]{"51001/51001", "51002/51001", "51003/51001"};
    /**
     * 沙滩排球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_STPQ = new String[]{"全场独赢", "全场让球", "全场大小"};
    /**
     * 手球玩法编号
     */
    public static String[] PLAY_TYPE_ID_SQ = new String[]{"8008/8001", "8001/8001", "8002/8001"};
    /**
     * 手球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_SQ = new String[]{"全场独赢", "全场让球", "全场大小"};


    public static String[][] PLAY_TYPE_ID = new String[][]{
            PLAY_TYPE_ID_FB,
            PLAY_TYPE_ID_BSB,
            PLAY_TYPE_ID_WQ,
            PLAY_TYPE_ID_SNK,
            PLAY_TYPE_ID_BQ,
            PLAY_TYPE_ID_PQ,
            PLAY_TYPE_ID_YMQ,
            PLAY_TYPE_ID_MSZQ,
            PLAY_TYPE_ID_BBQ,
            PLAY_TYPE_ID_ICEQ,
            PLAY_TYPE_ID_QJ,
            PLAY_TYPE_ID_STPQ,
            PLAY_TYPE_ID_SQ};
    public static String[][] PLAY_TYPE_NAME = new String[][]{
            PLAY_TYPE_NAME_FB,
            PLAY_TYPE_NAME_BSB,
            PLAY_TYPE_NAME_WQ,
            PLAY_TYPE_NAME_SNK,
            PLAY_TYPE_NAME_BQ,
            PLAY_TYPE_NAME_PQ,
            PLAY_TYPE_NAME_YMQ,
            PLAY_TYPE_NAME_MSZQ,
            PLAY_TYPE_NAME_BBQ,
            PLAY_TYPE_NAME_ICEQ,
            PLAY_TYPE_NAME_QJ,
            PLAY_TYPE_NAME_STPQ,
            PLAY_TYPE_NAME_SQ};

}
