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
import com.xtree.mine.databinding.ItemVipBinding;
import com.xtree.mine.vo.VipUpgradeItemVo;

public class VipInfoAdapter extends CachedAutoRefreshAdapter<VipUpgradeItemVo> {
    Context ctx;
    ItemVipBinding binding;
    int level;
    String sp;

    public VipInfoAdapter(Context ctx, int level, String sp) {
        this.ctx = ctx;
        this.level = level;
        this.sp = sp;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_vip, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        VipUpgradeItemVo vo = get(position);

        binding = ItemVipBinding.bind(holder.itemView);

        CfLog.e("position : " + position);

        if (position != 0) {
            binding.llTitle.setVisibility(View.GONE);
        }
        if (sp.equals("0")) {
            binding.tvwVipLevel1.setText("VIP" + vo.level);
        } else {
            binding.tvwVipLevel1.setText("VIP" + vo.display_level);
        }
        binding.tvwVipSport1.setText(vo.sports_ratio + "%");
        binding.tvwVipReal1.setText(vo.esport_ratio + "%");
        binding.tvwVipElectronic1.setText(vo.esport_ratio + "%");
        binding.tvwVipGaming1.setText(vo.gaming_ratio + "%");
        binding.tvwVipPorker1.setText(vo.chess_ratio + "%");
        binding.tvwVipLottery1.setText(vo.lottery_ratio + "%");
        binding.tvwVipFishing1.setText(vo.esport_ratio + "%");

        if (position == level) {
            setBackground(ctx.getResources().getColor(R.color.clr_main), ctx.getResources().getColor(R.color.clr_main_04));
        } else if (position % 2 == 1) {
            setBackground(ctx.getResources().getColor(R.color.clr_gray_4), ctx.getResources().getColor(R.color.clr_white));
        } else {
            setBackground(ctx.getResources().getColor(R.color.clr_gray_5), ctx.getResources().getColor(R.color.clr_gray_4));
        }
    }

    private void setBackground(int vipLevel, int venue) {
        binding.tvwVipLevel1.setBackgroundColor(vipLevel);
        binding.tvwVipSport1.setBackgroundColor(venue);
        binding.tvwVipReal1.setBackgroundColor(venue);
        binding.tvwVipElectronic1.setBackgroundColor(venue);
        binding.tvwVipGaming1.setBackgroundColor(venue);
        binding.tvwVipPorker1.setBackgroundColor(venue);
        binding.tvwVipLottery1.setBackgroundColor(venue);
        binding.tvwVipFishing1.setBackgroundColor(venue);
    }
}
