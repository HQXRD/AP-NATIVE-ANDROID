package com.xtree.bet.ui.adapter;

import android.content.Context;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Option;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;

public class OptionAdapter extends CommonAdapter<Option> {
    public OptionAdapter(Context context, int layoutId, List<Option> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Option option, int position) {
        holder.setText(R.id.tv_option_name, option.getSortName());
        holder.setText(R.id.tv_option_odd, String.valueOf(option.getOdd()));
        float h = mContext.getResources().getDimension(R.dimen.bt_match_list_height) - ConvertUtils.dp2px(40);
        float optionHeight = h / getItemCount();
        holder.itemView.getLayoutParams().height = (int)optionHeight;
    }
}
