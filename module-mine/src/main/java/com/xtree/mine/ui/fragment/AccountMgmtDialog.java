package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.vo.ProfileVo;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogAccountMgmtBinding;

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 账户管理 底部弹窗
 */
public class AccountMgmtDialog extends BottomPopupView {

    Context ctx;
    DialogAccountMgmtBinding binding;

    public AccountMgmtDialog(@NonNull Context context) {
        super(context);
        ctx = context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = DialogAccountMgmtBinding.bind(findViewById(R.id.cl_root));
        initView();
    }

    private void initView() {
        binding.ivwClose.setOnClickListener(v -> dismiss());
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo mProfileVo = new Gson().fromJson(json, ProfileVo.class);
        //当已绑定的支付宝\微信数量>0时，显示管理支付宝\微信
        if (mProfileVo != null) {
            if (mProfileVo.onepayzfb_count != null && Integer.parseInt(mProfileVo.onepayzfb_count) > 0) {
                binding.tvZfb.setText(R.string.txt_manage_alipay);
            } else {
                binding.tvZfb.setText(R.string.txt_bind_alipay);
            }
            if (mProfileVo.onepaywx_count != null && Integer.parseInt(mProfileVo.onepaywx_count) > 0) {
                binding.tvWx.setText(R.string.txt_manage_wechat);
            } else {
                binding.tvWx.setText(R.string.txt_bind_wechat);
            }
        }

        // 这里加点击事件
        for (int i = 0; i < binding.llMenu.getChildCount(); i++) {
            View child = binding.llMenu.getChildAt(i);
            child.setOnClickListener(v -> {
                String type = child.getTag().toString();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE;
                //ctx.startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);

                Intent intent = new Intent(getContext(), ContainerActivity.class);
                intent.putExtra(ContainerActivity.ROUTER_PATH, path);
                intent.putExtra(ContainerActivity.BUNDLE, bundle);
                ctx.startActivity(intent);
                dismiss();
            });
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_account_mgmt;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 85 / 100);
    }
}
