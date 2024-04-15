package com.xtree.base.global;

/**
 * 全局SharedPreferences Key 统一存放在这里，单个组件中的key可以另外在组件中定义
 * Created by goldze on 2018/6/21 0021.
 */
public class SPKeyGlobal {
    public static final String USER_INFO = "user_info";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String PUBLIC_KEY = "public_key";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_TOKEN_TYPE = "user_token_type";
    public static final String USER_SHARE_SESSID = "user_sessid";
    public static final String USER_SHARE_COOKIE_NAME = "user_cookie_name";
    public static final String USER_AUTO_THRAD_STATUS = "auto_thrad_status";
    public static final String NET_WORK_BASE_URL = "net_work_base_url";

    public static final String HOME_BANNER_LIST = "home_banner_list";
    public static final String HOME_NOTICE_LIST = "home_notice_list";
    public static final String HOME_GAME_LIST = "home_game_list";
    public static final String HOME_PROFILE = "home_profile";
    public static final String HOME_VIP_INFO = "home_vip_info";
    public static final String DC_NEWS_LIST = "dc_news_list";
    public static final String RC_PAYMENT_OBJ = "rc_payment_obj";
    public static final String RC_PAYMENT_TYPE_OBJ = "rc_payment_type_obj";
    public static final String RC_NOT_TIP_TODAY_COUNT = "rc_not_tip_today_count"; // 充值 今日不再提示 次数
    public static final String RC_NOT_TIP_TODAY_LOW = "rc_not_tip_today_low"; // 充值 今日不再提示 低成功率
    public static final String PM_NOT_TIP_TODAY = "pm_not_tip_today"; // 熊猫体育 今日不再提示
    public static final String AG_NOT_TIP_TODAY = "ag_not_tip_today"; // AG真人 今日不再提示
    public static final String DB_NOT_TIP_TODAY = "db_not_tip_today"; // DB真人 今日不再提示
    public static final String RC_PAYMENT_THIRIFRAME = "rc_payment_thiriframe";
    public static final String WLT_CENTRAL_BLC = "wlt_central_blc";
    public static final String WLT_GAME_ROOM_BLC = "wlt_game_room_blc";
    public static final String FBXC_TOKEN = "fbxc_token";
    public static final String FB_TOKEN = "fb_token";
    public static final String PM_TOKEN = "pm_token";
    public static final String FB_API_SERVICE_URL = "fb_api_service_url";
    public static final String FBXC_API_SERVICE_URL = "fbxc_api_service_url";
    public static final String PM_API_SERVICE_URL = "pm_api_service_url";
    public static final String PM_IMG_SERVICE_URL = "PM_IMG_SERVICE_URL";
    public static final String PM_USER_ID = "PM_USER_ID";
    public static final String MSG_INFO = "msg_info";
    public static final String MSG_PERSON_INFO = "msg_person_info";
    public static final String VIP_INFO = "vip_info";
    public static final String QUESTION_WEB = "questions_web";
    public static final String AUG_LIST = "aug_list";

    public static final String APP_INTERVAL_TIME = "app_interval_time";//App请求更新时间初始时间
    public static final String APP_LAST_CHECK_TIME = "app_last_check_time";//App请求更新时间
}
