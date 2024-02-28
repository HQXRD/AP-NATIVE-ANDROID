package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.databinding.BtLayoutCarBtMatchItemBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BetConfirmOptionAdapter extends BaseAdapter<BetConfirmOption> {
    private BtCarDialogFragment btCarDialogFragment;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable;

    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_bt_match_item;
    }

    public BetConfirmOptionAdapter(Context context, List<BetConfirmOption> datas) {
        super(context, datas);
    }

    public void setBtCarDialogFragment(BtCarDialogFragment btCarDialogFragment) {
        this.btCarDialogFragment = btCarDialogFragment;
    }

    @Override
    protected void convert(ViewHolder holder, BetConfirmOption betConfirmOption, int position) {

        BtLayoutCarBtMatchItemBinding binding = BtLayoutCarBtMatchItemBinding.bind(holder.itemView);
        binding.tvName.setText(betConfirmOption.getOptionName());
        String score = betConfirmOption.getScore();
        if(TextUtils.isEmpty(score)){
            if(mContext instanceof MainActivity) {
                score = ((MainActivity) mContext).getScore(betConfirmOption.getMatch().getId());
            }
        }

        if(TextUtils.isEmpty(score)){
            holder.setText(R.id.iv_play_type, betConfirmOption.getPlayType().getPlayTypeName());
        }else {
            holder.setText(R.id.iv_play_type, betConfirmOption.getPlayType().getPlayTypeName() + "[" + score + "]");
        }
        binding.ivMatchTeam.setText(betConfirmOption.getTeamName());
        binding.tvLeagueName.setText(betConfirmOption.getMatch().getLeague().getLeagueName());
        binding.ivOdd.setBtCar(true);
        binding.ivOdd.setOptionOdd(betConfirmOption.getOption());
        String oldScore = ((TextView)holder.getView(R.id.iv_play_type)).getText().toString();
        if(oldScore.indexOf("[") > -1 && oldScore.indexOf("]") > -1) {
            oldScore = oldScore.substring(betConfirmOption.getPlayType().getPlayTypeName().length() + 1, oldScore.length() - 1);
            if (!TextUtils.isEmpty(betConfirmOption.getScore()) && !TextUtils.isEmpty(oldScore) && !TextUtils.equals(betConfirmOption.getScore(), oldScore)) {
                holder.setVisible(R.id.iv_tip, true);
                holder.setText(R.id.iv_tip, mContext.getResources().getString(R.string.bt_bt_score_has_changed));
                binding.ivTip.postDelayed(() -> binding.ivTip.setVisibility(View.INVISIBLE), 4000);
            }
        }

        holder.setOnClickListener(R.id.iv_option_delete, view -> {
            BtCarManager.removeBtCar(betConfirmOption);
            if(BtCarManager.size() < 2){
                btCarDialogFragment.dismiss();
                return;
            }
            List<BetConfirmOption> newData = new ArrayList<>();
            newData.addAll(BtCarManager.getBtCarList());
            setNewData(newData);

            //快速点击删除时，只执行最后一次
            if (mRunnable != null) {
                mHandler.removeCallbacks(mRunnable);
            }
            mRunnable = () -> btCarDialogFragment.batchBetMatchMarketOfJumpLine();
            mHandler.postDelayed(mRunnable, 400);

        });
        holder.setVisible(R.id.iv_option_delete, getItemCount() > 1);
        holder.setVisible(R.id.ll_close_tip, betConfirmOption.isClose());
    }
}
