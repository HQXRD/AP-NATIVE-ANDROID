package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemMyWalletBinding;
import com.xtree.mine.vo.GameBalanceVo;

public class MyWalletAdapter extends CachedAutoRefreshAdapter<GameBalanceVo> {
    Context ctx;
    ItemMyWalletBinding binding;

    public MyWalletAdapter(Context context) {
        ctx = context;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_my_wallet, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        GameBalanceVo vo = getData().get(position);

        binding = ItemMyWalletBinding.bind(holder.itemView);

        binding.tvwThirdGameName.setText(vo.gameName);
        binding.tvwThirdGameBalance.setText(vo.balance);

        if ((position + 1) % 3 == 0) {
            binding.tvwSpace.setVisibility(View.GONE);
        }

        CfLog.i("vo.balance : " + vo.balance);
        if (vo.balance.matches("\\d+\\.\\d{4}")) {
            binding.llMain.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_my_wallet_item));
            binding.tvwThirdGameBalance.setTextSize(18);
        } else {
            binding.llMain.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_gray_8));
            binding.tvwThirdGameBalance.setTextSize(10);
        }
    }
}
