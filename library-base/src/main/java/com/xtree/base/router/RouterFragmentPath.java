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
        public static final String PAGER_FUNDS_PWD = MINE + "/FundsPwd"; //修改资金密码
        public static final String PAGER_REGISTER_PROMOTION = MINE + "/RegisterPromotion";
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
        public static final String PAGER_REBATE_AGREEMENT = MINE + "/RebateAgreement"; // 返水契约
        public static final String PAGER_REBATE_AGREEMENT_GAME = MINE + "/GameRebateAgrt"; // 游戏场馆返水契约
        public static final String PAGER_DIVIDEND_AGREEMENT_GAME = MINE + "/GameDividendAgrt"; // 游戏场馆分红契约
        public static final String PAGER_RECOMMENDED_REPORTS_GAME = MINE + "/RecommendedReports"; // 游戏推荐报表/彩票推荐报表
        public static final String PAGER_REBATEAGRT_CREATE_DIALOG = MINE + "/RebateAgrtCreateDialog"; // 返水契约-创建契约
        public static final String PAGER_REBATEAGRT_SEARCHUSER_DIALOG = MINE + "/RebateAgrtSearchUserDialog"; // 返水契约-搜索用户
        public static final String PAGER_DIVIDENDAGRT_SEND_DIALOG = MINE + "/DividendAgrtSendDialog"; // 返水契约-分红契约-手动发放分红
        public static final String PAGER_DIVIDENDAGRT_CHECK_DIALOG = MINE + "/DividendAgrtCheckDialog"; //返水契约-分红契约-查看契约
        public static final String PAGER_COMMISSIONSREPORTS = MINE + "/CommissionsReports"; //返水契约-佣金报表

        public static final String PAGER_THIRD_TRANSFER = MINE + "/ThirdTransfer"; // 三方转账
        public static final String PAGER_RECHARGE_WITHDRAW = MINE + "/RechargeWithdraw"; // 充提记录
        public static final String PAGER_BT_REPORT = MINE + "/BtReport"; // 投注记录
        public static final String PAGER_FORGET_PASSWORD = MINE + "/ForgetPassword"; // 忘记密码
        public static final String PAGER_MSG = MINE + "/Msg"; // 忘记密码
        public static final String PAGER_MSG_LIST = MINE + "/MsgList"; // 忘记密码
        public static final String PAGER_MSG_CHAT = MINE + "/MsgChat"; // 上下级聊天
        public static final String PAGER_MSG_PERSON_LIST = MINE + "/MsgPersonList"; // 忘记密码
        public static final String PAGER_VIP_UPGRADE = MINE + "/VipUpgrade"; // VIP升级资讯
        public static final String PAGER_VIP_INFO = MINE + "/VipInfo"; // VIP资讯
        public static final String PAGER_INFO = MINE + "/Info"; // 帮助中心
        public static final String PAGER_QUESTION = MINE + "/QUESTION"; // 网页资讯
        public static final String PAGER_CHOOSE = MINE + "/ChooseWithdraw"; // 提款
        public static final String PAGER_MY_WALLET = MINE + "/wallet";
        public static final String PAGER_MEMBER_MANAGER = MINE + "/MemberManager"; // 团队管理
        public static final String PAGER_MEMBER_TRANSFER = MINE + "/MemberMemberTransfer"; // 下级转账
        public static final String PAGER_BONUS_REPORT = MINE + "/BonusReport"; // 代理服务費
    }

    public static class Wallet {
        private static final String WALLET = "/wallet";
        public static final String PAGER_TRANSFER = WALLET + "/Transfer";
        //转账结果
        public static final String PAGER_TRANSFER_RESULT = WALLET + "/TransferResult";
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

    public static class Transfer {
        private static final String Transfer = "/transfer";
        //极速转账-提交订单
        public static final String PAGER_TRANSFER_EX_COMMIT = Transfer + "/ExTransferCommitFragment";
        //极速转账-确认付款
        public static final String PAGER_TRANSFER_EX_CONFIRM = Transfer + "/ExTransferConfirmFragment";
        //极速转账-失败
        public static final String PAGER_TRANSFER_EX_FAIL = Transfer + "/ExTransferFailFragment";
        //极速转账-转账汇款
        public static final String PAGER_TRANSFER_EX_PAYEE = Transfer + "/ExTransferPayeeFragment";
        //极速转账-上传凭证
        public static final String PAGER_TRANSFER_EX_VOUCHER = Transfer + "/ExTransferVoucherFragment";
        //极速转账-客服
        public static final String PAGER_TRANSFER_EX_CHAT = Transfer + "/CommChatFragment";

    }
}
