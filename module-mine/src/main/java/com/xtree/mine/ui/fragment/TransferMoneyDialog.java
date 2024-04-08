package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogTransferMoneyBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterFragmentPath.Mine.PAGER_MEMBER_TRANSFER)
public class TransferMoneyDialog extends BaseFragment<DialogTransferMoneyBinding, MineViewModel> {
    private static final String ARG_USERNAME = "userName";
    private static final String ARG_USERID = "userId";
    private static final String ARG_VERIFY = "verify";
    //LifecycleOwner owner;
    //DialogTransferMoneyBinding binding;
    //MineViewModel viewModel;
    String checkCode;
    String username;
    String userid;
    BasePopupView ppw = null;

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.liveDataSendMoney.observe(this, vo -> {
            if (vo.msg_type.equals("3")) {
                viewModel.getBalance();
            }
            ToastUtils.showLong(vo.message);
            getActivity().finish();
        });
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            userid = getArguments().getString(ARG_USERID);
            checkCode = getArguments().getString(ARG_VERIFY);
        }

        binding.tvwUserAccount.setText(username);
        binding.tvwUserBalance.setText(SPUtils.getInstance().getString(SPKeyGlobal.WLT_CENTRAL_BLC));

        binding.ivwClose.setOnClickListener(v -> getActivity().finish());
        binding.btnCancel.setOnClickListener(v -> getActivity().finish());
        binding.btnConfirm.setOnClickListener(v -> {
            String content = String.format(getContext().getString(R.string.txt_check_transfer), binding.etUserMoney.getText().toString());
            String txtRight = getContext().getString(R.string.text_confirm);
            String txtLeft = getContext().getString(R.string.text_cancel);
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

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_transfer_money;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    private void checkPassword() {
        String money = binding.etUserMoney.getText().toString();

        LoadingDialog.show(getContext());

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "confirm");
        map.put("check", checkCode);
        map.put("money", money);
        map.put("uid", userid);
        map.put("nonce", UuidUtil.getID16());
        viewModel.sendMoney(map);
    }
}