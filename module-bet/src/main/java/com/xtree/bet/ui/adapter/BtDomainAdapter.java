package com.xtree.bet.ui.adapter;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.NumberUtils;
import com.xtree.bet.R;
import com.xtree.bet.databinding.BtLayoutDomainAgentItemBinding;
import com.xtree.bet.weight.DomainChangeDialog;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class BtDomainAdapter extends BaseAdapter<String> {
    private DomainChangeDialog.ICallBack mICallBack;
    private CheckBox mCbAgent;
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
    @Override
    public int layoutId() {
        return R.layout.bt_layout_domain_agent_item;
    }

    public BtDomainAdapter(Context context, List<String> datas, DomainChangeDialog.ICallBack iCallBack, CheckBox checkBox) {
        super(context, datas);
        mICallBack = iCallBack;
        mCbAgent = checkBox;
    }

    @Override
    protected void convert(ViewHolder holder, String domain, int position) {
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);
        int useLinePosition = SPUtils.getInstance().getInt(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);
        CfLog.e("useLinePosition=======" + useLinePosition);
        BtLayoutDomainAgentItemBinding binding = BtLayoutDomainAgentItemBinding.bind(holder.itemView);
        if(isAgent){
            binding.tvAgentChange.setVisibility(View.VISIBLE);
        }else {
            binding.tvAgentChange.setVisibility(useLinePosition != position ? View.VISIBLE : View.INVISIBLE);
        }
        if(position == useLinePosition){
            binding.tvAgentName.setText("当前线路");
        }else {
            binding.tvAgentName.setText("线路" + NumberUtils.int2chineseNum(position + 1));
        }
        binding.tvAgentChange.setOnClickListener(v -> {
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, position);
            mICallBack.onDomainChange(false, true, mCbAgent);
            notifyDataSetChanged();
        });
    }
}
