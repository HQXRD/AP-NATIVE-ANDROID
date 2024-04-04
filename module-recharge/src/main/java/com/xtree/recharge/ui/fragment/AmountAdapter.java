package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.ItemAmountBinding;

public class AmountAdapter extends CachedAutoRefreshAdapter<String> {

    Context ctx;
    ICallBack mCallBack;
    ItemAmountBinding binding;

    View curView;

    public interface ICallBack {
        void onClick(String str);
    }

    public AmountAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_amount, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        String str = get(position);
        binding = ItemAmountBinding.bind(holder.itemView);
        binding.tvwTitle.setText(str);
        binding.tvwTitle.setSelected(false); // 默认不选中
        binding.tvwTitle.setOnClickListener(v -> {
            CfLog.i(str);
            if (curView != null) {
                curView.setSelected(false);
            }
            v.setSelected(true);
            curView = v;
            mCallBack.onClick(str);
        });
    }
}
