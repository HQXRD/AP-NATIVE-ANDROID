package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindAddAwBinding;
import com.xtree.mine.ui.viewmodel.BindCardViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.UserBankConfirmVo;
import com.xtree.mine.vo.UserBankProvinceVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 绑定支付宝、微信
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_AW_ADD)
public class BindAWAddFragment extends BaseFragment<FragmentBindAddAwBinding, BindCardViewModel> {
    private static final String ARG_TOKEN_SIGN = "tokenSign";
    private static final String ARG_MARK = "mark";
    private static final String ARG_TYPE = "type";
    private final String controller = "security";
    private final String action = "adduserbank";
    private static final String TYPE_ALIPAY = "alipay";
    private static final String TYPE_WECHAT = "wechat";

    private String mark = "bindcard";
    private String tokenSign;
    private String type;

    UserBankConfirmVo mConfirmVo;
    List<UserBankProvinceVo.AreaVo> listCity = new ArrayList<>();

    public BindAWAddFragment() {
    }

    @Override
    public void initView() {
        initArguments();
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ivwBack.setOnClickListener(v -> {
            if (binding.llAdd.getVisibility() == View.GONE) {
                binding.llAdd.setVisibility(View.VISIBLE);
                binding.llConfirm.setVisibility(View.GONE);
            } else {
                getActivity().finish();
            }
        });

        //binding.ivwNext.setOnClickListener(v -> doNext());
        binding.tvwSubmit.setOnClickListener(v -> doSubmit());

        binding.tvwBack.setOnClickListener(v -> {
            if (binding.llAdd.getVisibility() == View.GONE) {
                binding.llAdd.setVisibility(View.VISIBLE);
                binding.llConfirm.setVisibility(View.GONE);
            }
        });

    }

    public void initArguments() {
        if (getArguments() != null) {
            tokenSign = getArguments().getString(ARG_TOKEN_SIGN);
            mark = getArguments().getString(ARG_MARK);
            type = getArguments().getString(ARG_TYPE);
            String typeName = "";
            switch (type) {
                case TYPE_ALIPAY: {
                    typeName = getString(R.string.txt_alipay);
                    binding.tvwTitle.setText(getString(R.string.txt_bind_alipay));
                    binding.tvName.setText(("*" + getString(R.string.txt_alipay_name)));
                    binding.tvAccount.setText(("*" + getString(R.string.txt_alipay_phone)));
                    binding.tvNickname.setText(("*" + getString(R.string.txt_alipay_nickname)));

                    binding.tvwName.setText(getString(R.string.txt_alipay_name));
                    binding.tvwPhone.setText(getString(R.string.txt_alipay_phone));
                    binding.tvwNickname.setText(getString(R.string.txt_alipay_nickname));
                    binding.tvwCode.setText(getString(R.string.txt_alipay_code));
                }
                case TYPE_WECHAT: {
                    typeName = getString(R.string.txt_wechat);
                    binding.tvwTitle.setText(getString(R.string.txt_bind_wechat));
                    binding.tvName.setText(("*" + getString(R.string.txt_wechat_name)));
                    binding.tvAccount.setText(("*" + getString(R.string.txt_wechat_phone)));
                    binding.tvNickname.setText(("*" + getString(R.string.txt_wechat_nickname)));

                    binding.tvwName.setText(getString(R.string.txt_wechat_name));
                    binding.tvwPhone.setText(getString(R.string.txt_wechat_phone));
                    binding.tvwNickname.setText(getString(R.string.txt_wechat_nickname));
                    binding.tvwCode.setText(getString(R.string.txt_wechat_code));
                }
            }

        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_add_aw;
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

        viewModel.liveDataBindCardCheck.observe(this, vo -> {
            CfLog.i("******");
            mConfirmVo = vo;
            setConfirmView();
        });
        viewModel.liveDataBindCardResult.observe(this, vo -> {
            CfLog.i("******");
            //getActivity().finish();
            viewModel.getProfile();
        });
        viewModel.liveDataProfile.observe(this, vo -> {
            CfLog.i("******");
            getActivity().finish();
        });

    }

    private void setConfirmView() {
        binding.llAdd.setVisibility(View.GONE);
        binding.llConfirm.setVisibility(View.VISIBLE);

        binding.tvwNickname.setText(mConfirmVo.nickname);

    }

    //private void doNext() {
    //
    //    String account = binding.edtAcc.getText().toString().trim();
    //    String account_name = binding.edtName.getText().toString().trim();
    //    String branch = binding.edtBranch.getText().toString().trim();
    //
    //    if (account.isEmpty()) {
    //        ToastUtils.showLong(R.string.txt_enter_bank_num);
    //        return;
    //    }
    //    if (account_name.isEmpty()) {
    //        ToastUtils.showLong(R.string.txt_enter_account_name);
    //        return;
    //    }
    //
    //    if (mBankInfoVo == null) {
    //        ToastUtils.showLong(R.string.txt_choose_bank);
    //        return;
    //    }
    //    if (mProvince == null) {
    //        ToastUtils.showLong(R.string.txt_choose_province);
    //        return;
    //    }
    //    if (mCity == null) {
    //        ToastUtils.showLong(R.string.txt_choose_city);
    //        return;
    //    }
    //    if (branch.isEmpty()) {
    //        ToastUtils.showLong(R.string.txt_enter_branch);
    //        return;
    //    }
    //
    //    HashMap queryMap = new HashMap();
    //    queryMap.put("controller", controller);
    //    queryMap.put("action", action);
    //    queryMap.put("client", "m");
    //    queryMap.put("mark", mark);
    //    queryMap.put("check", tokenSign);
    //
    //    HashMap map = new HashMap();
    //    map.put("flag", "add");
    //    map.put("controller", controller);
    //    map.put("action", action);
    //    //map.put("oldid", "");
    //    map.put("entrancetype", "0");
    //    map.put("account", account);  // "4500***1234"
    //    map.put("account_name", account_name); // "姓名"
    //    map.put("bank", mBankInfoVo.bank_id + "#" + mBankInfoVo.bank_name); // "111#上海银行",
    //    map.put("bank_id", mBankInfoVo.bank_id); // "111",
    //    map.put("bank_name", mBankInfoVo.bank_name); // "上海银行",
    //    map.put("province", mProvince.id + "#" + mProvince.name); // "22#云南",
    //    map.put("province_id", mProvince.id); // "22",
    //    map.put("province_name", mProvince.name); // "云南",
    //    map.put("city", mCity.id + "#" + mCity.name); // "23#丽江",
    //    map.put("city_id", mCity.id); // "23",
    //    map.put("city_name", mCity.name); // "丽江",
    //    map.put("branch", branch); // "丽江支行",
    //    //map.put("submit", "下一步"); // "下一步",
    //    map.put("nonce", UuidUtil.getID16());
    //    viewModel.doBindCardByCheck(queryMap, map);
    //}

    private void doSubmit() {

        HashMap queryMap = new HashMap();
        queryMap.put("controller", controller);
        queryMap.put("action", action);
        queryMap.put("client", "m");
        queryMap.put("mark", mark);
        queryMap.put("check", tokenSign);

        HashMap map = new HashMap();
        map.put("flag", "confirm");
        map.put("controller", controller);
        map.put("action", action);
        map.put("nickname", "");
        map.put("bank_id", mConfirmVo.bank_id); // "111",
        map.put("bank", mConfirmVo.bank); // "上海银行",
        map.put("province_id", mConfirmVo.province_id); // "22",
        map.put("province", mConfirmVo.province); // "云南",
        map.put("city_id", mConfirmVo.city_id); // "23",
        map.put("city", mConfirmVo.city); // "丽江",
        map.put("branch", mConfirmVo.branch); // "丽江支行",
        map.put("account_name", mConfirmVo.account_name); // "姓名"
        map.put("account", mConfirmVo.account);  // "4500***1234"
        map.put("oldid", "");
        map.put("entrancetype", "0");

        map.put("nonce", UuidUtil.getID16());
        viewModel.doBindCardBySubmit(queryMap, map);
    }

}