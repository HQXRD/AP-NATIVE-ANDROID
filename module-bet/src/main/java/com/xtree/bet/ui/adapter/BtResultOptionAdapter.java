package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.BtResultOption;
import com.xtree.bet.databinding.BtLayoutBtRecordMatchItemBinding;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class BtResultOptionAdapter extends BaseAdapter<BtResultOption> {
    private boolean mIsAdvanceSettlement;
    BtResult data;

    public BtResultOptionAdapter(Context context, BtResult data, boolean isAdvanceSettlement) {
        super(context, data.getBetResultOption());
        this.data = data;
        mIsAdvanceSettlement = isAdvanceSettlement;
    }

    @Override
    public int layoutId() {
        return R.layout.bt_layout_bt_record_match_item;
    }

    @Override
    protected void convert(ViewHolder holder, BtResultOption option, int position) {
        BtLayoutBtRecordMatchItemBinding binding = BtLayoutBtRecordMatchItemBinding.bind(holder.itemView);
        binding.tvLeagueName.setText(option.getLeagueName());
        binding.tvOdd.setText(option.getOdd());
        binding.tvMatchTime.setText(mContext.getResources().getString(R.string.bt_bt_result_record_match_time, TimeUtils.longFormatString(option.getMatchTime(), TimeUtils.FORMAT_YY_MM_DD_HH_MM)));
        binding.tvName.setText(option.getOptionName());
        String playType = option.getPlayType();
        if (!TextUtils.isEmpty(option.getScore())) {
            playType += "[" + option.getScore() + "]";
        }
        binding.tvPlayType.setText(playType);
        binding.tvMatchTeam.setText(option.getTeamName());
        binding.tvFullScore.setText(option.getFullScore());
        if (option.isSettled()) {
            binding.tvResult.setVisibility(View.VISIBLE);
        } else {
            binding.tvResult.setVisibility(View.INVISIBLE);
        }
        if (data.getStatusDesc().equals("投注失败")) {
            binding.tvResult.setVisibility(View.INVISIBLE);
            binding.tvFullScore.setVisibility(View.INVISIBLE);
            binding.tvOdd.setVisibility(View.INVISIBLE);
        }
        if (!mIsAdvanceSettlement) {
            binding.tvResult.setText(option.getBtResult());
            binding.tvResult.setTextColor(mContext.getResources().getColor(option.getResultColor()));
        }
    }
}
