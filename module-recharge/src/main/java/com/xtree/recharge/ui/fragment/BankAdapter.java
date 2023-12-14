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
import com.xtree.recharge.databinding.ItemBankBinding;
import com.xtree.recharge.vo.BankCardVo;

public class BankAdapter extends CachedAutoRefreshAdapter<BankCardVo> {
    ItemBankBinding binding;
    Context ctx;
    //<BankCardVo> mList;
    ICallBack mCallBack;

    public interface ICallBack {
        void onClick(BankCardVo vo);
    }

    public BankAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_bank, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        BankCardVo vo = get(position);
        binding = ItemBankBinding.bind(holder.itemView);
        binding.tvwTitle.setText(vo.name);
        binding.tvwTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.i(vo.toString());
                mCallBack.onClick(vo);
            }
        });
    }

}
