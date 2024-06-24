package com.xtree.mine.ui.fragment.withdrawal;

import com.xtree.mine.vo.withdrawals.WithdrawalListVo;

public interface IWithdrawListCallback {
    public void onClickListItem(final WithdrawalListVo.WithdrawalItemVo itemVo);
}
