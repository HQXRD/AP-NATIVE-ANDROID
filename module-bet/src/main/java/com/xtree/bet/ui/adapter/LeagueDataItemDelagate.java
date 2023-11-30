package com.xtree.bet.ui.adapter;

import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class LeagueDataItemDelagate implements ItemViewDelegate<League> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.bt_fb_list_item;
    }

    @Override
    public boolean isForViewType(League item, int position) {
        return !item.isHead();
    }

    @Override
    public void convert(ViewHolder holder, League league, int position) {
        boolean isExpand = league.getExpand();
        holder.setVisible(R.id.rv_match, isExpand);
        holder.setText(R.id.tv_league_name, league.getLeagueName());
        RecyclerView rvMatch = holder.getView(R.id.rv_match);
        rvMatch.setLayoutManager(new LinearLayoutManager(rvMatch.getContext()));
        rvMatch.setAdapter(new MatchAdapter(rvMatch.getContext(), R.layout.bt_fb_match_list_item, league.getMatchList()));
        Glide.with(rvMatch.getContext())
                .load(league.getIcon())
                //.apply(new RequestOptions().placeholder(placeholderRes))
                .into((ImageView) holder.getView(R.id.iv_icon));
        holder.itemView.setOnClickListener(view -> {
            league.setExpand(!league.getExpand());
            holder.setVisible(R.id.rv_match, league.getExpand());
        });
    }
}
