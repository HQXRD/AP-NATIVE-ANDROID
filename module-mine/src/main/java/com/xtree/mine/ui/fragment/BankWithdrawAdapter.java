package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemBankWithdrawBinding;

public class BankWithdrawAdapter extends CachedAutoRefreshAdapter<ProfileVo.CardInfoVo> {

    Context ctx;
    ICallBack mCallBack;

    ItemBankWithdrawBinding binding;
    View curRoot;
    View curCheck;

    public interface ICallBack {
        void onClick(ProfileVo.CardInfoVo vo);
    }

    public BankWithdrawAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_bank_withdraw, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        ProfileVo.CardInfoVo vo = get(position);
        binding = ItemBankWithdrawBinding.bind(holder.itemView);

        if (TextUtils.isEmpty(vo.account)) {
            binding.llRoot.setBackgroundResource(R.mipmap.cm_ic_add_more_card);
            binding.llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CfLog.i("****** add more...");
                }
            });

            return;
        }

        binding.tvwTitle.setText(vo.bank_name);
        binding.tvwAccount.setText(vo.account);
        binding.llRoot.setBackgroundResource(R.mipmap.cm_ic_wd_card);
        binding.llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.i("******");

                if (curRoot != null) {
                    curRoot.setSelected(false);
                }

                curRoot = v;
                v.setSelected(true);

                mCallBack.onClick(vo);
            }
        });

    }

}
