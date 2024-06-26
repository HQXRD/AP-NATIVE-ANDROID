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

        public static final String AD = HOME + "/ad";
        public static final String AUG = HOME + "/Aug";
        public static final String ELE = HOME + "/Ele";
        public static final String PG_DEBUG = HOME + "/PageDebug";

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
        /**
         * 綁定谷歌动态口令
         */
        public static final String PAGER_BIND_GOOGLE_PWD = MINE + "/GooglePSW";

        public static final String PAGER_BIND_CARD = MINE + "/BindCard";
        public static final String PAGER_BIND_ALIPAY_WECHAT = MINE + "/BindAlipayWechat";
        public static final String PAGER_BIND_CARD_ADD = MINE + "/BindCardAdd";
        public static final String PAGER_BIND_AW_ADD = MINE + "/BindAWAdd";
        public static final String PAGER_BIND_AW_TIP = MINE + "/BindAWTip";
        public static final String PAGER_BIND_CARD_LOCK = MINE + "/BindCardLock";
        public static final String PAGER_BIND_USDT = MINE + "/BindUsdtList";
        public static final String PAGER_BIND_USDT_ADD = MINE + "/BindUsdtAdd";
        public static final String PAGER_BIND_USDT_REBIND = MINE + "/BindUsdtRebind";

        public static final String PAGER_ACCOUNT_CHANGE = MINE + "/AccountChange"; //账变记录
        public static final String PAGER_PROFIT_LOSS = MINE + "/ProfitLoss"; // 盈亏报表
        public static final String PAGER_REBATE_REPORT = MINE + "/RebateReport"; // 返水报表
        public static final String PAGER_THIRD_TRANSFER = MINE + "/ThirdTransfer"; // 三方转账
        public static final String PAGER_RECHARGE_WITHDRAW = MINE + "/RechargeWithdraw"; // 充提记录
        public static final String PAGER_BT_REPORT = MINE + "/BtReport"; // 投注记录
        public static final String PAGER_FORGET_PASSWORD = MINE + "/ForgetPassword"; // 忘记密码
        public static final String PAGER_MSG = MINE + "/Msg"; // 忘记密码
        public static final String PAGER_MSG_LIST = MINE + "/MsgList"; // 忘记密码
        public static final String PAGER_MSG_PERSON_LIST = MINE + "/MsgPersonList"; // 忘记密码
        public static final String PAGER_VIP_UPGRADE = MINE + "/VipUpgrade"; // VIP升级资讯
        public static final String PAGER_VIP_INFO = MINE + "/VipInfo"; // VIP资讯
        public static final String PAGER_INFO = MINE + "/Info"; // 帮助中心
        public static final String PAGER_QUESTION = MINE + "/QUESTION"; // 网页资讯
        public static final String PAGER_CHOOSE = MINE + "/ChooseWithdraw"; // 提款
        public static final String PAGER_MY_WALLET = MINE + "/wallet";
    }

    public static class Wallet {
        private static final String WALLET = "/wallet";
        public static final String PAGER_TRANSFER = WALLET + "/Transfer";
        public static final String PAGER_WITHDRAW = WALLET + "/Withdraw";

    }

    public static class Recharge {
        private static final String RECHARGE = "/recharge";
        public static final String PAGER_RECHARGE = RECHARGE + "/Recharge";
        public static final String PAGER_RECHARGE_FEEDBACK = RECHARGE + "/RechargeFeedback"; // 反馈填表
        public static final String PAGER_RECHARGE_FEEDBACK_EDIT = RECHARGE + "/RechargeFeedbackEdit"; // 反馈修改页面
        public static final String PAGER_RECHARGE_FEEDBACK_DETAIL = RECHARGE + "/RechargeFeedbackDetail"; // 反馈详情(充提记录)
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
        public static final String PAGER_BET_TUTORIAL = BET + "/tutorial";
    }
}
