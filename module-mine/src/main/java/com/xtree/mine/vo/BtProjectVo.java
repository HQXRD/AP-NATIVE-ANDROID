package com.xtree.mine.vo;

import java.util.List;

public class BtProjectVo {

    public List<BtOrderVo> list;
    public String total_num; // 1284,
    public TotalVo total; // 本页合计
    public String total_bet; // 22465.5,
    public String total_win; // 16294.559,
    public String total_profit_loss; // -6170.941

    public static class TotalVo {
        public String bet; // 167.8,
        public String win; // 126.58,
        public String profit_loss; // -41.22
    }

}
