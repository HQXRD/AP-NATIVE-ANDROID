package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindCardLockBinding;
import com.xtree.mine.ui.viewmodel.BindCardViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 锁定银行卡
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_CARD_LOCK)
public class BindCardLockFragment extends BaseFragment<FragmentBindCardLockBinding, BindCardViewModel> {
    private static final String ARG_ID = "id";
    private final String controller = "security";
    private final String action = "deluserbank";
    private String id = "";

    public BindCardLockFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        doCheck();
    }

    @Override
    public void initView() {
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.tvwBack.setOnClickListener(v -> getActivity().finish());
        binding.tvwSubmit.setOnClickListener(v -> doSubmit());

    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            String accountName = requireArguments().getString("accountName");
           /* if (accountName != null && !accountName.isEmpty()) {
                binding.edtName.setText(accountName);
                binding.edtName.setEnabled(false);
            }*/

        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_card_lock;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BindCardViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(BindCardViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataDelCardCheck.observe(this, vo -> {
            CfLog.i("******");
            if (vo.msg_type == 2) {
                // "操作失败"
                ToastUtils.showLong(vo.message); // 您提交的数据有误，请核对后重新提交！
                getActivity().finish();
            }
        });

        viewModel.liveDataDelCardResult.observe(this, vo -> {
            CfLog.i("******");
            ToastUtils.showLong(vo.message);
            // msg_type 1,2-失败, 3-成功
            if (vo.msg_type == 3) {
                // "操作成功"
                getActivity().finish();
            }
        });
    }

    private void doCheck() {

        HashMap queryMap = new HashMap();
        queryMap.put("flag", "lock");
        queryMap.put("client", "m");
        queryMap.put("id", id);

        HashMap map = new HashMap();

        viewModel.delCardByCheck(queryMap, map);
    }

    private void doSubmit() {

        String account = binding.edtAcc.getText().toString().trim();
        String account_name = binding.edtName.getText().toString().trim();

        if (account.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_bank_num);
            return;
        }
        if (account_name.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_account_name);
            return;
        }

        HashMap queryMap = new HashMap();
        queryMap.put("flag", "lock");
        queryMap.put("client", "m");
        queryMap.put("id", id);

        HashMap map = new HashMap();
        map.put("flag", "lock");
        map.put("controller", controller);
        map.put("action", action);
        map.put("id", id);
        map.put("smscode", "");
        map.put("smstype", "");
        map.put("account_name", account_name); // "姓名"
        map.put("account", account);  // "4500***1234"

        map.put("nonce", UuidUtil.getID16());
        viewModel.delCardBySubmit(queryMap, map);
    }

}