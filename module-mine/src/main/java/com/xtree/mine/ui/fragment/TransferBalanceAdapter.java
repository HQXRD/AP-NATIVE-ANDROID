package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemGameBalanceBinding;
import com.xtree.mine.vo.GameBalanceVo;

public class TransferBalanceAdapter extends CachedAutoRefreshAdapter<GameBalanceVo> {
    Context ctx;
    ItemGameBalanceBinding binding;

    public TransferBalanceAdapter(Context context) {
        ctx = context;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_game_balance, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        GameBalanceVo vo = get(position);

        binding = ItemGameBalanceBinding.bind(holder.itemView);

        CfLog.d("TransferBalanceAdapter : " + position);

        binding.tvwName.setText(vo.gameName);
        binding.tvwBlc.setText(vo.balance);

        if (Double.parseDouble(vo.balance) > 0) {
            binding.tvwBlc.setSelected(true);
        } else {
            binding.tvwBlc.setSelected(false); // 这里是为了颜色
        }

        if ((position + 1) % 3 == 0) {
            binding.ivLine.setVisibility(View.GONE);
        } else {
            binding.ivLine.setVisibility(View.VISIBLE);
        }
    }
}
