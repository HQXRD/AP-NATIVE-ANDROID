package com.xtree.mine.vo;

import java.util.List;

/**
 * 充提 (充值, 提现, 反馈)
 */
public class RechargeReportVo extends BaseReportVo {

    public MobilePageVo pages; // 页信息(充值记录)
    public List<RechargeOrderVo> result; // 充值记录
    public List<WithdrawOrderVo> aProject; // 提现记录

    public List<FeedbackOrderVo> list; // 未到账反馈记录

    public int nowPage; // 1, 当前页
    public String count; // "7" 库总条数
    public int showRows; // 10 每页条数

    //List<Object> banksInfo; //
    //List<Object> modeInfo; //
    //List<Object> protocolInfo; //
    //List<Object> last3Deps; //
}
