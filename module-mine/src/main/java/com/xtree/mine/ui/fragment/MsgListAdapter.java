package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.ClickUtil;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemMsgInfoBinding;
import com.xtree.mine.vo.MsgVo;

public class MsgListAdapter extends CachedAutoRefreshAdapter<MsgVo> {
    Context ctx;
    ItemMsgInfoBinding binding;
    ICallBack mCallBack;

    public interface ICallBack {
        void onClick(MsgVo vo);
    }

    public MsgListAdapter(Context ctx, ICallBack callBack) {
        this.ctx = ctx;
        this.mCallBack = callBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_msg_info, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        MsgVo vo = getData().get(position);

        binding = ItemMsgInfoBinding.bind(holder.itemView);

        binding.ckbMsgCheck.setVisibility(View.GONE);
        binding.tvwMsgTitle.setText(vo.title);
        binding.tvwMsgDate.setText(vo.created_at);

        if (vo.is_read) {
            binding.ivwMsgIcon.setImageResource(R.mipmap.ic_round_dot);
        } else {
            binding.ivwMsgIcon.setImageResource(R.mipmap.ic_round);
        }

        binding.clItem.setOnClickListener(v ->
        { // 限制多次快速点击
            if (ClickUtil.isFastClick()) {
                return;
            }
            mCallBack.onClick(vo);
        });
    }
}