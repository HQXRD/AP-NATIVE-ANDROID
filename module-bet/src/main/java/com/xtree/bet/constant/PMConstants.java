package com.xtree.bet.constant;

import android.util.ArrayMap;

import com.google.gson.Gson;
import com.xtree.bet.R;
import com.xtree.bet.bean.response.fb.HotLeague;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PMConstants {
    private static Map<String, Integer> mapBgMatchDetailTop = new ArrayMap<>();

    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS;

    /**
     * 默认体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_DEFAULT = new String[]{"0", "40203", "40204", "40206", "40207", "40212", "40210", "40208", "40213", "40209", "40211", "40216", "40211", "40215"};
    /**
     * 默认冠军体育分类ID，与sportNames一一对应
     */
    public static String[] SPORT_IDS_CHAMPION_SPECAIL = new String[]{"40603", "40604", "40606", "40607", "40612", "40610", "40608", "40613", "40609", "40611", "40216", "40211", "40215"};
    public static Integer[] SPORT_TYPES_ADDITIONAL = new Integer[]{22, 52, 20037, 20038, 24, 51, 20033, 20018};

    public static String[] SPORT_NAMES;
    public static String[] SPORT_NAMES_NOMAL = new String[]{"足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击/格斗", "沙滩排球", "手球"};
    public static String[] SPORT_NAMES_LIVE = new String[]{"全部", "足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击/格斗", "沙滩排球", "手球"};
    public static String[] SPORT_NAMES_TODAY_CG = new String[]{"热门", "足球", "篮球", "网球", "斯诺克", "棒球", "排球", "羽毛球", "美式足球", "乒乓球", "冰球", "拳击/格斗", "沙滩排球", "手球"};

    public static int[] SPORT_ICON_ADDITIONAL = new int[]{
            project.tqyb.com.library_res.R.drawable.bt_match_item_glq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_golf_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_wbq_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_darts_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_water_ball_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_cycling_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_f1_selector,
            project.tqyb.com.library_res.R.drawable.bt_match_item_yl_selector};

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

    private static List<HotLeague> hotFootBallLeagueTopList = new ArrayList<>();
    private static List<HotLeague> hotBasketBallLeagueTopList = new ArrayList<>();
    private static Map<String, Integer> mapHotLeagueIconTop = new ArrayMap<>();

    public static int getHotLeagueIcon(String code){
        if(mapHotLeagueIconTop.isEmpty()){
            mapHotLeagueIconTop.put("allleague", R.drawable.bt_hot_league_item_epl_selector);
            mapHotLeagueIconTop.put("EURO", R.drawable.bt_hot_league_item_euro_selector);
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
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [], code: \"allleague\", name: \"全部\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [33163,1196925222286433163], code: \"EURO\", name: \"欧洲杯\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [2,6408,352541568130764801], code: \"UCL\", name: \"欧冠\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [3,180,29461,1682748461414224369], code: \"EPL\", name: \"英超\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [4,239], code: \"SEA\", name: \"意甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [5,320,1682748470622372141], code: \"LaLiga\", name: \"西甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [6,32230,276,29569], code: \"BVB\", name: \"德甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [7,79,343264595247255555], code: \"Ligue1\", name: \"法甲\"}", HotLeague.class));
            hotFootBallLeagueTopList.add(gson.fromJson("{ leagueid: [8,6344,1682748478869187623], code: \"CSL\", name: \"中超\"}", HotLeague.class));
        }
        return hotFootBallLeagueTopList;
    }

    public static List<HotLeague> getHotBasketFootBallLeagueTopList(){
        if(hotBasketBallLeagueTopList.isEmpty()){
            Gson gson = new Gson();
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [], code: \"allleague\", name: \"全部\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [132], code: \"NBA\", name: \"NBA\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [427], code: \"NBL\", name: \"澳洲NBL\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [146], code: \"CBA\", name: \"CBA\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [48], code: \"JB1\", name: \"日本B1\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [208], code: \"KBL\", name: \"韩国KBL\"}", HotLeague.class));
            hotBasketBallLeagueTopList.add(gson.fromJson("{ leagueid: [122], code: \"EL\", name: \"欧洲EL\"}", HotLeague.class));
        }
        return hotBasketBallLeagueTopList;
    }

}
