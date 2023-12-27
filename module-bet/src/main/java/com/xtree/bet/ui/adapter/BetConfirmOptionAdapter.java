package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.stx.xhb.androidx.XBanner;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.databinding.BtLayoutBtCarBinding;
import com.xtree.bet.databinding.BtLayoutBtCarBindingImpl;
import com.xtree.bet.databinding.BtLayoutCarBtItemBinding;
import com.xtree.bet.databinding.BtLayoutKeyboardItemBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BetConfirmOptionAdapter extends BaseAdapter<BetConfirmOption> {
    private BtCarDialogFragment btCarDialogFragment;
    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_bt_item;
    }

    public BetConfirmOptionAdapter(Context context, List<BetConfirmOption> datas) {
        super(context, datas);
    }

    public void setBtCarDialogFragment(BtCarDialogFragment btCarDialogFragment) {
        this.btCarDialogFragment = btCarDialogFragment;
    }

    @Override
    protected void convert(ViewHolder holder, BetConfirmOption option, int position) {

        BtLayoutCarBtItemBinding binding = BtLayoutCarBtItemBinding.bind(holder.itemView);
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

        if(TextUtils.isEmpty(score)){
            holder.setText(R.id.iv_play_type, option.getPlayType().getPlayTypeName());
        }else {
            holder.setText(R.id.iv_play_type, option.getPlayType().getPlayTypeName() + "[" + score + "]");
        }
        binding.ivMatchTeam.setText(option.getTeamName());
        binding.ivOdd.setText("@" + option.getOption().getOdd());
        String oldScore = ((TextView)holder.getView(R.id.iv_play_type)).getText().toString();
        if(oldScore.indexOf("[") > -1 && oldScore.indexOf("]") > -1) {
            oldScore = oldScore.substring(option.getPlayType().getPlayTypeName().length() + 1, oldScore.length() - 1);
            if (!TextUtils.isEmpty(option.getScore()) && !TextUtils.isEmpty(oldScore) && !TextUtils.equals(option.getScore(), oldScore)) {
                holder.setVisible(R.id.iv_tip, true);
                holder.setText(R.id.iv_tip, mContext.getResources().getString(R.string.bt_bt_score_has_changed));
                binding.ivTip.postDelayed(() -> binding.ivTip.setVisibility(View.GONE), 4000);
            }
        }

        holder.setOnClickListener(R.id.iv_option_delete, view -> {
            BtCarManager.removeBtCar(option);
            if(BtCarManager.size() < 2){
                btCarDialogFragment.dismiss();
                return;
            }
            List<BetConfirmOption> newData = new ArrayList<>();
            newData.addAll(BtCarManager.getBtCarList());
            setNewData(newData);
            btCarDialogFragment.batchBetMatchMarketOfJumpLine();
        });
        holder.setVisible(R.id.iv_option_delete, getItemCount() > 1);
    }
}
