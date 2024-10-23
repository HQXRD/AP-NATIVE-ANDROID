package com.xtree.bet.ui.fragment;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.xtree.bet.R;
import com.xtree.bet.databinding.BtDialogOddsTipBinding;

public class TipOddsDialog extends CenterPopupView {

    ICallBack mCallBack;

    BtDialogOddsTipBinding binding;

    public interface ICallBack {
        void onClick();
    }

    public TipOddsDialog(@NonNull Context context) {
        super(context);
    }


    public TipOddsDialog(@NonNull Context context, ICallBack mCallBack) {
        super(context);
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.bt_dialog_odds_tip;
    }

    private void initView() {
        binding = BtDialogOddsTipBinding.bind(findViewById(R.id.ll_root));

        binding.tvwMsg2.setText(getContext().getString(R.string.bt_txt_odds));
        binding.tvwMsg.setText("1. 下注时若赔率升高，系统将默认您接受此赔率，不会打断您的下注行为，提高下注成功率。\n\n " +
                "2. 下注时若赔率降低，系统将默认您不接受此赔率，并打断您的下注行为，您需要确认赔率后再次投注。\n\n" +
                "3. 若未勾选此功能，系统将认为您【自动接受任何赔率】\n" +
                "（即无论下注时的赔率如何变化，系统都将默认您可以接受，不会打断您的下注行为）");

        binding.tvwKnow.setOnClickListener(v -> {
            if (mCallBack != null) {
                mCallBack.onClick();
            }
        });

    }

}
