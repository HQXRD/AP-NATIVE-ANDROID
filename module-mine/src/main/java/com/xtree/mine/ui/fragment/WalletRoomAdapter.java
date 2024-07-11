package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemWalletRoomBinding;
import com.xtree.mine.vo.GameBalanceVo;

public class WalletRoomAdapter extends CachedAutoRefreshAdapter<GameBalanceVo> {

    Context ctx;
    String curAlias;
    ICallBack mCallBack;

    ItemWalletRoomBinding binding;

    View curRoot;
    View curCheck;

    public interface ICallBack {
        void onClick(GameBalanceVo vo);
    }

    public WalletRoomAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    public WalletRoomAdapter(Context ctx, String curAlias, ICallBack mCallBack) {
        this.ctx = ctx;
        this.curAlias = curAlias;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_wallet_room, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        GameBalanceVo vo = get(position);
        CfLog.d(vo.toString());

        binding = ItemWalletRoomBinding.bind(holder.itemView);
        binding.ivwLogo.setImageLevel(vo.orderId);
        binding.tvwTitle.setText(vo.gameName);
        binding.tvwBalance.setText(vo.balance);
        chooseIcon(vo);

        if (vo.gameAlias.equals(curAlias)) {
            binding.llRoot.setSelected(true);
            binding.ivwCheck.setSelected(true);

            binding.tvwTitle.setTextColor(ctx.getResources().getColor(R.color.clr_white));
            binding.tvwBalance.setTextColor(ctx.getResources().getColor(R.color.clr_white));

            curRoot = binding.llRoot;
            curCheck = binding.ivwCheck;
        } else {
            binding.tvwTitle.setTextColor(ctx.getResources().getColor(R.color.clr_black));
            binding.tvwBalance.setTextColor(ctx.getResources().getColor(R.color.clr_black));

            binding.llRoot.setSelected(false);
            binding.ivwCheck.setSelected(false);
        }

        binding.llRoot.setOnClickListener(v -> {
            if (curRoot != null) {
                curRoot.setSelected(false);
                curCheck.setSelected(false);
            }
            binding.llRoot.setSelected(true);
            binding.ivwCheck.setSelected(true);
            mCallBack.onClick(vo);
        });
    }

    private void chooseIcon(GameBalanceVo vo) {
        switch (vo.orderId) {
            case 1:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_pt);
                break;
            case 2:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_bbin);
                break;
            case 4:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_ag);
                break;
            case 40:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_obgdj);
                break;
            case 20:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_yy);
                break;
            case 32:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_obgqp);
                break;
            case 80:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_wali);
                break;
            default:
                binding.ivwLogo.setImageResource(R.mipmap.me_ic_wlt_center);
                break;
        }
    }
}
