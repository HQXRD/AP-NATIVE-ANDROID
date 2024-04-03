package com.xtree.base.global;

public class Constant {

    public static final String LoginRegisterActivity_ENTER_TYPE = "enter_type";
    public static final int LoginRegisterActivity_LOGIN_TYPE = 0x1001;
    public static final int LoginRegisterActivity_REGISTER_TYPE = 0x1002;

    public static final String VERIFY_BIND_PHONE = "verify_bind_phone"; // 邮箱验证后绑定手机
    public static final String VERIFY_BIND_PHONE2 = "verify_bind_phone2"; // 邮箱验证后绑定手机 (补)
    public static final String VERIFY_BIND_EMAIL = "verify_bind_email"; // 手机验证后绑定邮箱
    public static final String VERIFY_BIND_EMAIL2 = "verify_bind_email2"; // 手机验证后绑定邮箱 (补)

    public static final String RESET_LOGIN_PASSWORD = "resetloginpassword"; // 修改登录密码

    public static final String UPDATE_PHONE = "update_phone"; // 更新手机
    public static final String UPDATE_PHONE2 = "update_phone2"; // 更新手机 (补)
    public static final String UPDATE_EMAIL = "update_email"; // 更新邮箱 (老邮箱)
    public static final String UPDATE_EMAIL2 = "update_email2"; // 更新邮箱 (新邮箱)

    public static final String BIND = "bind"; // 绑定手机或邮箱 (补)

    public static final String BIND_PHONE = "bind_phone"; // 绑定手机
    public static final String BIND_EMAIL = "bind_email"; // 绑定邮箱
    public static final String BIND_CARD = "bindcard"; // 绑定银行卡
    public static final String BIND_USDT = "bindusdt"; // 绑定usdt
    public static final String BIND_EBPAY = "bindebpay"; // 绑定ebpay
    public static final String BIND_TOPAY = "bindtopay"; // 绑定topay
    public static final String BIND_HIWALLET = "bindhiwallet"; // 绑定hiwallet
    public static final String BIND_MPAY = "bindmpay"; // 绑定mpay
    public static final String BIND_GCNYT = "bindgcnyt"; // 绑定cnyt
    public static final String BIND_GOBAO = "bindgobao"; // 绑定gobao
    public static final String BIND_GOPAY = "bindgopay"; // 绑定GOPAY
    public static final String BIND_OKPAY = "bindokpay"; // 绑定OKPAY
    public static final String BIND_ALIPAY = "bindcardzfb"; // 绑定支付宝
    public static final String BIND_WECHAT = "bindcardwx"; // 绑定微信
    public static final String VERIFY_LOGIN = "verify_login"; // 异地登录/换设备登录 (补)

    public static final String URL_DEPOSIT_FEEDBACK = "/webapp/#/depositFeedback"; // 充值—反馈
    public static final String URL_MY_MESSAGES = "/webapp/#/my/messages"; // 消息中心
    public static final String URL_CUSTOMER_SERVICE = "/webapp/#/customerService"; // 客服中心
    //public static final String URL_VIP_CENTER = "/webapp/#/vipcenter"; // VIP中心
    public static final String URL_VIP_CENTER = "/webapp/?isNative=1#/vipcenter"; // VIP中心
    public static final String URL_WITHDRAW = "/webapp/#/withdraw"; // 提现
    public static final String URL_PROFIT_LOSS = "/webapp/#/report/reports/eprofitloss"; // 盈亏报表
    public static final String URL_3RD_TRANSFER = "/webapp/#/report/reports/3rd_transfer"; // 第三方转账
    public static final String URL_REBATE_REPORT = "/webapp/#/report/compact/userantirepot"; // 返水报表

    public static final String URL_PARTNER = "https://sc.sc/"; // 官方合营
    public static final String URL_ACTIVITY = "/webapp/#/activity/"; // 网页前缀
    public static final String URL_SPORT_RULES = "/static/activity/sportRules/index.html"; // 体育规则
    public static final String URL_QA = "/webapp/#/my/qa"; // 常见问题
    public static final String URL_TUTORIAL = "/static/vir-tutorial/cnyt.html"; // 充值教程
    public static final String URL_ANTI_FRAUD = "/static/vir-tutorial/antiScam_m.html"; // 防骗教程
    public static final String URL_HELP = "/webapp/#/my/help"; // 帮助中心
    public static final String URL_BET_RECORD = "/webapp/#/report/bet-record"; // 投注记录
    public static final String URL_ACCOUNT_CHANGE = "/webapp/#/report/account-change"; // 账变记录
    public static final String URL_DW_RECORD = "/webapp/#/report/dw-record"; // 充提记录
    //public static final String URL_INVITE_FRIEND = "/webapp/#/activity/141"; // 邀请好友
    public static final String URL_INVITE_FRIEND = "/webapp/?isNative=1#/activity/141"; // 邀请好友
    public static final String URL_DC_CENTER = "/webapp/?isNative=1#/report/team/activity/reward"; // 优惠中心
    public static final String URL_APP_CENTER = "/webapp/#/features"; // 底部几个菜单中间的那个
    public static final String URL_LOTTERY_INFO = "/static/activity/xc/lotteryInfo.html"; // 彩种信息
    public static final String URL_PLAY_INTRO = "/static/activity/xc/rulesInfo.html"; // 玩法介绍

    public static final String URL_DOWNLOAD_HI_WALLET = "https://www.hiwalletapp.com/download"; // 下载嗨钱包
    public static final String CHOOSEACTIVITY_CHOOSE_ENTER_TYPE = "giftMoney";//获取礼金

    public static final int CHOOSEACTIVITY_CHOOSE_TYPE = 0x1888; //获取礼金
    public static final int CHOOSEACTIVITY_CHOOSE_TYPE_ALL = 0x1999; //获取礼金 获取支付方式

}
