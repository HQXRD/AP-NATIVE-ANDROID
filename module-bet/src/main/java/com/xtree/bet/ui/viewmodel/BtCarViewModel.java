package com.xtree.bet.ui.viewmodel;

import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.CgOddLimit;

import java.util.List;

public interface BtCarViewModel {
    /**
     * 投注前查询指定玩法赔率
     */
    void batchBetMatchMarketOfJumpLine(List<BetConfirmOption> betConfirmOptionList);
    /**
     * 单关投注
     */
    void singleBet(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, int acceptOdds);
    /**
     * 串关投注
     */
    void betMultiple(List<BetConfirmOption> betConfirmOptionList, List<CgOddLimit> cgOddLimitList, int acceptOdds);
}
