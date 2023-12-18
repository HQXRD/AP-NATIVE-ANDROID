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
import com.xtree.bet.manager.BtCarManager;
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
        String optionName = option.getOption().getName().length() > option.getOption().getSortName().length() ? option.getOption().getName() : option.getOption().getSortName();
        holder.setText(R.id.iv_name, optionName);
        holder.setText(R.id.iv_play_type, option.getPlayType().getPlayTypeName() + "[" + option.getScore() + "]");
        holder.setText(R.id.iv_match_team, option.getTeamName());
        holder.setText(R.id.iv_odd, "@" + String.valueOf(option.getOption().getOdd()));

        String oldScore = ((TextView)holder.getView(R.id.iv_play_type)).getText().toString();
        oldScore = oldScore.substring(option.getPlayType().getPlayTypeName().length() + 1, oldScore.length() - 1);
        TextView tvTip = (TextView) holder.getView(R.id.iv_tip);
        if (!TextUtils.isEmpty(option.getScore()) && !TextUtils.isEmpty(oldScore) && !TextUtils.equals(option.getScore(), oldScore)) {
            holder.setVisible(R.id.iv_tip, true);
            holder.setText(R.id.iv_tip, mContext.getResources().getString(R.string.bt_bt_score_has_changed));
            tvTip.postDelayed(() -> holder.setVisible(R.id.iv_tip, false), 4000);
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
