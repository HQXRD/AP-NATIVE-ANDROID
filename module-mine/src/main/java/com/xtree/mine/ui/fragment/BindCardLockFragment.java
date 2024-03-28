package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
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
import com.xtree.mine.vo.UserUsdtJumpVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 锁定银行卡
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_CARD_LOCK)
public class BindCardLockFragment extends BaseFragment<FragmentBindCardLockBinding, BindCardViewModel> {
    private static final String ARG_ID = "id";
    private static final String ARG_IS_VERIFY = "is_verify";
    private static final String ARG_IS_USDT = "is_usdt";
    private static final String ARG_OBJ = "obj";
    private final String controller = "security";
    private final String action = "deluserbank";
    private boolean isVerify = false;
    private boolean isUSDT = false;
    private String id = "";
    private UserUsdtJumpVo vo = null;

    public BindCardLockFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isVerify && !isUSDT) {
            doCheck();
        }
        if (isVerify) {
            binding.tvwDelWarning.setVisibility(View.INVISIBLE);
            binding.tvwName.setText(getResources().getText(R.string.txt_verify_name));
            binding.tvwAcc.setText(getResources().getText(R.string.txt_verify_acc));
        }
        if (isUSDT) {
            binding.tvwDelWarning.setVisibility(View.GONE);
            binding.tvwName.setVisibility(View.GONE);
            binding.edtName.setVisibility(View.GONE);
            binding.tvwNameWarning.setVisibility(View.GONE);
            if (vo != null) {
                binding.tvwTitle.setText(vo.title);
            } else {
                binding.tvwTitle.setText(getResources().getText(R.string.txt_bind_wallet));
            }
            binding.tvwAcc.setText(getResources().getText(R.string.txt_verify_addr));
            binding.edtAcc.setInputType(InputType.TYPE_CLASS_TEXT);
            InputFilter[] filters = new InputFilter[1];
            filters[0] = new InputFilter.LengthFilter(64);
            binding.edtAcc.setFilters(filters);
            binding.edtAcc.setHint(getResources().getText(R.string.txt_enter_wallet_addr));
            binding.tvwAccWarning.setHint(getResources().getText(R.string.txt_verify_addr_warning));
        }
    }

    @Override
    public void initView() {
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.tvwBack.setOnClickListener(v -> getActivity().finish());
        binding.tvwSubmit.setOnClickListener(v -> {
            if (isVerify || isUSDT) {
                doVerify();
            } else {
                doSubmit();
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            isVerify = getArguments().getBoolean(ARG_IS_VERIFY);
            isUSDT = getArguments().getBoolean(ARG_IS_USDT);
            vo = getArguments().getParcelable(ARG_OBJ);
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

        viewModel.liveDataVerify.observe(this, isSuccess -> {
            CfLog.i("******");
            if (isSuccess) {
                if (isUSDT) {
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_USDT_ADD, getArguments());
                } else {
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_CARD_ADD, getArguments());
                }
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

    private void doVerify() {
        String account = binding.edtAcc.getText().toString().trim();
        String account_name = binding.edtName.getText().toString().trim();

        if (account.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_bank_num);
            return;
        }
        if (account_name.isEmpty() && isVerify) {
            ToastUtils.showLong(R.string.txt_enter_account_name);
            return;
        }

        HashMap qMap = new HashMap();
        qMap.put("client", "m");
        qMap.put("1", 1);

        HashMap map = new HashMap();
        map.put("account", account);
        if (isUSDT) {
            map.put("account_name", "");
            map.put("is_digital", "1");
        } else {
            map.put("account_name", account_name);
            map.put("is_digital", "0");
        }
        map.put("nonce", UuidUtil.getID16());

        viewModel.doVerify(qMap, map);
    }
}