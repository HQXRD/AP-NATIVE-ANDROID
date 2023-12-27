package com.xtree.bet.ui.adapter;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.contract.BetContract;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import me.xtree.mvvmhabit.bus.RxBus;

public class HeadItemDelagate implements ItemViewDelegate<League> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.bt_league_list_header;
    }

    @Override
    public boolean isForViewType(League item, int position) {
        return item.isHead();
    }

    @Override
    public void convert(ViewHolder holder, League item, int position) {
        holder.setText(R.id.tv_header_name, "未开赛");
        holder.itemView.setOnClickListener(view -> {
            RxBus.getDefault().post(new BetContract(BetContract.ACTION_EXPAND));
        });
    }
}
