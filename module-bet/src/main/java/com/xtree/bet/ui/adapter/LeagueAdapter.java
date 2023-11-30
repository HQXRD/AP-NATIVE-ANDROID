package com.xtree.bet.ui.adapter;

import android.content.Context;
import com.xtree.bet.bean.ui.League;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

public class LeagueAdapter extends MultiItemTypeAdapter<League> {
    public LeagueAdapter(Context context, List<League> datas) {
        super(context, datas);
        addItemViewDelegate(new HeadItemDelagate());
        addItemViewDelegate(new LeagueDataItemDelagate());
    }

}
