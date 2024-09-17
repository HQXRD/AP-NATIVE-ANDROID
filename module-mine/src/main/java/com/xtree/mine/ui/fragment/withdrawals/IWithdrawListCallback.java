package com.xtree.mine.ui.fragment.withdrawals;


import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;

public interface IWithdrawListCallback {
    public void onClickListItem(final WithdrawalListVo.WithdrawalItemVo itemVo);
}
