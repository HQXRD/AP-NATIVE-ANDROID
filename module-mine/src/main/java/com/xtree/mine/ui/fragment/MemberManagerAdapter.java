package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ItemMemberManagerBinding;
import com.xtree.mine.vo.MemberUserInfoVo;

import me.xtree.mvvmhabit.utils.SPUtils;

public class MemberManagerAdapter extends CachedAutoRefreshAdapter<MemberUserInfoVo> {
    public static final String BAT_RECORD = "bet_record";
    public static final String ACCOUNT_RECORD = "account_record";
    public static final String TRANSFER_MEMBER = "transfer_member";
    Context ctx;
    ItemMemberManagerBinding binding;
    ICallBack mCallBack;
    boolean isShow;
    ProfileVo mProfileVo;

    public interface ICallBack {
        void onClick(MemberUserInfoVo vo, String msg);

        void onSearch(String name);
    }

    public MemberManagerAdapter(Context context, ICallBack callBack) {
        ctx = context;
        mCallBack = callBack;
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_member_manager, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        MemberUserInfoVo vo = get(position);
        binding = ItemMemberManagerBinding.bind(holder.itemView);

        if (vo.userid.equals(SPUtils.getInstance().getString(SPKeyGlobal.USER_ID))) {
            binding.tvwSomeoneSelf.setVisibility(View.VISIBLE);
            binding.btnTransfor.setVisibility(View.GONE);
        } else {
            binding.tvwSomeoneSelf.setVisibility(View.GONE);
        }

        if (isShow && vo.recharge) {
            binding.btnTransfor.setVisibility(View.VISIBLE);
        }

        binding.tvwUserName.setText(vo.username);
        if (vo.userpoint != null) {
            binding.tvwReturnPoint.setText((Double.parseDouble(vo.userpoint) * 100) + "%");
            binding.tvwUserName.setEnabled(true); //名字不可点击
        } else {
            //binding.tvwReturnPoint.setText("0%");
            binding.tvwReturnPoint.setText(mProfileVo.rebate_percentage);
            binding.tvwUserName.setEnabled(false); // 名字可点击
        }
        binding.tvwMemberNum.setText(vo.children_num);
        binding.tvwUserBalance.setText(vo.availablebalance);
        binding.tvwMemberBalance.setText(vo.team_balance);
        binding.tvwRegisterTime.setText(vo.registertime);
        binding.tvwLastTime.setText(vo.lasttime);

        binding.btnBet.setOnClickListener(v -> {
            mCallBack.onClick(vo, BAT_RECORD);
        });

        binding.btnAccount.setOnClickListener(v -> {
            mCallBack.onClick(vo, ACCOUNT_RECORD);
        });

        binding.btnTransfor.setOnClickListener(v -> {
            mCallBack.onClick(vo, TRANSFER_MEMBER);
        });

        binding.tvwUserName.setOnClickListener(v -> {
            CfLog.d("onSearch");
            mCallBack.onSearch(vo.username);
        });
    }
}
