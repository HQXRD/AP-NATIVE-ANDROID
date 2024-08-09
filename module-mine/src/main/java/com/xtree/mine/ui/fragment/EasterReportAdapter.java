package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemEasterReportBinding;
import com.xtree.mine.vo.EasterReportItemVo;

public class EasterReportAdapter extends CachedAutoRefreshAdapter<EasterReportItemVo> {
    Context ctx;
    ItemEasterReportBinding binding;
    ICallBack mCallBack;

    public interface ICallBack {
        void onClick(String member);
    }

    public EasterReportAdapter(Context context, ICallBack callBack) {
        ctx = context;
        mCallBack = callBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_easter_report, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        EasterReportItemVo vo = get(position);
        binding = ItemEasterReportBinding.bind(holder.itemView);

        binding.tvwDate.setText(vo.updatetime);
        if (vo.status == 0) {
            binding.tvwStatus.setText(ctx.getString(R.string.txt_not_give_lottery));
            binding.tvwStatus.setTextColor(ctx.getResources().getColor(R.color.clr_green_01));
        } else if (vo.status == 1) {
            binding.tvwStatus.setText(ctx.getString(R.string.txt_give_lottery));
            binding.tvwStatus.setTextColor(ctx.getResources().getColor(R.color.clr_green_01));
        } else if (vo.status == 2) {
            binding.tvwStatus.setText(ctx.getString(R.string.txt_cancel_lottery));
            binding.tvwStatus.setTextColor(ctx.getResources().getColor(R.color.textColorHint));
        } else {
            binding.tvwStatus.setText("");
        }

        binding.tvwLottery.setText(vo.lottery);
        binding.tvwReturnMoney.setText(vo.rebate);
        binding.tvwOpenNumber.setText(vo.code);
        binding.tvwEasterNumber.setText(vo.eastereggcode);
        binding.tvwName.setText(vo.tousername);
        binding.tvwMemberName.setText(vo.fromusername);

        binding.tvwMemberName.setOnClickListener(v -> mCallBack.onClick(vo.fromusername));
    }
}
