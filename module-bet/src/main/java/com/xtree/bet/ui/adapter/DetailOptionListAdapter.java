package com.xtree.bet.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.OptionList;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


public class DetailOptionListAdapter extends CommonAdapter<OptionList> {
    public DetailOptionListAdapter(Context context, int layoutId, List<OptionList> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, OptionList optionList, int position) {
        RecyclerView rvOptionList = holder.getView(R.id.rv_option_list);
        int spanCount = optionList.getOptionList().size() >= 3 ? 3 : optionList.getOptionList().size();
        rvOptionList.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        //rvOptionList.setAdapter(new OptionAdapter(mContext, optionList.getOptionList()));
    }
}
