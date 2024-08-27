package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.widget.ListDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindUsdtRebindBinding;
import com.xtree.mine.ui.viewmodel.BindUsdtViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.UserUsdtConfirmVo;
import com.xtree.mine.vo.UserUsdtJumpVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

/**
 * 绑定USDT 增加
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_USDT_REBIND)
public class BindUsdtRebindFragment extends BaseFragment<FragmentBindUsdtRebindBinding, BindUsdtViewModel> {

    private final String controller = "security";
    private String action = "adduserusdt"; // adduserusdt

    private String id = "";
    String type = ""; // ERC20_USDT,TRC20_USDT
    UserUsdtJumpVo mUserUsdtJumpVo;

    ItemTextBinding binding2;
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)
    List<String> typeList = new ArrayList<>();

    UserUsdtConfirmVo mConfirmVo;

    public BindUsdtRebindFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvwTitle.setText(mUserUsdtJumpVo.title);
        binding.tvwTipAddress.setText(mUserUsdtJumpVo.remind);

        if (mUserUsdtJumpVo != null && mUserUsdtJumpVo.isShowType) {
            binding.tvwChoose.setVisibility(View.VISIBLE);
            binding.tvwChooseTitle.setVisibility(View.VISIBLE);
            binding.tvwTipAddress.setText("");
        } else {
            binding.tvwChoose.setVisibility(View.GONE);
            binding.tvwChooseTitle.setVisibility(View.GONE);
        }

        getRebindCheck();
    }

    @Override
    public void initView() {
        binding.llRoot.setOnClickListener(v -> hideKeyBoard());
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.tvwChoose.setOnClickListener(v -> showChooseType());
        binding.ivwNext.setOnClickListener(v -> doNext());
        //新增返回按钮事件
        binding.tvwBindBack.setOnClickListener(v -> getActivity().finish());
        binding.tvwSubmit.setOnClickListener(v -> doSubmit());

        binding.tvwBack.setOnClickListener(v -> {
            if (binding.llAdd.getVisibility() == View.GONE) {
                binding.llAdd.setVisibility(View.VISIBLE);
                binding.llConfirm.setVisibility(View.GONE);
            }
        });

        binding.tvwBackOld.setOnClickListener(v -> getActivity().finish());
        binding.tvwSubmitOld.setOnClickListener(v -> doCheckOld());
    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            mUserUsdtJumpVo = getArguments().getParcelable("obj");
            CfLog.i(mUserUsdtJumpVo.toString());
            action = mUserUsdtJumpVo.action;
            //mark = mUserUsdtJumpVo.mark;
            //tokenSign = mUserUsdtJumpVo.tokenSign;
            type = mUserUsdtJumpVo.type;
            id = mUserUsdtJumpVo.id;
            viewModel.key = mUserUsdtJumpVo.key;
        }
        if (TextUtils.isEmpty(id)) {
            CfLog.e("Arguments is null... ");
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_usdt_rebind;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BindUsdtViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(BindUsdtViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.liveDataRebindCard01.observe(this, vo -> {
            CfLog.i("****** 1 " + vo.flag);
            mConfirmVo = vo;
            if (vo != null && !TextUtils.isEmpty(vo.flag) && !TextUtils.isEmpty(vo.action)) {
                binding.tvwSubmitOld.setEnabled(true); // 设置为可点击
            }
        });

        viewModel.liveDataRebindCard02.observe(this, vo -> {
            CfLog.i("****** 2 " + vo.flag);
            mConfirmVo = vo;
            binding.llOld.setVisibility(View.GONE);
            binding.llAdd.setVisibility(View.VISIBLE);

            if (mUserUsdtJumpVo.isShowType) {
                String json = new Gson().toJson(mConfirmVo.usdt_type);
                List<String> list = new Gson().fromJson(json, new TypeToken<List<String>>() {
                }.getType());
                typeList.clear();
                typeList.addAll(list);
            }

        });
        viewModel.liveDataRebindCard03.observe(this, vo -> {
            CfLog.i("****** 3 " + vo.flag);
            mConfirmVo = vo;
            setConfirmView();
        });
        viewModel.liveDataRebindCard04.observe(this, vo -> {
            CfLog.i("****** 4 ");
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

        binding.tvwType.setText(mConfirmVo.usdt_type.toString());
        binding.tvwAcc.setText(mConfirmVo.usdt_card);
    }

    /**
     * 步骤-1 重新绑定检查
     */
    private void getRebindCheck() {

        HashMap queryMap = new HashMap();
        queryMap.put("flag", "reset");
        queryMap.put("id", id);
        queryMap.put("client", "m");

        HashMap map = new HashMap();

        viewModel.doRebindCard01(queryMap, map);
    }

    /**
     * 步骤-2 重新绑定
     * （ 校验 旧号 ）
     */
    private void doCheckOld() {
        String account = binding.edtAccOld.getText().toString().trim();

        HashMap queryMap = new HashMap();
        queryMap.put("flag", "reset");
        queryMap.put("id", id);
        queryMap.put("client", "m");

        HashMap map = new HashMap();
        map.put("flag", mConfirmVo.flag); // "verify"
        map.put("controller", controller);
        map.put("action", mConfirmVo.action); // usdtrebinding
        map.put("oldid", id);

        map.put("smscode", "");
        map.put("smstype", "");
        map.put("usdt_card", account);

        viewModel.doRebindCard02(queryMap, map);
    }

    /**
     * 步骤-3 重新绑定
     * （ 校验 新号 ）
     */
    private void doNext() {
        type = binding.tvwChoose.getText().toString();
        String account = binding.edtAcc.getText().toString().trim();
        String account2 = binding.edtAcc2.getText().toString().trim();

        if (!typeList.isEmpty() && type.isEmpty()) {
            ToastUtils.showLong(R.string.txt_choose_type_pls);
            return;
        }
        if (account.isEmpty()) {
            ToastUtils.showLong(R.string.txt_enter_wallet_addr);
            return;
        }
        if (!account2.equals(account)) {
            ToastUtils.showLong(R.string.txt_address_should_same);
            return;
        }
        HashMap queryMap = new HashMap();
        queryMap.put("flag", "reset");
        queryMap.put("id", id);
        queryMap.put("client", "m");

        HashMap map = new HashMap();
        map.put("flag", mConfirmVo.flag); // "reset"
        map.put("controller", controller);
        map.put("action", action); //

        map.put("oldid", id);
        map.put("entrancetype", "0");
        map.put("usdt_type", type);
        map.put("bank_id", "");
        map.put("bank_name", "");
        map.put("usdt_card", account);
        map.put("account_again", account);

        viewModel.doRebindCard03(queryMap, map);
    }

    /**
     * 步骤-4 重新绑定
     * （ 提交 ）
     */
    private void doSubmit() {

        HashMap queryMap = new HashMap();
        queryMap.put("flag", "reset");
        queryMap.put("id", id);
        queryMap.put("client", "m");

        HashMap map = new HashMap();
        map.put("flag", mConfirmVo.flag); // confirmset
        map.put("controller", controller);
        map.put("action", action); //

        map.put("usdt_type", mConfirmVo.usdt_type);
        map.put("usdt_card", mConfirmVo.usdt_card);
        map.put("oldid", id);
        map.put("entrancetype", "0");

        viewModel.doRebindCard04(queryMap, map);
    }

    private void showChooseType() {
        showDialog();
    }

    private void showDialog() {

        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<String>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                String txt = get(position);
                binding2.tvwTitle.setText(txt);

                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvwChoose.setText(txt);
                    if (txt.contains("TRC")) {
                        binding.tvwTipAddress.setText(R.string.txt_remind_usdt_trc20);
                        binding.tvwTipAddress.setVisibility(View.VISIBLE);
                    } else if (txt.contains("ERC")) {
                        binding.tvwTipAddress.setText(R.string.txt_remind_usdt_erc20);
                    } else {
                        binding.tvwTipAddress.setText(mUserUsdtJumpVo.remind);
                    }

                    ppw.dismiss();
                });

            }
        };

        //adapter.clear();
        adapter.addAll(typeList);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "", adapter));
        ppw.show();
    }

}