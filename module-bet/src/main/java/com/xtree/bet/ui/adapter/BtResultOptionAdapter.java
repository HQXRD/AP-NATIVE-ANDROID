package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.BtResultOption;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.databinding.BtFbListItemPlayTypeItemOptionBinding;
import com.xtree.bet.databinding.BtLayoutBtRecordMatchItemBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.DiscolourTextView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class BtResultOptionAdapter extends BaseAdapter<BtResultOption> {
    private boolean mIsAdvanceSettlement;
    public BtResultOptionAdapter(Context context, List<BtResultOption> datas, boolean isAdvanceSettlement) {
        super(context, datas);
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
        if(!TextUtils.isEmpty(option.getScore())){
            playType += "["+ option.getScore() + "]";
        }
        binding.tvPlayType.setText(playType);
        binding.tvMatchTeam.setText(option.getTeamName());
        binding.tvFullScore.setText(option.getFullScore());
        if(option.isSettled()){
            binding.tvResult.setVisibility(View.VISIBLE);
        }else {
            binding.tvResult.setVisibility(View.INVISIBLE);
        }
        if(!mIsAdvanceSettlement) {
            binding.tvResult.setText(option.getBtResult());
            binding.tvResult.setTextColor(mContext.getResources().getColor(option.getResultColor()));
        }
    }
}
