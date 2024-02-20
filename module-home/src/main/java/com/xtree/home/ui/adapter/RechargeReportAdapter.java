package com.xtree.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.home.R;
import com.xtree.home.databinding.ItemRechargeServiceBinding;
import com.xtree.home.vo.RechargeOrderVo;

public class RechargeReportAdapter extends CachedAutoRefreshAdapter<RechargeOrderVo> {
    Context ctx;
    ItemRechargeServiceBinding binding;

    public RechargeReportAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_recharge_service, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        RechargeOrderVo vo = get(position);
        binding = ItemRechargeServiceBinding.bind(holder.itemView);

        binding.itemMoney.setText(vo.money);
        binding.itemWay.setText(vo.payport_nickname);
        binding.itemTime.setText(vo.timeout);

        if (position % 2 == 1) {
            binding.itemMoney.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white));
            binding.itemWay.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white));
            binding.itemTime.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white));
        } else {
            binding.itemMoney.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple));
            binding.itemWay.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple));
            binding.itemTime.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple));
        }
    }
}