package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.view.View;

import com.stx.xhb.androidx.XBanner;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayType;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class BetConfirmOptionAdapter extends CommonAdapter<BetConfirmOption> {
    public BetConfirmOptionAdapter(Context context, int layoutId, List<BetConfirmOption> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BetConfirmOption option, int position) {
        holder.setText(R.id.iv_name, option.getOption().getSortName());
        holder.setText(R.id.iv_play_type, option.getScore());
        holder.setText(R.id.iv_match_team, option.getTeamName());
        holder.setText(R.id.iv_odd, String.valueOf(option.getOption().getOdd()));
        holder.setOnClickListener(R.id.iv_option_delete, view -> {

        });
        holder.setVisible(R.id.iv_option_delete, getItemCount() > 1);
    }
}
