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
    String mAmount = ""; // 当前选中的金额,或者输入框输入的金额 (设置某个金额为选中状态时使用)

    public interface ICallBack {
        void onClick(String str);
    }

    public AmountAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getAmount() {
        return mAmount;
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
        binding.tvwTitle.setText(str + ctx.getString(R.string.txt_yuan));
        binding.tvwTitle.setSelected(false); // 默认不选中
        if (str.equals(mAmount)) {
            binding.tvwTitle.setSelected(true);
        }

        binding.tvwTitle.setOnClickListener(v -> {
            CfLog.i(str);
            if (curView != null) {
                curView.setSelected(false);
            }
            mAmount = str;
            v.setSelected(true);
            curView = v;
            mCallBack.onClick(str);
        });
    }
}
