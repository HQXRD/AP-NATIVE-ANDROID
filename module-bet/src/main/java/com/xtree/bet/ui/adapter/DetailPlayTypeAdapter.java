package com.xtree.bet.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.PlayType;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class DetailPlayTypeAdapter extends CommonAdapter<PlayType> {
    public DetailPlayTypeAdapter(Context context, int layoutId, List<PlayType> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PlayType playType, int position) {
        holder.setText(R.id.tv_playtype_name, playType.getPlayTypeName());
        RecyclerView rvOptionList = holder.getView(R.id.rv_option_list);
        rvOptionList.setLayoutManager(new LinearLayoutManager(mContext));
        rvOptionList.setAdapter(new DetailOptionListAdapter(mContext, R.layout.bt_fb_detail_item_option_list_item, playType.getOptionLists()));
    }
}
