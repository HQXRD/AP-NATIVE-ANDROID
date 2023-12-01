package com.xtree.bet.ui.adapter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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
        boolean isExpand = league.isExpand();
        holder.setVisible(R.id.rv_match, isExpand);
        holder.setText(R.id.tv_league_name, league.getLeagueName());
        RecyclerView rvMatch = holder.getView(R.id.rv_match);
        rvMatch.setLayoutManager(new LinearLayoutManager(rvMatch.getContext()));
        rvMatch.setAdapter(new MatchAdapter(rvMatch.getContext(), R.layout.bt_fb_match_list_item, league.getMatchList()));
        Glide.with(rvMatch.getContext())
                .load(league.getIcon())
                //.apply(new RequestOptions().placeholder(placeholderRes))
                .into((ImageView) holder.getView(R.id.iv_icon));
        holder.getView(R.id.rl_league).setOnClickListener(view -> {
            if(league.isExpand()){
                rvMatch.setAnimation(moveToViewTop(rvMatch));
            }else{
                rvMatch.setAnimation(moveTopToViewLocaton(rvMatch));
            }

            league.setExpand(!league.isExpand());
            holder.setVisible(R.id.rv_match, league.isExpand());
        });
    }

    public TranslateAnimation moveToViewTop(View view){
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return mHiddenAction;
    }

    public TranslateAnimation moveTopToViewLocaton(View view){
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mShowAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return mShowAction;
    }
}
