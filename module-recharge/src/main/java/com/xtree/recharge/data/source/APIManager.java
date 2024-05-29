package com.xtree.recharge.data.source;

/**
 * Created by KAKA on 2024/3/12.
 * Describe: api 静态管理
 */
public class APIManager {

    //存款
    private static final String DEPOSIT_API = "/api/deposit/";

    //充值建单申请
    public static final String DEPOSIT_CREATEORDER_URL = DEPOSIT_API + "createorder";
    //充值取消等待
    public static final String DEPOSIT_CANCELWAIT_URL = DEPOSIT_API + "rechargeOrderCancel/wait";
    //充值取消审核
    public static final String DEPOSIT_CANCELPROCESS_URL = DEPOSIT_API + "rechargeOrderCancel/process";
    //充值银行卡信息
    public static final String DEPOSIT_RECHARGEBANKINFO_URL = DEPOSIT_API + "rechargeBankInfo";
    //识别凭证信息ocr
    public static final String DEPOSIT_RECHARGERECEIPTOCR_URL = DEPOSIT_API + "rechargeReceiptOCR";
    //上传付款凭证
    public static final String DEPOSIT_RECHARGERECEIPTUPLOAD_URL = DEPOSIT_API + "rechargeReceiptUpload";

}
