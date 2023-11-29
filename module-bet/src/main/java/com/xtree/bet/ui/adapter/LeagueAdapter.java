package com.xtree.bet.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class LeagueAdapter extends CommonAdapter<League> {
    public LeagueAdapter(Context context, int layoutId, List<League> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, League league, int position) {
        holder.setText(R.id.tv_league_name, league.getLeagueName());
        ((RecyclerView) holder.getView(R.id.rv_match)).setLayoutManager(new LinearLayoutManager(mContext));
        ((RecyclerView) holder.getView(R.id.rv_match)).setAdapter(new CommonAdapter<Match>(mContext, R.layout.bt_fb_match_list_item, league.getMatchList()) {
            @Override
            protected void convert(ViewHolder holder, Match match, int position) {
                holder.setText(R.id.tv_team_name_main, match.getTeamMain());
                holder.setText(R.id.tv_team_name_visitor, match.getTeamVistor());
                if(match.getScore() != null && match.getScore().size() > 1){
                    holder.setText(R.id.tv_score_main, String.valueOf(match.getScore().get(0)));
                    holder.setText(R.id.tv_score_visitor, String.valueOf(match.getScore().get(1)));
                }
                holder.setText(R.id.tv_match_time, match.getStage() + " " + match.getTime());
                holder.setText(R.id.tv_playtype_count, match.getPlayTypeCount() + "+>");

            }
        });
    }
}
