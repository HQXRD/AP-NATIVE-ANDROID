package com.xtree.home.vo;

import com.xtree.base.vo.RechargeOrderVo;

import java.util.List;

import me.xtree.mvvmhabit.http.BaseResponse2;

public class RechargeReportVo {
    public BaseResponse2.MobilePageVo pages = new BaseResponse2.MobilePageVo(); // 页信息(充值记录)
    public List<RechargeOrderVo> result; // 充值记录
    public int nowPage; // 1, 当前页
    public String count; // "7" 库总条数
    public int showRows; // 10 每页条数
}