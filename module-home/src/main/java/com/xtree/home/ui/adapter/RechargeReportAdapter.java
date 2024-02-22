package com.xtree.home.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView itemMoney = holder.itemView.findViewById(R.id.item_money);
        TextView itemWay = holder.itemView.findViewById(R.id.item_way);
        TextView itemTime = holder.itemView.findViewById(R.id.item_time);

        itemMoney.setText(vo.money);
        itemWay.setText(vo.payport_nickname);
        itemTime.setText(vo.timeout);

        if (position % 2 == 1) {
            itemMoney.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white, ctx.getResources().newTheme()));
            itemWay.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white, ctx.getResources().newTheme()));
            itemTime.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white, ctx.getResources().newTheme()));
        } else {
            itemMoney.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple, ctx.getResources().newTheme()));
            itemWay.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple, ctx.getResources().newTheme()));
            itemTime.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple, ctx.getResources().newTheme()));
        }
    }
}