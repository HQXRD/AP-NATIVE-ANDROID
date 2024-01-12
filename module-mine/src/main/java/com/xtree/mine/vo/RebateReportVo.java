package com.xtree.mine.vo;

import java.util.List;

/**
 * 返水
 */
public class RebateReportVo extends BaseReportVo {

    public Total total; // 总合计
    public Count count; // 本页合计
    public List<RebateVo> data; // [
    public String description; // "<p>温馨提示:</p>\r\n<p>1.日薪从契约生效日期起算流水，

    public static class Total {
        public String sum_bet; // 404,
        public String sum_effective_bet; // 404,
        public String sum_total_money; // 0,
        public String sum_sub_money; // 0,
        public String sum_self_money; // 1,
        public String sum_liushui; // 404
    }

    public static class Count {
        public String bet; // 404,
        public String effective_bet; // 404,
        public String liushui; // 404,
        public String total_money; // 0,
        public String sub_money; // 0,
        public String self_money; // 1
    }

}
