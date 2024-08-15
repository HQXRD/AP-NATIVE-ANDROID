package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.ClickUtil;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemMsgInfoBinding;
import com.xtree.mine.databinding.ItemTeamActivityBinding;
import com.xtree.mine.vo.MsgVo;
import com.xtree.mine.vo.TeamActivityReportVo;

public class TeamActivityListAdapter extends CachedAutoRefreshAdapter<TeamActivityReportVo> {
    Context ctx;
    ItemTeamActivityBinding binding;


    public TeamActivityListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_team_activity, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        TeamActivityReportVo vo = getData().get(position);

        binding = ItemTeamActivityBinding.bind(holder.itemView);
        binding.tvMonth.setText(vo.month);
        binding.tvCondition.setText(vo.condition);
        binding.tvMoney.setText(vo.money+"(RMB)");
        if (TextUtils.equals("0",vo.status)){
            binding.tvStatus.setText("未发送");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvStatus.setTextColor(ctx.getColor(R.color.red));
            }
        }else{
            binding.tvStatus.setText("已发送");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.tvStatus.setTextColor(ctx.getColor(R.color.red));
            }
        }
        binding.tvCreatedAt.setText(vo.created_at);
    }
}