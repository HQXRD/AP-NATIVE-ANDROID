package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.vo.MemberUserInfoVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class TransferMoneyDialog extends BottomPopupView {
    Context context;
    LifecycleOwner owner;
    com.xtree.mine.databinding.DialogTransferMoneyBinding binding;
    MineViewModel viewModel;
    ICallBack callBack;
    String checkCode;
    MemberUserInfoVo vo;
    BasePopupView ppw = null;

    public TransferMoneyDialog(@NonNull Context context) {
        super(context);
    }

    public interface ICallBack {
        void onSuccessTransfer();
    }

    public static TransferMoneyDialog newInstance(@NonNull Context context, LifecycleOwner owner, String checkCode, MemberUserInfoVo vo, ICallBack callBack) {
        TransferMoneyDialog transferMoneyDialog = new TransferMoneyDialog(context);
        transferMoneyDialog.context = context;
        transferMoneyDialog.owner = owner;
        transferMoneyDialog.checkCode = checkCode;
        transferMoneyDialog.vo = vo;
        transferMoneyDialog.callBack = callBack;
        return transferMoneyDialog;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        binding = com.xtree.mine.databinding.DialogTransferMoneyBinding.bind(findViewById(R.id.ll_root));
        viewModel = new MineViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());

        viewModel.liveDataSendMoney.observe(owner, vo -> {
            if (vo.msg_type.equals("3")) {
                callBack.onSuccessTransfer();
            }
            ToastUtils.showLong(vo.message);
            dismiss();
        });

        initView();
    }

    private void initView() {
        binding.tvwUserAccount.setText(vo.username);
        binding.tvwUserBalance.setText(SPUtils.getInstance().getString(SPKeyGlobal.WLT_CENTRAL_BLC));

        binding.ivwClose.setOnClickListener(v -> dismiss());
        binding.btnCancel.setOnClickListener(v -> dismiss());
        binding.btnConfirm.setOnClickListener(v -> {
            String content = String.format(getContext().getString(R.string.txt_check_transfer), binding.etUserMoney.getText().toString());
            String txtRight = context.getString(R.string.text_confirm);
            String txtLeft = context.getString(R.string.text_cancel);
            ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), "", content, txtLeft, txtRight, new MsgDialog.ICallBack() {
                @Override
                public void onClickLeft() {
                    ppw.dismiss();
                }

                @Override
                public void onClickRight() {
                    checkPassword();
                    ppw.dismiss();
                }
            }));
            ppw.show();
        });
    }

    private void checkPassword() {
        String money = binding.etUserMoney.getText().toString();

        LoadingDialog.show(context);

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "confirm");
        map.put("check", checkCode);
        map.put("money", money);
        map.put("uid", vo.userid);
        map.put("nonce", UuidUtil.getID16());
        viewModel.sendMoney(map);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_transfer_money;
    }

    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }
}