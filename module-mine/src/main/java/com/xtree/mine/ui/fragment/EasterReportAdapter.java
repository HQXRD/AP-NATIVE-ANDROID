package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemEasterReportBinding;
import com.xtree.mine.ui.dialog.MemberDialog;
import com.xtree.mine.vo.EasterReportItemVo;

public class EasterReportAdapter extends CachedAutoRefreshAdapter<EasterReportItemVo> {
    Context ctx;
    ItemEasterReportBinding binding;
    ICallBack mCallBack;
    int maxNum = 17;

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

        binding.tvwDate.setText(vo.time);
        binding.tvwName.setText(vo.username);
        binding.tvwTime.setText(vo.recordsCount);
        binding.tvwReturnMoney.setText(vo.total_sum);
        if (vo.user_str.isEmpty()) {
            binding.tvwTouchMember.setText("--");
        } else if (vo.user_str.contains(",")) {
            if (vo.user_str.length() > maxNum) {
                String detailString = vo.user_str.substring(0, maxNum) + "..>>";
                binding.tvwTouchMember.setText(detailString);
            } else {
                binding.tvwTouchMember.setText(vo.user_str + "..>>");
            }
            binding.tvwTouchMember.setOnClickListener(v -> {
                new XPopup.Builder(ctx).asCustom(new MemberDialog(ctx, vo.username, vo.user_str)).show();
            });
        } else {
            binding.tvwTouchMember.setText(vo.user_str);
        }
    }
}
