package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentWithdrawBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.ProfileVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 提现页
 */
@Route(path = RouterFragmentPath.Wallet.PAGER_WITHDRAW)
public class WithdrawFragment extends BaseFragment<FragmentWithdrawBinding, MyWalletViewModel> {
    ProfileVo mProfileVo;
    BasePopupView ppw = null; // 底部弹窗

    public WithdrawFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mProfileVo == null) {
            getActivity().finish();
            return;
        }
        viewModel.readCache(); // 先读取缓存数据
        //refreshBalance();
        viewModel.getBalance();

        binding.tvwUserName1.setText(mProfileVo.username);
        binding.tvwUserName2.setText(mProfileVo.username);

        binding.rdoYhk.setChecked(true);
        setCards(binding.rdoYhk.getId());

    }

    @Override
    public void initView() {
        int auto_thrad_status = SPUtils.getInstance().getInt(SPKeyGlobal.USER_AUTO_THRAD_STATUS);
        if (auto_thrad_status == 1) {
            binding.llCenterWallet.ckbAuto.setChecked(true);
        } else {
            binding.llCenterWallet.ckbAuto.setChecked(false);
        }

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.llCenterWallet.tvw1kRecycle.setOnClickListener(v -> showDialog1kAutoRecycle());
        binding.llCenterWallet.ckbAuto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showDialogAutoTrans();
            } else {
                // 关闭自动免转, 不用弹窗提示
                doAutoTransfer(false);
            }
        });

        binding.rgpWithdraw.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                setCards(checkedId);
            }
        });

        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CfLog.d("amount: " + s);
                if (s.toString().indexOf(".") > 0) {
                    binding.tvwRealAmount.setText(s.subSequence(0, s.toString().indexOf(".")));
                    binding.tvwWdAmount.setText(binding.tvwRealAmount.getText());
                    binding.tvwRealAmount2.setText(binding.tvwRealAmount.getText());
                } else {
                    binding.tvwRealAmount.setText(s);
                    binding.tvwWdAmount.setText(s);
                    binding.tvwRealAmount2.setText(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.ivwNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.llStep01.getVisibility() == View.VISIBLE) {
                    binding.llStep01.setVisibility(View.GONE);
                    binding.llStep02.setVisibility(View.VISIBLE);

                    if (binding.rdoYhk.isChecked()) {
                        binding.llBank1.setVisibility(View.VISIBLE);
                        binding.llBank2.setVisibility(View.VISIBLE);
                        binding.llEbpay1.setVisibility(View.GONE);
                        binding.llEbpay2.setVisibility(View.GONE);
                        binding.llUsdt1.setVisibility(View.GONE);
                        binding.llUsdt2.setVisibility(View.GONE);
                    } else if (binding.rdoEbpay.isChecked()) {
                        binding.llBank1.setVisibility(View.GONE);
                        binding.llBank2.setVisibility(View.GONE);
                        binding.llEbpay1.setVisibility(View.VISIBLE);
                        binding.llEbpay2.setVisibility(View.VISIBLE);
                        binding.llUsdt1.setVisibility(View.GONE);
                        binding.llUsdt2.setVisibility(View.GONE);
                    } else if (binding.rdoUsdt.isChecked()) {
                        binding.llBank1.setVisibility(View.GONE);
                        binding.llBank2.setVisibility(View.GONE);
                        binding.llEbpay1.setVisibility(View.GONE);
                        binding.llEbpay2.setVisibility(View.GONE);
                        binding.llUsdt1.setVisibility(View.VISIBLE);
                        binding.llUsdt2.setVisibility(View.VISIBLE);
                    }

                } else {
                    // 提款,提交到接口
                }
            }
        });

    }

    @Override
    public void initData() {
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_withdraw;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MyWalletViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MyWalletViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataBalance.observe(this, new Observer<BalanceVo>() {
            @Override
            public void onChanged(BalanceVo vo) {
                binding.llCenterWallet.tvwBalance.setText(vo.balance);
                binding.tvwWdAmountAll.setText(vo.balance);
            }
        });

        viewModel.liveDataAutoTrans.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    ToastUtils.showLong(R.string.txt_set_succ);
                    //refreshBalance();
                } else {
                    ToastUtils.showLong(R.string.txt_set_fail);
                }

            }
        });
        viewModel.liveData1kRecycle.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    ToastUtils.showLong(R.string.txt_recycle_succ);
                    //refreshBalance();
                } else {
                    ToastUtils.showLong(R.string.txt_recycle_fail);
                }
            }
        });

    }

    private void showDialog1kAutoRecycle() {
        String title = getString(R.string.txt_1k_recycle);
        String msg = getString(R.string.txt_is_1k_recycle_all);

        ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                viewModel.do1kAutoRecycle();
                ppw.dismiss();
            }
        }));
        ppw.show();
    }

    private void showDialogAutoTrans() {
        String title = getString(R.string.txt_tip);
        String msg = getString(R.string.txt_open_auto_slow);
        String txtRight = getString(R.string.txt_confirm_open);
        ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, "", txtRight, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                binding.llCenterWallet.ckbAuto.setChecked(false);//
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                doAutoTransfer(true);
                ppw.dismiss();
            }
        }));
        ppw.show();
    }

    private void doAutoTransfer(boolean isOpen) {
        String status = isOpen ? "1" : "0"; // 1-开启,0-关闭
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        ProfileVo vo = new Gson().fromJson(json, ProfileVo.class);

        // 开启自动免转
        HashMap<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("userid", String.valueOf(vo.userid));
        map.put("nonce", UuidUtil.getID16());
        viewModel.doAutoTransfer(map);
    }

    private void setCards(int resId) {

        binding.llStep01.setVisibility(View.VISIBLE);
        binding.llStep02.setVisibility(View.GONE);
        binding.llStep03.setVisibility(View.GONE);

        if (resId == R.id.rdo_yhk) {
            // ProfileVo.CardInfoVo[] array = mProfileVo.binding_card_info;
            List<ProfileVo.CardInfoVo> list = new ArrayList<>();
            list.addAll(Arrays.asList(mProfileVo.binding_card_info));
            list.add(new ProfileVo.CardInfoVo());

            BankWithdrawAdapter adapter = new BankWithdrawAdapter(getContext(), new BankWithdrawAdapter.ICallBack() {
                @Override
                public void onClick(ProfileVo.CardInfoVo vo) {
                    binding.tvwUserName1.setText(vo.bank_name);
                    binding.tvwUserName2.setText(vo.bank_name);
                }
            });

            binding.rcvCards.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.rcvCards.setAdapter(adapter);
            adapter.addAll(list);
        } else if (resId == R.id.rdo_usdt) {

        } else if (resId == R.id.rdo_ebpay) {

        } else {

        }

    }

}