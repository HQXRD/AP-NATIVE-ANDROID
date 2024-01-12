package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.databinding.BtLayoutCarBtMatchItemBinding;
import com.xtree.bet.ui.activity.MainActivity;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class BetResultOptionAdapter extends BaseAdapter<BetConfirmOption> {
    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_bt_match_item;
    }

    public BetResultOptionAdapter(Context context, List<BetConfirmOption> datas) {
        super(context, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BetConfirmOption option, int position) {

        BtLayoutCarBtMatchItemBinding binding = BtLayoutCarBtMatchItemBinding.bind(holder.itemView);
        binding.tvName.setText(option.getOptionName());
        String score = option.getScore();
        if(TextUtils.isEmpty(score)){
            if(mContext instanceof MainActivity) {
                score = ((MainActivity) mContext).getScore(option.getMatch().getId());
            }
        }
        String playTypeName = option.getPlayType().getPlayTypeName();
        if(!TextUtils.isEmpty(score)){
            playTypeName += "[" + score + "]";
        }
        binding.ivPlayType.setText(playTypeName);
        binding.ivMatchTeam.setText(option.getTeamName());
        binding.ivOdd.setText("@" + option.getOption().getOdd());
        binding.ivOptionDelete.setVisibility(View.GONE);

    }
}
