package com.xtree.bet.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ConvertUtils;

public class OptionAdapter extends CommonAdapter<Option> {

    private boolean isMeasure;

    public void setMeasure(boolean measure) {
        isMeasure = measure;
    }

    public OptionAdapter(Context context, int layoutId, List<Option> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Option option, int position) {
        holder.setText(R.id.tv_option_name, option.getSortName());
        holder.setText(R.id.tv_option_odd, String.valueOf(option.getOdd()));

        if(isMeasure) {
            float h = mContext.getResources().getDimension(R.dimen.bt_match_list_height) - ConvertUtils.dp2px(40);
            float optionHeight = h / getItemCount();
            holder.itemView.getLayoutParams().height = (int) optionHeight;
        }

        holder.itemView.setOnClickListener(view -> {
            BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
            if(mContext instanceof BaseActivity) {
                btCarDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "btCarDialogFragment");
            }
        });
    }
}
