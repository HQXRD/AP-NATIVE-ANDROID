package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemBonusReportBinding;
import com.xtree.mine.vo.BounsReportItemVo;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BonusAdapter extends CachedAutoRefreshAdapter<BounsReportItemVo> {
    private Context ctx;
    private int level;

    public BonusAdapter(Context context) {
        ctx = context;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_bonus_report, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        ItemBonusReportBinding binding = ItemBonusReportBinding.bind(holder.itemView);
        BounsReportItemVo vo = get(position);

        level = SPUtils.getInstance().getInt(SPKeyGlobal.USER_LEVEL);
        if (level > 2) {
            binding.tvwItemUnder.setVisibility(View.GONE);
            binding.tvwUnderTitle.setVisibility(View.GONE);
        }

        binding.tvwItemDate.setText(vo.date);
        binding.tvwItemUser.setText(vo.tousername);
        binding.tvwItemServerFee.setText(vo.radio);
        binding.tvwItemOwn.setText(vo.myselfprice);
        binding.tvwItemActive.setText(vo.activenum);
        binding.tvwItemServer.setText(vo.realbonus);
        binding.tvwItemUnder.setText(vo.childprice);

        if (vo.status.equals("1")) {
            binding.tvwStatus.setText("已发放");
            binding.tvwStatus.setTextColor(ctx.getColor(R.color.clr_green_01));
        //} else if (vo.status.equals("2")) {
        //    binding.tvwStatus.setText("无抽水");
        //    binding.tvwStatus.setTextColor(ctx.getColor(R.color.clr_green_01));
        } else if (vo.status.equals("-1")) {
            binding.tvwStatus.setText("未发放");
            binding.tvwStatus.setTextColor(ctx.getColor(R.color.clr_red_01));
        }

        if (vo.childprice.equals("0.0000") && vo.myselfprice.equals("0.0000")) {
            binding.tvwStatus.setText("无抽水");
            binding.tvwStatus.setTextColor(ctx.getColor(R.color.clr_green_01));
        }

    }
}
