package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemMsgInfoBinding;
import com.xtree.mine.vo.MsgPersonVo;

public class MsgPersonListAdapter extends CachedAutoRefreshAdapter<MsgPersonVo> {
    Context ctx;
    ItemMsgInfoBinding binding;
    ICallBack mCallBack;

    // 两次点击之间的最小点击间隔时间(单位:ms)
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    // 最后一次点击的时间
    private long lastClickTime;

    public interface ICallBack {
        void onClick(MsgPersonVo vo);

        void onEnable(Boolean enabled);
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

        binding.ckbMsgCheck.setChecked(vo.selected_delete);
        binding.tvwMsgTitle.setText(vo.title);
        binding.tvwMsgDate.setText(vo.sent_at);

        binding.clItem.setOnClickListener(v ->
        { // 限制多次点击
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime < MIN_CLICK_DELAY_TIME) {// 两次点击的时间间隔小于最小限制时间，不触发点击事件
                return;
            }
            lastClickTime = currentTime;
            mCallBack.onClick(vo);
        });

        binding.ckbMsgCheck.setOnClickListener(view -> {
            CfLog.e("vo.selected_delete : " + vo.selected_delete);
            vo.selected_delete = !vo.selected_delete;
            for (MsgPersonVo item : getData()) {
                if (!item.selected_delete) {
                    mCallBack.onEnable(false);
                    return;
                }
            }
            mCallBack.onEnable(true);
        });
    }
}