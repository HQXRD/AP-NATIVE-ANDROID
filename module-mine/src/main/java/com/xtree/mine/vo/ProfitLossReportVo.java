package com.xtree.mine.vo;

import java.util.List;

/**
 * 盈亏
 */
public class ProfitLossReportVo extends BaseReportVo {

    public List<ThirdGameTypeVo> fromgameArr; // 平台类型
    public List<ProfitLossVo> aProfitLoss; // 主列表
    public ProfitLossCountVo totalCount; // 总合计
    public ProfitLossCountVo pageCount; // 本页合计

}
