package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.ItemRcChannelBinding;
import com.xtree.recharge.vo.RechargeVo;

public class RechargeChannelAdapter extends CachedAutoRefreshAdapter<RechargeVo> {

    Context ctx;
    View curView;
    String id;
    ItemRcChannelBinding binding;
    ICallBack mCallBack;

    public interface ICallBack {
        void onClick(RechargeVo vo);
    }

    public RechargeChannelAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @Override
    public void clear() {
        CfLog.i("******");
        super.clear();
        //curView = null;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_rc_channel, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        RechargeVo vo = get(position);
        binding = ItemRcChannelBinding.bind(holder.itemView);
        binding.tvwTitle.setText(vo.title);
        binding.tvwTitle.setSelected(false); // 默认不选中
        binding.tvwTitle.setTag(vo.bid); // 推荐用

        if (!TextUtils.isEmpty(vo.depositfee_rate) && Double.parseDouble(vo.depositfee_rate) > 0) {
            String txt = String.format(ctx.getString(R.string.txt_deposit_fee), vo.depositfee_rate);
            binding.tvwDepRate.setText(txt);
            binding.tvwDepRate.setVisibility(View.VISIBLE);
        } else {
            binding.tvwDepRate.setVisibility(View.INVISIBLE);
        }

        binding.tvwTitle.setOnClickListener(v -> {
            CfLog.i(vo.toInfo());
            //curRechargeVo = vo;
            if (curView != null) {
                curView.setSelected(false);
            }
            v.setSelected(true);
            curView = v;
            id = vo.bid; // 处理有时会选中多个选项 (推荐过来的)

            mCallBack.onClick(vo);
            //onClickPayment(vo);
        });
    }

}
