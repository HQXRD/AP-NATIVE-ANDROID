package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stx.xhb.androidx.XBanner;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayType;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MatchAdapter extends CommonAdapter<Match> {
    public MatchAdapter(Context context, int layoutId, List<Match> datas) {
        super(context, layoutId, datas);
    }

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


        List<PlayType> playTypeList = match.getPlayTypeList();
        PlayGroup playGroup = new PlayGroup(playTypeList);
        List<PlayGroup> playGroupList = playGroup.getPlayGroupList();

        XBanner playTypeBanner = holder.getView(R.id.play_type_banner);
        playTypeBanner.setBannerData(R.layout.bt_fb_list_item_play_type, playGroupList);
        playTypeBanner.loadImage(new PlayGroupAdapter());
        playTypeBanner.getViewPager().setOverScrollMode(View.OVER_SCROLL_NEVER);

        holder.setVisible(R.id.iv_court, match.hasAs());
        holder.setVisible(R.id.iv_live, match.hasVideo());

    }
}
