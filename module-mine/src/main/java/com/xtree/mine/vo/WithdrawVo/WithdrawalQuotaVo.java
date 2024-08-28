package com.xtree.mine.vo.WithdrawVo;

/**
 * 提款余额 提款列表
 */
public class WithdrawalQuotaVo {
    /* {
         "status": 10000,
             "message": "success",
             "data": {
         "fAvailableBalance": 9920918.25,
                 "unSportActivityAward": 0,
                 "quota": 9920918.25,
                 "nedusdt": true,
                 "formula": "(9920918.25)-(0+0+0+0-0-15558.0000+0.0000)+ (114000.0000)"
     },
         "timestamp": 1714027704
     }*/
    public int networkStatus;
    public String fAvailableBalance;
    public String unSportActivityAward;
    public String quota;
    public boolean nedusdt;

}
