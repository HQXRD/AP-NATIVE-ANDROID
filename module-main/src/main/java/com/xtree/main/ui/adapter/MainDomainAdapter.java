package com.xtree.main.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.vo.TopSpeedDomain;
import com.xtree.main.R;
import com.xtree.main.databinding.MainLayoutDomainAgentItemBinding;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.utils.SPUtils;

public class MainDomainAdapter extends BaseAdapter<TopSpeedDomain> {
    private Context context;

    @Override
    public int layoutId() {
        return R.layout.main_layout_domain_agent_item;
    }

    public MainDomainAdapter(Context context, List<TopSpeedDomain> datas) {
        super(context, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, TopSpeedDomain domain, int position) {
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT);
        int useLinePosition = SPUtils.getInstance().getInt(SPKeyGlobal.KEY_USE_LINE_POSITION, 0);
        MainLayoutDomainAgentItemBinding binding = MainLayoutDomainAgentItemBinding.bind(holder.itemView);
        if (isAgent) {
            binding.tvAgentChange.setVisibility(View.VISIBLE);
        } else {
            binding.tvAgentChange.setVisibility(useLinePosition != position ? View.VISIBLE : View.INVISIBLE);
        }
        if (position == useLinePosition) {
            binding.tvAgentName.setText("当前线路");
        } else {
            binding.tvAgentName.setText("线路" + NumberUtils.int2chineseNum(position + 1));
        }
        binding.tvSpeed.setText(String.valueOf(domain.speedSec));

        if (domain.speedSec < 200) {
            binding.tvAgentChange.setTextColor(context.getResources().getColor(R.color.clr_green_03));
            binding.tvAgentChange.setBackground(context.getResources().getDrawable(R.drawable.bg_stroke_green));
            binding.ivRedPoint.setBackground(context.getResources().getDrawable(R.drawable.ic_green_point));
            binding.tvRecomment.setVisibility(View.VISIBLE);
        } else if (domain.speedSec > 200 && domain.speedSec < 500) {
            binding.tvAgentChange.setTextColor(context.getResources().getColor(R.color.clr_orange_02));
            binding.tvAgentChange.setBackground(context.getResources().getDrawable(R.drawable.bg_stroke_orange));
            binding.ivRedPoint.setBackground(context.getResources().getDrawable(R.drawable.ic_orange_point));
            binding.tvRecomment.setVisibility(View.VISIBLE);
        } else {
            binding.tvAgentChange.setTextColor(context.getResources().getColor(R.color.clr_red_02));
            binding.tvAgentChange.setBackground(context.getResources().getDrawable(R.drawable.bg_stroke_red));
            binding.ivRedPoint.setBackground(context.getResources().getDrawable(R.drawable.ic_red_point));
            binding.tvRecomment.setVisibility(View.INVISIBLE);
        }

        binding.tvAgentChange.setOnClickListener(v -> {
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_LINE_POSITION, position);
            DomainUtil.setApiUrl(domain.url);
            Activity activity = AppManager.getAppManager().currentActivity();
            activity.startActivity(new Intent(activity, activity.getClass()));
            notifyDataSetChanged();
        });
    }
}
