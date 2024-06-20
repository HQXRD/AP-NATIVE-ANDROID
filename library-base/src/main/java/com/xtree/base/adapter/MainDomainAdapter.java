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

public class MainDomainAdapter extends BaseAdapter<TopSpeedDomain> {
    private boolean mChecking = true;
    private List<TopSpeedDomain> tmpTopSpeedDomainList = new ArrayList<>();
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
        }else{
            binding.tvAgentChange.setVisibility(!TextUtils.equals(domain.url, DomainUtil.getApiUrl()) ? View.VISIBLE : View.INVISIBLE);
            binding.tvRecomment.setText("推荐");
            binding.tvSpeed.setText(String.valueOf(domain.speedSec));
        }

        binding.tvAgentChange.setOnClickListener(v -> {
            DomainUtil.setApiUrl(domain.url);
            Activity activity = AppManager.getAppManager().currentActivity();
            activity.startActivity(new Intent(activity, activity.getClass()));
            notifyDataSetChanged();
        });
    }

    public void setIsChecking(boolean isChecking) {
        mChecking = isChecking;
    }
}
