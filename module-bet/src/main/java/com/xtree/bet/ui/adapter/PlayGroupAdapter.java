package com.xtree.bet.ui.adapter;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stx.xhb.androidx.XBanner;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayGroup;

public class PlayGroupAdapter implements XBanner.XBannerAdapter {
    Match match;

    /*public void setMatch(Match match) {
        this.match = match;
    }*/

    public PlayGroupAdapter(Match match){
        this.match = match;
    }

    @Override
    public void loadBanner(XBanner banner, Object model, View view, int position) {
        PlayGroup playGroup1 = (PlayGroup) model;

        RecyclerView rvPlayType = view.findViewById(R.id.rv_group);
        rvPlayType.setLayoutManager(new GridLayoutManager(banner.getContext(), 3));
        rvPlayType.setAdapter(new PlayTypeAdapter(banner.getContext(), R.layout.bt_fb_list_item_play_type_item, playGroup1.getPlayTypeList(), match));
    }
}
