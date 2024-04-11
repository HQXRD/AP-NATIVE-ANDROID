package com.xtree.recharge.ui.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.ItemRcTypeBinding;
import com.xtree.recharge.vo.PaymentTypeVo;
import com.xtree.recharge.vo.RechargeVo;

public class RechargeTypeAdapter extends CachedAutoRefreshAdapter<PaymentTypeVo> {

    Context ctx;
    ICallBack mCallBack;
    ItemRcTypeBinding binding;

    View curView;
    String curId = "-1"; // 当前选中的充值类型, (解决网络请求的数据回来,选中的项被取消问题)

    public interface ICallBack {
        void onClick(PaymentTypeVo vo);
    }

    public RechargeTypeAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_rc_type, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        PaymentTypeVo vo = get(position);
        CfLog.d(vo.toInfo());
        binding = ItemRcTypeBinding.bind(holder.itemView);
        String url = DomainUtil.getDomain2() + vo.un_selected_image; // 未选中
        //Glide.with(ctx).load(url).placeholder(R.mipmap.dc_bg).into(binding.ivwIcon);

        binding.tvwTitle.setText(vo.dispay_title);
        binding.clRoot.setTag(vo.id); // 弹窗时会用到
        if (vo.id.equals(curId)) {
            curView = binding.clRoot;
            //curView.setSelected(true);
            binding.ivwBg.setSelected(true); // 背景图
            binding.tvwTitle.setSelected(true);
            binding.tvwDepRate.setSelected(true);
            url = DomainUtil.getDomain2() + vo.selected_image; // 选中
        }

        binding.ivwIcon.setTag(vo);
        Glide.with(ctx).load(url).placeholder(R.mipmap.rc_ic_type_cs).into(binding.ivwIcon);
        String depositfee_rate = "0.0";
        for (int i = 0; i < vo.payChannelList.size(); i++) {
            RechargeVo t2 = vo.payChannelList.get(i);
            if (t2.depositfee_disabled && !TextUtils.isEmpty(t2.depositfee_rate)) {
                if (Double.parseDouble(t2.depositfee_rate) > Double.parseDouble(depositfee_rate)) {
                    depositfee_rate = t2.depositfee_rate;
                }
            }
        }

        // 存款加赠5.00%
        //if (vo.depositfee_disabled && !TextUtils.isEmpty(vo.depositfee_rate)) {
        if (Double.parseDouble(depositfee_rate) > 0) {
            //binding.tvwTitle.setText(vo.dispay_title + "\n");
            String txt = String.format(ctx.getString(R.string.txt_deposit_fee_most), depositfee_rate);
            binding.tvwDepRate.setText(txt);
            binding.tvwDepRate.setVisibility(View.VISIBLE);
        } else {
            binding.tvwDepRate.setVisibility(View.GONE);
        }

        int visible = vo.is_hot == 0 ? View.INVISIBLE : View.VISIBLE;
        binding.ivwHot.setVisibility(visible); // HOT
        int visible2 = vo.recommend == 0 ? View.INVISIBLE : View.VISIBLE;
        binding.ivwRcmd.setVisibility(visible2); // 推荐

        binding.clRoot.setOnClickListener(v -> {
            CfLog.e(vo.toInfo());

            if (curView != null) {
                //curView.setSelected(false);
                setItemStatus(curView, false);
            }

            //v.setSelected(true);
            curView = v;
            curId = vo.id;
            setItemStatus(v, true);
            mCallBack.onClick(vo);
        });
    }

    private void setItemStatus(View view, boolean isSelected) {
        CfLog.i("****** view is null ? " + (view == null));
        if (view == null) {
            return;
        }
        ItemRcTypeBinding binding2 = ItemRcTypeBinding.bind(view);

        PaymentTypeVo vo = (PaymentTypeVo) binding2.ivwIcon.getTag();
        String url = isSelected ? vo.selected_image : vo.un_selected_image;
        url = DomainUtil.getDomain2() + url; // 选中/未选中

        binding2.ivwBg.setSelected(isSelected);
        binding2.tvwTitle.setSelected(isSelected);
        binding2.tvwDepRate.setSelected(isSelected);
        Glide.with(ctx).load(url).placeholder(R.mipmap.rc_ic_type_cs).into(binding2.ivwIcon);
    }

}
