package com.xtree.base.router;

/**
 * 用于组件开发中，ARouter多Fragment跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 * Created by goldze on 2018/6/21
 */

public class RouterFragmentPath {
    /**
     * 首页组件
     */
    public static class Home {
        private static final String HOME = "/home";
        /*首页*/
        public static final String PAGER_HOME = HOME + "/Home";

    }

    public static class Activity {

        private static final String ACTIVITY = "/activity";
        public static final String PAGER_ACTIVITY = ACTIVITY + "/Activity";
    }

    public static class Mine {
        private static final String MINE = "/mine";
        public static final String PAGER_MINE = MINE + "/Mine";
        public static final String PAGER_SECURITY_CENTER = MINE + "/SecurityCenter";
        public static final String PAGER_SECURITY_VERIFY = MINE + "/SecurityVerify";
        public static final String PAGER_SECURITY_VERIFY_CHOOSE = MINE + "/SecurityVerifyChoose";
        public static final String PAGER_BIND = MINE + "/Bind";
        public static final String PAGER_BIND_PHONE = MINE + "/BindPhone";
        public static final String PAGER_BIND_EMAIL = MINE + "/BindEmail";
        public static final String PAGER_CHANGE_PWD = MINE + "/ChangePwd";
    }

    public static class Wallet {
        private static final String WALLET = "/wallet";
        public static final String PAGER_TRANSFER = WALLET + "/Mine";
        public static final String PAGER_WITHDRAW = WALLET + "/Withdraw";

    }

    public static class Recharge {
        private static final String RECHARGE = "/recharge";
        public static final String PAGER_RECHARGE = RECHARGE + "/Recharge";
        public static final String PAGER_RECHARGE_FEEDBACK = RECHARGE + "/RechargeFeedback";
    }

    /**
     * 工作组件
     */
    public static class Work {
        private static final String WORK = "/work";
        /*工作*/
        public static final String PAGER_WORK = WORK + "/Work";
    }

    /**
     * 消息组件
     */
    public static class Msg {
        private static final String MSG = "/msg";
        /*消息*/
        public static final String PAGER_MSG = MSG + "/msg/Msg";
    }

    /**
     * 用户组件
     */
    public static class User {
        private static final String USER = "/user";
        /*我的*/
        public static final String PAGER_ME = USER + "/Me";
    }

    /**
     * 投注组件
     */
    public static class Bet {
        private static final String BET = "/bet";
        public static final String PAGER_BET_HOME = BET + "/home";
    }
}
