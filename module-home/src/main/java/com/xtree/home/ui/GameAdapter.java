package com.xtree.home.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.home.R;
import com.xtree.home.databinding.HmItemGameBinding;
import com.xtree.home.vo.GameVo;

public class GameAdapter extends CachedAutoRefreshAdapter<GameVo> {
    Context ctx;
    HmItemGameBinding binding;

    public GameAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.hm_item_game, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        GameVo vo = get(position);
        binding = HmItemGameBinding.bind(holder.itemView);
        binding.ivwImg.setImageLevel(vo.typeId);
        binding.ivwImg.setOnClickListener(view -> jump(vo));

        if (vo.status == 0) {
            String txt = ctx.getString(R.string.hm_txt_maintaining, vo.maintenance_start, vo.maintenance_end);
            binding.tvwMaintenance.setText(txt);
            binding.tvwMaintenance.setVisibility(View.VISIBLE);
            binding.ivwGreyCover.setVisibility(View.VISIBLE);
        } else if (vo.status == 2) {
            binding.tvwMaintenance.setText("已下架");
            binding.tvwMaintenance.setVisibility(View.VISIBLE);
            binding.ivwGreyCover.setVisibility(View.VISIBLE);
        } else {
            binding.tvwMaintenance.setVisibility(View.GONE);
            binding.ivwGreyCover.setVisibility(View.GONE);
        }
    }

    private void jump(GameVo vo) {
        CfLog.i(vo.toString());
        if (vo.isH5) {

            if (TextUtils.isEmpty(vo.playURL)) {
                // 去请求网络接口
                CfLog.i("去请求网络接口");
            } else {
                // 拼装URL
                CfLog.i("拼装URL");
            }

        } else {
            // 跳原生
            CfLog.i("跳原生");
        }

    }

}
