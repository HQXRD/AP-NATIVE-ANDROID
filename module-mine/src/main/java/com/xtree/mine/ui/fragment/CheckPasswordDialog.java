package com.xtree.mine.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.data.Injection;
import com.xtree.mine.databinding.DialogTransferMemberBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.VerifyViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.VerifyVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

@Route(path = RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE)
public class CheckPasswordDialog extends BaseFragment<DialogTransferMemberBinding, MineViewModel> {
    private static final String ARG_TYPE = "type";
    private static final String ARG_PAGE = "page";
    private static final String ARG_VERIFY = "verify";
    String type = "";
    String page = "";
    String verify = "";
    MineViewModel mineViewModel;
    VerifyViewModel verifyViewModel;

    @Override
    public void initData() {
        super.initData();
        binding.setVariable(BR.model, viewModel);
        verifyViewModel = new VerifyViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
        mineViewModel = new MineViewModel((Application) Utils.getContext(), Injection.provideHomeRepository());
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE, "");
            page = getArguments().getString(ARG_PAGE, "");
            verify = getArguments().getString(ARG_VERIFY, "");
        }
    }

    @Override
    public void initView() {
        binding.ivwClose.setOnClickListener(v -> close());
        binding.btnCancel.setOnClickListener(v -> close());
        binding.btnConfirm.setOnClickListener(v -> checkPassword());
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

    @Override
    public void initViewObservable() {
        mineViewModel.liveDataCheckPassword.observe(this, vo -> {
            if (!vo.isEmpty()) {
                if (page.isEmpty()) {
                    VerifyVo verifyVo = new VerifyVo();
                    verifyVo.tokenSign = vo;
                    verifyVo.mark = type;
                    verifyViewModel.goOthers(getActivity(), type, verifyVo);
                    close();
                } else {
                    Bundle bundle = getArguments();
                    bundle.putString("verify", vo);
                    startContainerFragment(page, bundle);
                    close();
                }
            } else {
                ToastUtils.showLong(vo);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (page.equals("pass")) {
            VerifyVo verifyVo = new VerifyVo();
            verifyVo.tokenSign = verify;
            verifyVo.mark = type;
            intoBindCard(verifyVo);
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_transfer_member;
    }

    private void checkPassword() {
        String code = binding.etTransferPassword.getText().toString();

        LoadingDialog.show(getContext());

        HashMap<String, String> map = new HashMap<>();
        map.put("flag", "check");
        map.put("nextact", "bindsequestion");
        map.put("nextcon", "user");
        map.put("secpass", code);
        map.put("nonce", UuidUtil.getID16());
        mineViewModel.checkMoneyPassword(map);
    }

    private void close() {
        getActivity().finish();
    }

    public void intoBindCard(VerifyVo vo) {
        verifyViewModel.goOthers(getActivity(), type, vo);
    }
}