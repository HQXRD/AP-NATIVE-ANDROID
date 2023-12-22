package com.xtree.bet.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.databinding.BtLayoutCarCgResultItemBinding;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class CgBtResultAdapter extends BaseAdapter<BtResult> {
    private List<CgOddLimit> cgOddLimitList;
    public CgBtResultAdapter(Context context, List<CgOddLimit> cgOddLimitList, List<BtResult> datas) {
        super(context, datas);
        this.cgOddLimitList = cgOddLimitList;
    }

    @Override
    public int layoutId() {
        return R.layout.bt_layout_car_cg_result_item;
    }

    @Override
    protected void convert(ViewHolder holder, BtResult btResult, int position) {
        BtLayoutCarCgResultItemBinding binding = BtLayoutCarCgResultItemBinding.bind(holder.itemView);
        CgOddLimit cgOddLimit = cgOddLimitList.get(position);
        if (getItemCount() > 1 || !TextUtils.isEmpty(cgOddLimit.getCgName())) { // 串关
            binding.cslCgDan.setVisibility(View.GONE);
            binding.cslCgCc.setVisibility(View.VISIBLE);
            binding.tvName.setText(cgOddLimit.getCgName());
            binding.ivZsAmount.setText("x" + cgOddLimit.getBtCount());
            binding.tvAmountCc.setText(mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getBtAmount())));
            binding.tvWinCc.setText(mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getWin(cgOddLimit.getBtAmount()))));
            binding.tvResult.setText(btResult.getStatusDesc());
            if(btResult.isSuccessed()){
                binding.tvResult.setTextColor(mContext.getResources().getColor(R.color.bt_color_bt_result_success));
            }else{
                binding.tvResult.setTextColor(mContext.getResources().getColor(R.color.bt_color_bt_result_failed));
            }
        } else {
            binding.cslCgDan.setVisibility(View.VISIBLE);
            binding.cslCgCc.setVisibility(View.GONE);
            binding.tvAmountDan.setText(mContext.getResources().getString(R.string.bt_bt_pay, String.valueOf(cgOddLimit.getBtAmount())));
            binding.tvWinDan.setText(mContext.getResources().getString(R.string.bt_bt_win, String.valueOf(cgOddLimit.getWin(cgOddLimit.getBtAmount()))));
        }
        if(btResult != null) {
            binding.tvBtId.setText(btResult.getId());
        }
    }
}
