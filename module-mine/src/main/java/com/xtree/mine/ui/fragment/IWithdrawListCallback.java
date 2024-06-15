package com.xtree.mine.ui.fragment;

import com.xtree.mine.vo.WithdrawVo.WithdrawalListVo;

/**
 * 点击提款列表回调
 */
public interface IWithdrawListCallback {
    public void onClickListItem(final WithdrawalListVo itemVo);
}
