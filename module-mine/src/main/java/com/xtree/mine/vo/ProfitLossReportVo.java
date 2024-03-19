package com.xtree.mine.vo;

import java.util.List;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * 盈亏
 */
public class ProfitLossReportVo extends BaseResponse2 {

    public List<ThirdGameTypeVo> fromgameArr; // 平台类型
    public List<ProfitLossVo> aProfitLoss; // 主列表
    public ProfitLossCountVo totalCount; // 总合计
    public ProfitLossCountVo pageCount; // 本页合计
    public List<UserVo> bread; // 本级

    public static class UserVo {
        public String userid; // 本级
        public String username; // 本级
    }

}
