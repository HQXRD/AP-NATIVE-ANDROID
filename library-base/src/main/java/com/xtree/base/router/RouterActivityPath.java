package com.xtree.base.router;

/**
 * 用于组件开发中，ARouter单Activity跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 * Created by goldze on 2018/6/21
 */
public class RouterActivityPath {
    /**
     * 主业务组件
     */
    public static class Main {
        private static final String MAIN = "/main";
        /*主业务界面*/
        public static final String PAGER_MAIN = MAIN + "/Main";
    }

    /**
     * 身份验证组件
     */
    public static class Sign {
        private static final String SIGN = "/sign";
        /*登录界面*/
        public static final String PAGER_LOGIN = SIGN + "/Login";
    }

    /**
     * 用户组件
     */
    public static class User {
        private static final String USER = "/user";
        /*用户详情*/
        public static final String PAGER_USERDETAIL = USER + "/UserDetail";
    }

    /**
     * 我的组件
     */
    public static class Mine {
        private static final String MINE = "/mine";
        /*登录注册*/
        public static final String PAGER_LOGIN_REGISTER = MINE + "/loginRegister";
        public static final String PAGER_WITHDRAW = MINE + "/withdraw";
        public static final String PAGER_CHOOSE_WITHDRAW = MINE + "/choose";
        public static final String PAGER_MY_WALLET_FLOW = MINE + "/flow";//活动流水
    }

    /**
     * 投注组件
     */
    public static class Bet {
        private static final String BET = "/bet";
        public static final String PAGER_BET_HOME = BET + "/home";
    }

    /**
     * 组件页
     */
    public static class Widget {
        private static final String WIDGET = "/widget";
        public static final String PAGER_BROWSER = WIDGET + "/browser";
        public static final String PAGER_FORBIDDEN = WIDGET + "/forbidden"; // 403页面
    }

}
