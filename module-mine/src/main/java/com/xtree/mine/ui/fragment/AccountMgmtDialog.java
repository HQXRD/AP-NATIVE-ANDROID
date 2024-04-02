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
    ProfileVo mProfileVo;

    public AccountMgmtDialog(@NonNull Context context) {
        super(context);
        ctx = context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = DialogAccountMgmtBinding.bind(findViewById(R.id.cl_root));
        initData();
        initView();
    }

    private void initView() {
        binding.ivwClose.setOnClickListener(v -> dismiss());

        // 这里加点击事件
        for (int i = 0; i < binding.llMenu.getChildCount(); i++) {
            View child = binding.llMenu.getChildAt(i);
            child.setOnClickListener(v -> {
                String type = child.getTag().toString();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                String path;
                //ctx.startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);

                if (mProfileVo.has_securitypwd) {
                    path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE;
                    Intent intent = new Intent(getContext(), ContainerActivity.class);
                    intent.putExtra(ContainerActivity.ROUTER_PATH, path);
                    intent.putExtra(ContainerActivity.BUNDLE, bundle);
                    ctx.startActivity(intent);
                } else {
                    path = RouterFragmentPath.Mine.PAGER_FUNDS_PWD;
                    Intent intent = new Intent(getContext(), ContainerActivity.class);
                    intent.putExtra(ContainerActivity.ROUTER_PATH, path);
                    intent.putExtra(ContainerActivity.BUNDLE, bundle);
                    ctx.startActivity(intent);
                }
                dismiss();
            });
        }
    }

    private void initData() {
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
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
