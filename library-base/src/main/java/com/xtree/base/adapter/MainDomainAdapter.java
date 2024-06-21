package com.xtree.base.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.xtree.base.R;
import com.xtree.base.databinding.MainLayoutDomainAgentItemBinding;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.NumberUtils;
import com.xtree.base.vo.TopSpeedDomain;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class MainDomainAdapter extends BaseAdapter<TopSpeedDomain> {
    private boolean mChecking = true;
    private boolean mFailed = false;
    private List<TopSpeedDomain> tmpTopSpeedDomainList = new ArrayList<>();

    public void setChecking(boolean isChecking) {
        mChecking = isChecking;
    }

    public void setFailed(boolean isFailed) {
        mFailed = isFailed;
    }

    @Override
    public int layoutId() {
        return R.layout.main_layout_domain_agent_item;
    }

    public MainDomainAdapter(Context context, List<TopSpeedDomain> datas) {
        super(context, datas);
        tmpTopSpeedDomainList.addAll(datas);
    }

    @Override
    protected void convert(ViewHolder holder, TopSpeedDomain domain, int position) {
        MainLayoutDomainAgentItemBinding binding = MainLayoutDomainAgentItemBinding.bind(holder.itemView);

        if (TextUtils.equals(domain.url, DomainUtil.getApiUrl())) {
            binding.tvAgentName.setText("当前线路");
        } else {
            binding.tvAgentName.setText("线路" + NumberUtils.int2chineseNum(position + 1));
        }
        if(mChecking){
            binding.tvAgentChange.setVisibility(View.INVISIBLE);
            binding.tvSpeed.setText("");
            binding.tvRecomment.setText("测速中...");
            binding.tvRecomment.setVisibility(View.VISIBLE);
        }else if(mFailed){
            binding.tvAgentChange.setVisibility(View.INVISIBLE);
            binding.tvSpeed.setText("");
            binding.tvRecomment.setText("测速失败");
            binding.tvRecomment.setVisibility(View.VISIBLE);
        }else{
            binding.tvAgentChange.setVisibility(!TextUtils.equals(domain.url, DomainUtil.getApiUrl()) ? View.VISIBLE : View.INVISIBLE);
            binding.tvRecomment.setText("推荐");
            binding.tvSpeed.setText(domain.speedSec + "ms");

            if (domain.speedSec < 200) {
                binding.tvAgentChange.setTextColor(mContext.getResources().getColor(R.color.clr_green_03));
                binding.tvAgentChange.setBackground(mContext.getResources().getDrawable(R.drawable.bg_stroke_green));
                binding.ivRedPoint.setBackground(mContext.getResources().getDrawable(R.drawable.ic_green_point));
                binding.tvRecomment.setVisibility(View.VISIBLE);
            } else if (domain.speedSec > 200 && domain.speedSec < 500) {
                binding.tvAgentChange.setTextColor(mContext.getResources().getColor(R.color.clr_orange_02));
                binding.tvAgentChange.setBackground(mContext.getResources().getDrawable(R.drawable.bg_stroke_orange));
                binding.ivRedPoint.setBackground(mContext.getResources().getDrawable(R.drawable.ic_orange_point));
                binding.tvRecomment.setVisibility(View.VISIBLE);
            } else {
                binding.tvAgentChange.setTextColor(mContext.getResources().getColor(R.color.clr_red_02));
                binding.tvAgentChange.setBackground(mContext.getResources().getDrawable(R.drawable.bg_stroke_red));
                binding.ivRedPoint.setBackground(mContext.getResources().getDrawable(R.drawable.ic_red_point));
                binding.tvRecomment.setVisibility(View.INVISIBLE);
            }
        }

        binding.tvAgentChange.setOnClickListener(v -> {
            DomainUtil.setApiUrl(domain.url);
            ToastUtils.showLong("切换线路成功");
            Activity activity = AppManager.getAppManager().currentActivity();
            activity.startActivity(new Intent(activity, activity.getClass()));
            notifyDataSetChanged();
        });
    }
}
