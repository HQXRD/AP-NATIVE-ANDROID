package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.databinding.BtLayoutCarBtItemBinding;
import com.xtree.bet.databinding.BtLayoutCarBtResultItemBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BetResultOptionAdapter extends BaseAdapter<BetConfirmOption> {
    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_bt_result_item;
    }

    public BetResultOptionAdapter(Context context, List<BetConfirmOption> datas) {
        super(context, datas);
    }

    @Override
    protected void convert(ViewHolder holder, BetConfirmOption option, int position) {

        BtLayoutCarBtResultItemBinding binding = BtLayoutCarBtResultItemBinding.bind(holder.itemView);
        String optionName;
        String name = option.getOption().getName();
        String sortName = option.getOption().getSortName();
        if(name != null && sortName != null){
            optionName = option.getOption().getName().length() > option.getOption().getSortName().length() ? option.getOption().getName() : option.getOption().getSortName();
        } else if (sortName == null) {
            optionName = name;
        } else {
            optionName = sortName;
        }
        binding.tvName.setText(optionName);
        String score = option.getScore();
        if(TextUtils.isEmpty(score)){
            if(mContext instanceof MainActivity) {
                score = ((MainActivity) mContext).getScore(option.getMatch().getId());
            }
        }
        String playTypeName = option.getPlayType().getPlayTypeName();
        if(!TextUtils.isEmpty(score)){
            playTypeName += "[" + score + "]";
        }
        binding.ivPlayType.setText(playTypeName);
        binding.ivMatchTeam.setText(option.getTeamName());
        binding.ivOdd.setText("@" + option.getOption().getOdd());
    }
}
