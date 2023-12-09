package com.xtree.bet.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.PlayType;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;

public class PlayTypeAdapter extends CommonAdapter<PlayType> {
    public PlayTypeAdapter(Context context, int layoutId, List<PlayType> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, PlayType playType, int position) {
        holder.setText(R.id.tv_playtype_name, playType.getPlayTypeName());
        RecyclerView rvOption = holder.getView(R.id.rv_option);
        rvOption.setLayoutManager(new GridLayoutManager(mContext, 1));
        OptionAdapter optionAdapter = new OptionAdapter(mContext, R.layout.bt_fb_list_item_play_type_item_option, playType.getOptionList());
        optionAdapter.setMeasure(true);
        rvOption.setAdapter(optionAdapter);
    }
}
