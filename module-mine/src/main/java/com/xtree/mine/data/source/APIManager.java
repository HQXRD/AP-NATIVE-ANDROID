package com.xtree.mine.data.source;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: api 静态管理
 */
public class APIManager {

    //LIVE
    public static final String GAMEREBATEAGRT_LIVE_URL = "/compact/userlive/";
    public static final String GAMESUBORDINATEAGRTE_LIVE_URL = "/compact/teamcompactlive/";
    public static final String GAMESUBORDINATEREBATE_LIVE_URL = "/compact/teamsalarylive/";

    //SPORT
    public static final String GAMEREBATEAGRT_SPORT_URL = "/compact/usersport/";
    public static final String GAMESUBORDINATEAGRTE_SPORT_URL = "/compact/teamcompactsport/";
    public static final String GAMESUBORDINATEREBATE_SPORT_URL = "/compact/teamsalarysport/";

    //CHESS
    public static final String GAMEREBATEAGRT_CHESS_URL = "/compact/userxy/";
    public static final String GAMESUBORDINATEAGRTE_CHESS_URL = "/compact/teamcompactxy/";
    public static final String GAMESUBORDINATEREBATE_CHESS_URL = "/compact/teamsalaryxy/";

    //EGAME
    public static final String GAMEREBATEAGRT_EGAME_URL = "/compact/usersportgaming/";
    public static final String GAMESUBORDINATEAGRTE_EGAME_URL = "/compact/teamcompactgaming/";

    //USER
    public static final String GAMEREBATEAGRT_USER_URL = "/compact/user/";
    public static final String GAMESUBORDINATEAGRTE_USER_URL = "/compact/teamcompact/";
    public static final String GAMESUBORDINATEREBATE_USER_URL = "/compact/teamsalary/";

    //分红契约
    public static final String GAMEDIVIDENDAGRT_URL = "/pink/index";
    //分红契约-一键发放
    public static final String GAMEDIVIDENDAGRT_AUTOSEND_URL = "/pink/ajaxhub/countpink";
    //分红契约-规则详情
    public static final String GAMEDIVIDENDAGRT_DETAIL_URL = "/pink/ajaxhub/contract";
}
