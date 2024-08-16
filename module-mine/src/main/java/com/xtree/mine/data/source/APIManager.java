package com.xtree.mine.data.source;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: api 静态管理
 */
public class APIManager {

    //功能菜单
    public static final String FUNCTIONT_MENUS_URL = "/api/account/menus";

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

    //DAY
    public static final String GAMEREBATEAGRT_DAY_URL = "/pink/mybonus/";

    //分红契约
    public static final String GAMEDIVIDENDAGRT_URL = "/pink/index";
    //分红契约-一键发放
    public static final String GAMEDIVIDENDAGRT_AUTOSEND_URL = "/pink/ajaxhub/countpink";
    //分红契约-一键发放-第二步
    public static final String GAMEDIVIDENDAGRT_AUTOSEND_STEP2_URL = "/pink/checkout";
    //分红契约-规则详情
    public static final String GAMEDIVIDENDAGRT_DETAIL_URL = "/pink/ajaxhub/contract";
    //分红契约-手动分发-第一步
    public static final String GAMEDIVIDENDAGRT_SEND_STEP1_URL = "/pink/ajaxhub/countpink";
    //分红契约-手动分发-第二步
    public static final String GAMEDIVIDENDAGRT_SEND_STEP2_URL = "/pink/checkout";
    //佣金报表
    public static final String COMMISSIONS_REPORTS_URL = "/gameinfo/commissionreport";
    //佣金报表查看
    public static final String COMMISSIONS_REPORTS2_URL = "/api/report/getcommission";
    //彩票契约-创建契约
    public static final String DIVIDENDAGRT_CREATE_URL = "/pink/create";
    //彩票契约-修改契约
    public static final String DIVIDENDAGRT_MODIFY_URL = "/pink/update";
}
