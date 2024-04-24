package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogAccountMgmtBinding;

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 账户管理 底部弹窗
 */
public class AccountMgmtDialog extends BottomPopupView {
    BasePopupView ppw2;
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
                //String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE;
                String path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY; // 验证页(可左右滑动)
                //ctx.startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
                // 绑定页面显示去充值按钮用
                SPUtils.getInstance().put(SPKeyGlobal.TYPE_RECHARGE_WITHDRAW, ctx.getString(R.string.txt_go_recharge));

                if (!mProfileVo.is_binding_email && !mProfileVo.is_binding_phone) {
                    dismiss();
                    ppw2 = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", getResources().getString(R.string.txt_no_binding), "绑定手机", "绑定邮箱", new TipDialog.ICallBack() {
                        @Override
                        public void onClickLeft() {
                            startBinding(Constant.BIND_PHONE, type);
                            ppw2.dismiss();
                        }

                        @Override
                        public void onClickRight() {
                            startBinding(Constant.BIND_EMAIL, type);
                            ppw2.dismiss();
                        }
                    })).show();
                    return;
                }

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

    /**
     * @param type  绑手机/邮箱
     * @param type2 绑YHK/U/ZFB/VX
     */
    private void startBinding(String type, String type2) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("type2", type2); // 绑定手机/邮箱后,接着要办的下一个业务 20240418
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        getContext().startActivity(intent);
    }
}
