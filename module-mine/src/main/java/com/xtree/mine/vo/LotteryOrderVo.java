package com.xtree.mine.vo;

/**
 * 彩票实体类 (投注列表-彩票),内层,列表和详情都用
 */
public class LotteryOrderVo {
    public String projectid; // 注单编号 "D20240130-141***EEF"
    //public String userid; // "5228471",
    //public String packageid; // "839024424",
    //public String lotteryid; // "14",
    //public String methodid; // "2370",
    public String writetime; // 注单时间 "2024-01-30 11:43:00",
    public String issue; // 投注期号 "20240130-141",
    public String code; // 投注内容 "5,6,7",
    public String totalprice; // 投注总额 "2.0000",
    public String bonus; // 奖金 "0.0000",
    public String iscancel; // "0", 0:默认(无),1:本人撤单,2:平台撤单,3:错开撤单,其它:暂无状态
    public String isgetprize; // "2", 0:未开奖,2:未中奖,1:(prizestatus=0 未派奖)已派奖
    public String prizestatus; // "0",
    public String modes; // 模式 "1", 厘
    public String multiple; // 倍数 "1",
    //public String i_code; // null,
    public String statuscode; // null, "2"
    //public String canneldeadline; // null,
    public String methodname; // "后三码后三直选",
    public String lotteryname; // "河内5分彩",
    public String username; // "tst033",
    //public String projectidCopy; // "2541404445",
    //public String codes_str; // "5,6,7",
    //public String ii; // 0

    // 详情
    //"lotteryid": "14",
    //"methodid": "2370",
    //"userid": "2888826",
    //"writetime": "2024-02-03 15:54:45",
    //"issue": "20240203-192",
    //"code": "1,1,1",
    //"totalprice": "0.0020",
    //"bonus": "0.0000",
    //"iscancel": "0",
    //"isgetprize": "2",
    //"prizestatus": "0",
    //"modes": "厘",
    //"multiple": "1",
    //"omodel": "2",
    //"prizemodel": "{\"methodpoint\":\"0.0\",\"levels\":[\"1960.00\"]}"
    //omodel; // "2", (详情接口多的字段)
    //prizemodel; // "{"methodpoint":"0.0","levels":["1960.00"]}" (详情接口多的字段)

}
