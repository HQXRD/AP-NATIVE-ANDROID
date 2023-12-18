package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.ItemRechargeBinding;
import com.xtree.recharge.vo.RechargeVo;

public class RechargeAdapter extends CachedAutoRefreshAdapter<RechargeVo> {

    Context ctx;

    ICallBack mCallBack;

    ItemRechargeBinding binding;

    View curView;

    public interface ICallBack {
        void onClick(RechargeVo vo);
    }

    public RechargeAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_recharge, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {

        RechargeVo vo = get(position);
        CfLog.d(vo.toInfo());
        binding = ItemRechargeBinding.bind(holder.itemView);
        binding.tvwTitle.setText(vo.title);

        // 存款加赠5.00%
        if (vo.depositfee_disabled && !TextUtils.isEmpty(vo.depositfee_rate)) {
            binding.tvwTitle.setText(vo.title + "\n");
            String txt = String.format(ctx.getString(R.string.txt_deposit_fee), vo.depositfee_rate);
            binding.tvwDepRate.setText(txt);
            binding.tvwDepRate.setVisibility(View.VISIBLE);
        } else {
            binding.tvwDepRate.setVisibility(View.GONE);
        }

        int visible = vo.firemark == 0 ? View.INVISIBLE : View.VISIBLE;
        binding.ivwHot.setVisibility(visible); // HOT
        int visible2 = vo.recommend == 0 ? View.INVISIBLE : View.VISIBLE;
        binding.ivwRcmd.setVisibility(visible2); // 推荐
        //binding.tvwTitle.setCompoundDrawables();
        Drawable dr = ctx.getDrawable(R.drawable.rc_ic_pmt_selector);
        dr.setLevel(Integer.parseInt(vo.bid));
        binding.tvwTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, dr, null, null);

        binding.tvwTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (curView != null) {
                    curView.setSelected(false);
                }

                v.setSelected(true);
                curView = v;
                mCallBack.onClick(vo);
            }
        });
    }

}
