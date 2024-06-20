package com.xtree.main.ui.adapter;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
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

    @Override
    public int layoutId() {
        return R.layout.main_layout_domain_agent_item;
    }

    public MainDomainAdapter(Context context, List<TopSpeedDomain> datas) {
        super(context, datas);
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
        binding.tvAgentChange.setOnClickListener(v -> {
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_LINE_POSITION, position);
            DomainUtil.setApiUrl(domain.url);
            Activity activity = AppManager.getAppManager().currentActivity();
            activity.startActivity(new Intent(activity, activity.getClass()));
            notifyDataSetChanged();
        });
    }
}
