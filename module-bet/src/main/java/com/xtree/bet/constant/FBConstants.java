package com.xtree.bet.constant;

import android.util.ArrayMap;

import com.google.gson.Gson;
import com.xtree.bet.R;
import com.xtree.bet.bean.response.fb.HotLeague;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FBConstants {

    /**
     * 体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS;
    public static String[] SPORT_IDS_ALL = new String[]{"0", "1", "3", "5", "16", "7", "13", "47", "6", "15", "2", "19", "51", "8"};
    public static String[] SPORT_IDS_NOMAL = new String[]{"1", "3", "5", "16", "7", "13", "47", "6", "15", "2", "19", "51", "8"};
    public static String[] SPORT_IDS_ADDITIONAL = new String[]{"4", "10", "12", "14", "17", "18", "20", "21", "24", "25", "92", "93", "100"};

    public static String[] SPORT_NAMES;
    public static String[] SPORT_NAMES_NOMAL = new String[]{"足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
    public static String[] SPORT_NAMES_LIVE = new String[]{"全部", "足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
    public static String[] SPORT_NAMES_TODAY_CG = new String[]{"热门", "足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击", "沙滩排球", "手球"};
    public static String[] SPORT_NAMES_ADDITIONAL = new String[]{"橄榄球", "地板球", "高尔夫球", "玩板球", "五人足球", "综合", "飞镖", "草地滚球", "水球", "自行车", "F1赛车", "特殊投注", "奥林匹克"};

    public static int[] SPORT_ICON_ADDITIONAL = new int[]{
            project.tqyb.com.library_res.R.drawable.bt_match_item_glq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_dbq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_golf_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_wbq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_wrzq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_zhgd_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_darts_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_cdgq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_water_ball_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_cycling_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_f1_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_tstz_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_olympic_selector};

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
    /**
     * 玩法ID，与PLAY_METHOD_NAMES一一对应
     */
    public static String[] PLAY_METHOD_TYPES = new String[]{"6", "1", "4", "2", "7"};

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
    public static String[] PLAY_TYPE_ID_BQ = new String[]{"7003/7001", "7001/7001", "7002/7001"};
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
    public static String[] PLAY_TYPE_NAME_QJ = new String[]{"全场独赢", "获胜方式", "回合数大小"};
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
    public static String[] PLAY_TYPE_ID_SQ = new String[]{"8005/8001", "8001/8001", "8002/8001"};
    /**
     * 手球玩法名称，跟上面的编号一一对应
     */
    public static String[] PLAY_TYPE_NAME_SQ = new String[]{"全场独赢", "全场让球", "全场大小"};

    private static Map<String, String[]> mapPlayTypeId = new ArrayMap<>();
    private static Map<String, String[]> mapPlayTypeName = new ArrayMap<>();
    private static Map<String, Integer> mapBgMatchDetailTop = new ArrayMap<>();
    private static List<HotLeague> hotFootBallLeagueTopList = new ArrayList<>();
    private static List<HotLeague> hotBasketBallLeagueTopList = new ArrayList<>();
    private static Map<String, Integer> mapHotLeagueIconTop = new ArrayMap<>();

    public static int getHotLeagueIcon(String code){
        if(mapHotLeagueIconTop.isEmpty()){
            mapHotLeagueIconTop.put("allleague", R.drawable.bt_hot_league_item_epl_selector);
            mapHotLeagueIconTop.put("UCL", R.drawable.bt_hot_league_item_ucl_selector);
            mapHotLeagueIconTop.put("EPL", R.drawable.bt_hot_league_item_epl_selector);
            mapHotLeagueIconTop.put("SEA", R.drawable.bt_hot_league_item_sea_selector);
            mapHotLeagueIconTop.put("LaLiga", R.drawable.bt_hot_league_item_laliga_selector);
            mapHotLeagueIconTop.put("BVB", R.drawable.bt_hot_league_item_bvb_selector);
            mapHotLeagueIconTop.put("Ligue1", R.drawable.bt_hot_league_item_ligue1_selector);
            mapHotLeagueIconTop.put("CSL", R.drawable.bt_hot_league_item_csl_selector);
            mapHotLeagueIconTop.put("NBA", R.drawable.bt_hot_league_item_nba_selector);
            mapHotLeagueIconTop.put("NBL", R.drawable.bt_hot_league_item_nbl_selector);
            mapHotLeagueIconTop.put("CBA", R.drawable.bt_hot_league_item_cba_selector);
            mapHotLeagueIconTop.put("JB1", R.drawable.bt_hot_league_item_jb1_selector);
            mapHotLeagueIconTop.put("KBL", R.drawable.bt_hot_league_item_kbl_selector);
            mapHotLeagueIconTop.put("EL", R.drawable.bt_hot_league_item_el_selector);
        }
        return mapHotLeagueIconTop.get(code);
    }

    public static List<HotLeague> getHotFootBallLeagueTopList(){
        if(hotFootBallLeagueTopList.isEmpty()){
            Gson gson = new Gson();
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [], code: \"allleague\", name:\"全部\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [2,11140], code: \"UCL\", name: \"欧冠\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [3,11062,11264], code: \"EPL\", name: \"英超\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [4,11018], code: \"SEA\", name: \"意甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [5,10815], code: \"LaLiga\", name: \"西甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [6,10807], code: \"BVB\", name: \"德甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [7,10983], code: \"Ligue1\", name: \"法甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [8], code: \"CSL\", name: \"中超\"}", HotLeague.class));
        }
        return hotFootBallLeagueTopList;
    }

    public static List<HotLeague> getHotBasketFootBallLeagueTopList(){
        if(hotBasketBallLeagueTopList.isEmpty()){
            Gson gson = new Gson();
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [], code: \"allleague\", name: \"全部\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [11274], code: \"NBA\", name: \"NBA\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [11898], code: \"NBL\", name: \"澳洲NBL\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [11276], code: \"CBA\", name: \"CBA\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [11863], code: \"JB1\", name: \"日本B1\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [16250], code: \"KBL\", name: \"韩国KBL\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [11278], code: \"EL\", name: \"欧洲EL\"}", HotLeague.class));
        }
        return hotBasketBallLeagueTopList;
    }

    public static int getBgMatchDetailTop(String sportId){
        if(mapBgMatchDetailTop.isEmpty()){
            mapBgMatchDetailTop.put(SPORT_ID_FB, R.mipmap.bt_detail_bg_zq_top);
            mapBgMatchDetailTop.put(SPORT_ID_BSB, R.mipmap.bt_detail_bg_lq_top);
            mapBgMatchDetailTop.put(SPORT_ID_WQ, R.mipmap.bt_detail_bg_wq_top);
            mapBgMatchDetailTop.put(SPORT_ID_SNK, R.mipmap.bt_detail_bg_snk_top);
            mapBgMatchDetailTop.put(SPORT_ID_BQ, R.mipmap.bt_detail_bg_bq_top);
            mapBgMatchDetailTop.put(SPORT_ID_PQ, R.mipmap.bt_detail_bg_pq_top);
            mapBgMatchDetailTop.put(SPORT_ID_YMQ, R.mipmap.bt_detail_bg_ymq_top);
            mapBgMatchDetailTop.put(SPORT_ID_MSZQ, R.mipmap.bt_detail_bg_mszq_top);
            mapBgMatchDetailTop.put(SPORT_ID_ICEQ, R.mipmap.bt_detail_bg_bnq_top);
            mapBgMatchDetailTop.put(SPORT_ID_BBQ, R.mipmap.bt_detail_bg_bbq_top);
            mapBgMatchDetailTop.put(SPORT_ID_QJ, R.mipmap.bt_detail_bg_mszq_default_top);
            mapBgMatchDetailTop.put(SPORT_ID_STPQ, R.mipmap.bt_detail_bg_pq_top);
            mapBgMatchDetailTop.put(SPORT_ID_SQ, R.mipmap.bt_detail_bg_mszq_default_top);
        }
        return mapBgMatchDetailTop.get(sportId);
    }

    public static String[] getPlayTypeId(String sportId){
        if(mapPlayTypeId.isEmpty()){
            mapPlayTypeId.put(SPORT_ID_FB, PLAY_TYPE_ID_FB);
            mapPlayTypeId.put(SPORT_ID_BSB, PLAY_TYPE_ID_BSB);
            mapPlayTypeId.put(SPORT_ID_WQ, PLAY_TYPE_ID_WQ);
            mapPlayTypeId.put(SPORT_ID_SNK, PLAY_TYPE_ID_SNK);
            mapPlayTypeId.put(SPORT_ID_BQ, PLAY_TYPE_ID_BQ);
            mapPlayTypeId.put(SPORT_ID_PQ, PLAY_TYPE_ID_PQ);
            mapPlayTypeId.put(SPORT_ID_YMQ, PLAY_TYPE_ID_YMQ);
            mapPlayTypeId.put(SPORT_ID_MSZQ, PLAY_TYPE_ID_MSZQ);
            mapPlayTypeId.put(SPORT_ID_ICEQ, PLAY_TYPE_ID_ICEQ);
            mapPlayTypeId.put(SPORT_ID_BBQ, PLAY_TYPE_ID_BBQ);
            mapPlayTypeId.put(SPORT_ID_QJ, PLAY_TYPE_ID_QJ);
            mapPlayTypeId.put(SPORT_ID_STPQ, PLAY_TYPE_ID_STPQ);
            mapPlayTypeId.put(SPORT_ID_SQ, PLAY_TYPE_ID_SQ);
        }
        return mapPlayTypeId.get(sportId);
    }

    public static String[] getPlayTypeName(String sportId){
        if(mapPlayTypeName.isEmpty()){
            mapPlayTypeName.put(SPORT_ID_FB, PLAY_TYPE_NAME_FB);
            mapPlayTypeName.put(SPORT_ID_BSB, PLAY_TYPE_NAME_BSB);
            mapPlayTypeName.put(SPORT_ID_WQ, PLAY_TYPE_NAME_WQ);
            mapPlayTypeName.put(SPORT_ID_SNK, PLAY_TYPE_NAME_SNK);
            mapPlayTypeName.put(SPORT_ID_BQ, PLAY_TYPE_NAME_BQ);
            mapPlayTypeName.put(SPORT_ID_PQ, PLAY_TYPE_NAME_PQ);
            mapPlayTypeName.put(SPORT_ID_YMQ, PLAY_TYPE_NAME_YMQ);
            mapPlayTypeName.put(SPORT_ID_MSZQ, PLAY_TYPE_NAME_MSZQ);
            mapPlayTypeName.put(SPORT_ID_ICEQ, PLAY_TYPE_NAME_ICEQ);
            mapPlayTypeName.put(SPORT_ID_BBQ, PLAY_TYPE_NAME_BBQ);
            mapPlayTypeName.put(SPORT_ID_QJ, PLAY_TYPE_NAME_QJ);
            mapPlayTypeName.put(SPORT_ID_STPQ, PLAY_TYPE_NAME_STPQ);
            mapPlayTypeName.put(SPORT_ID_SQ, PLAY_TYPE_NAME_SQ);
        }
        return mapPlayTypeName.get(sportId);
    }

}
