package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
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
    }
}
