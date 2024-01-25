package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemMsgInfoBinding;
import com.xtree.mine.vo.MsgPersonVo;

public class MsgPersonListAdapter extends CachedAutoRefreshAdapter<MsgPersonVo> {
    Context ctx;
    ItemMsgInfoBinding binding;
    ICallBack mCallBack;

    public interface ICallBack {
        void onClick(MsgPersonVo vo);
    }

    public MsgPersonListAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_msg_info, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        MsgPersonVo vo = getData().get(position);

        binding = ItemMsgInfoBinding.bind(holder.itemView);

        binding.msgCheck.setVisibility(View.GONE);
        binding.msgTitle.setText(vo.title);
        binding.msgDate.setText(vo.sent_at);

        binding.clItem.setOnClickListener(v -> mCallBack.onClick(vo));
    }
}