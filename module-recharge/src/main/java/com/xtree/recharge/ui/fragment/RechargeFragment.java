package com.xtree.recharge.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentRechargeBinding;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.BankCardVo;
import com.xtree.recharge.vo.RechargePayVo;
import com.xtree.recharge.vo.RechargeVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Recharge.PAGER_RECHARGE)
public class RechargeFragment extends BaseFragment<FragmentRechargeBinding, RechargeViewModel> {

    RechargeAdapter rechargeAdapter;
    double loadMin;
    double loadMax;
    RechargeVo curRechargeVo;
    BasePopupView ppw = null; // 底部弹窗 (选择银行卡)
    String bankId = ""; // 银行卡ID
    String hiWalletUrl; // 一键进入 HiWallet钱包
    String tutorialUrl; // 充值教程
    HashMap<String, RechargeVo> mapRechargeVo = new HashMap<>(); // 跳转第三方链接的充值渠道

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_recharge;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public RechargeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(RechargeViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.readCache(); // 先读取缓存数据
        viewModel.getPayments(); // 调用接口
        viewModel.get1kEntry(); // 一键进入
    }

    @Override
    public void initView() {
        boolean isShowBack = getArguments().getBoolean("isShowBack");
        if (isShowBack) {
            binding.ivwBack.setVisibility(View.VISIBLE);
        } else {
            binding.ivwBack.setVisibility(View.GONE);
        }
        binding.ivwBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        rechargeAdapter = new RechargeAdapter(getContext(), new RechargeAdapter.ICallBack() {
            @Override
            public void onClick(RechargeVo vo) {
                CfLog.d(vo.toInfo());
                curRechargeVo = vo;
                bankId = "";
                binding.tvwCurPmt.setText(vo.title);
                Drawable dr = getContext().getDrawable(R.drawable.rc_ic_pmt_selector);
                dr.setLevel(Integer.parseInt(vo.bid));
                binding.ivwCurPmt.setImageDrawable(dr);

                if (vo.op_thiriframe_use && !vo.phone_needbind) {
                    CfLog.d(vo.title + ", jump: " + vo.op_thiriframe_url);
                    binding.llDown.setVisibility(View.GONE); // 下面的部分隐藏
                    if (!TextUtils.isEmpty(vo.op_thiriframe_url)) {
                        String url = DomainUtil.getDomain2() + vo.op_thiriframe_url;
                        new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), vo.title, url)).show();
                    } else {
                        // 如果没有链接,调详情接口获取
                        //viewModel.getPayment(vo.bid);
                        if (mapRechargeVo.containsKey(vo.bid)) {
                            RechargeVo t2 = mapRechargeVo.get(vo.bid);
                            String url = DomainUtil.getDomain2() + t2.op_thiriframe_url;
                            CfLog.d(vo.title + ", jump: " + url);
                            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), vo.title, url)).show();
                        } else {
                            CfLog.e(vo.title + ", op_thiriframe_url is null...");
                        }
                    }

                    return;
                }

                binding.llDown.setVisibility(View.VISIBLE); // 下面的部分显示

                // 显示/隐藏银行卡
                if (!vo.userBankList.isEmpty()) {
                    binding.tvwChooseBankCard.setVisibility(View.VISIBLE);
                    binding.tvwBankCard.setVisibility(View.VISIBLE);
                    binding.tvwBankCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showBankCardDialog(vo);
                        }
                    });
                } else {
                    binding.tvwChooseBankCard.setVisibility(View.GONE);
                    binding.tvwBankCard.setVisibility(View.GONE);
                }

                // 设置存款人姓名
                if (vo.realchannel_status && vo.phone_fillin_name) {
                    binding.edtName.setText(vo.accountname);
                    binding.llName.setVisibility(View.VISIBLE);
                } else {
                    binding.edtName.setText("");
                    binding.llName.setVisibility(View.GONE);
                }

                // 有一组金额按钮需要显示出来 (固额和非固额)
                if (vo.fixedamount_channelshow && vo.fixedamount_info.length > 0) {
                    binding.edtAmount.setEnabled(false);
                    setAmountGrid(vo);
                } else {
                    binding.edtAmount.setEnabled(true);
                    List<String> list = getFastMoney(vo.loadmin, vo.loadmax);
                    vo.fixedamount_info = list.toArray(new String[list.size()]);
                    setAmountGrid(vo);
                }

                binding.edtAmount.setText("");

                loadMin = Double.parseDouble(vo.loadmin);
                loadMax = Double.parseDouble(vo.loadmax);
                setRate(vo); // 设置汇率提示信息

            }
        });

        binding.rcvPmt.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.rcvPmt.setAdapter(rechargeAdapter);
        binding.rcvPmt.setNestedScrollingEnabled(false); // 禁止滑动

        binding.ivwCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 客服
            }
        });
        binding.ivwRule.setOnClickListener(v -> {
            // 反馈
            startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE_FEEDBACK);
        });
        binding.ivwMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 消息
            }
        });

        binding.ivw1k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1键进入
                show1kEntryDialog();
            }
        });
        binding.tvwTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 充值教程
                if (!TextUtils.isEmpty(tutorialUrl)) {
                    String title = getString(R.string.txt_recharge_tutorial);
                    new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, tutorialUrl)).show();
                }
            }
        });

        binding.tvwBindPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.tvwBindYhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.ivwClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtName.setText("");
            }
        });
        binding.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CfLog.d("name: " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                } else {
                    binding.tvwRealAmount.setText(s);
                }

                if (curRechargeVo != null && !TextUtils.isEmpty(curRechargeVo.usdtrate)) {
                    setUsdtRate(curRechargeVo);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下一步
                if (curRechargeVo == null) {
                    ToastUtils.showLong(R.string.pls_choose_recharge_type);
                    return;
                }

                String txt = binding.edtAmount.getText().toString();
                double amount = Double.parseDouble(0 + txt);
                if (amount < loadMin && amount > loadMax) {
                    txt = String.format(getString(R.string.txt_recharge_range), curRechargeVo.loadmin, curRechargeVo.loadmax);
                    ToastUtils.showLong(txt);
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("alipayName", ""); //
                map.put("amount", txt); //
                // nonce: 如果第一次请求失败，第二次再请求 不能改变
                map.put("nonce", UUID.randomUUID().toString().replace("-", ""));
                map.put("rechRealname", binding.edtName.getText().toString().trim()); //

                map.put("bankid", bankId);
                //map.put("perOrder", "false");
                //map.put("orderKey", "");

                viewModel.rechargePay(curRechargeVo.bid, map);

            }
        });
    }

    private void show1kEntryDialog() {
        String title = getString(R.string.txt_kind_tips);
        String msg = getString(R.string.txt_is_to_open_hiwallet_wallet);
        ppw = new XPopup.Builder(getContext()).asCustom(new MsgDialog(getContext(), title, msg, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), "", hiWalletUrl)).show();
                ppw.dismiss();
            }
        }));
        ppw.show();
    }

    private void setAmountGrid(RechargeVo vo) {
        AmountAdapter adapter = new AmountAdapter(getContext(), new AmountAdapter.ICallBack() {
            @Override
            public void onClick(String str) {
                binding.edtAmount.setText(str);
            }
        });
        binding.rcvAmount.setAdapter(adapter);
        binding.rcvAmount.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter.addAll(Arrays.asList(vo.fixedamount_info));
    }

    private void showBankCardDialog(RechargeVo vo) {
        BankAdapter adapter = new BankAdapter(getContext(), new BankAdapter.ICallBack() {
            @Override
            public void onClick(BankCardVo vo) {
                CfLog.i(vo.toString());
                bankId = vo.id;
                binding.tvwBankCard.setText(vo.name);
                if (ppw != null) {
                    ppw.dismiss();
                }
            }
        });
        adapter.addAll(vo.userBankList);
        String title = getString(R.string.txt_pls_select_payment_card);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), title, adapter));
        ppw.show();
    }

    @Override
    public void initData() {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
            return;
        }

        String json = SPUtils.getInstance().getString(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, "{}");
        mapRechargeVo = new Gson().fromJson(json, new TypeToken<HashMap<String, RechargeVo>>() {
        }.getType());

    }

    private void setRate(RechargeVo vo) {
        binding.llRate.setVisibility(View.VISIBLE);
        binding.tvwPrePay.setText("");

        if (!TextUtils.isEmpty(vo.usdtrate)) {
            setUsdtRate(vo);
        } else if (vo.paycode.equals("hiwallet")) {
            binding.tvwFxRate.setText(R.string.txt_rate_cnyt_cny);
        } else if (vo.paycode.equals("hqppaytopay")) {
            binding.tvwFxRate.setText(R.string.txt_rate_tog_cny);
        } else if (vo.paycode.equals("ebpay")) {
            binding.tvwFxRate.setText(R.string.txt_rate_eb_cny);
        } else {
            binding.tvwFxRate.setText("");
            binding.llRate.setVisibility(View.GONE);
        }

    }

    private void setUsdtRate(RechargeVo vo) {
        binding.tvwFxRate.setText(getString(R.string.txt_rate_usdt) + vo.usdtrate);
        int realMoney = Integer.parseInt(0 + binding.tvwRealAmount.getText().toString());
        float realUsdt = realMoney / Float.parseFloat(vo.usdtrate);
        //String usdt = new DecimalFormat("#.##").format(realUsdt);
        String usdt = String.format(getString(R.string.format_change_range), realUsdt);
        binding.tvwPrePay.setText(usdt);
    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));
        viewModel.liveData1kEntry.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String url) {
                // 一键进入 HiWallet钱包
                hiWalletUrl = url;
            }
        });
        viewModel.liveDataRechargeList.observe(getViewLifecycleOwner(), new Observer<List<RechargeVo>>() {
            @Override
            public void onChanged(List<RechargeVo> list) {
                rechargeAdapter.clear();
                rechargeAdapter.addAll(list);

                // 提前查询需要跳转第三方的详情(链接)
                for (RechargeVo vo : list) {
                    if (vo.op_thiriframe_use && !vo.phone_needbind) {
                        CfLog.d(vo.title + ", jump: " + vo.op_thiriframe_url);
                        // 调详情接口获取 跳转链接
                        viewModel.getPayment(vo.bid);
                    }
                }
            }
        });
        viewModel.liveDataTutorial.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String url) {
                tutorialUrl = url;
            }
        });

        viewModel.liveDataRecharge.observe(getViewLifecycleOwner(), new Observer<RechargeVo>() {
            @Override
            public void onChanged(RechargeVo vo) {
                CfLog.d(vo.toString());
                mapRechargeVo.put(vo.bid, vo);
                SPUtils.getInstance().put(SPKeyGlobal.RC_PAYMENT_THIRIFRAME, new Gson().toJson(mapRechargeVo));
            }
        });
        viewModel.liveDataRechargePay.observe(getViewLifecycleOwner(), new Observer<RechargePayVo>() {
            @Override
            public void onChanged(RechargePayVo vo) {
                CfLog.i(vo.payname + ", jump: " + vo.redirecturl);
                // 点下一步 跳转到充值结果 弹窗
                new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), vo.payname, vo.redirecturl)).show();
            }
        });

    }

    private List<String> getFastMoney(String loadMin, String loadMax) {
        return getFastMoney(Integer.parseInt(loadMin), Integer.parseInt(loadMax));
    }

    private List<String> getFastMoney(int loadMin, int loadMax) {
        List<String> list = new ArrayList<>();
        list.add(getFastMoney(0.1d, loadMin, loadMax) + "");
        list.add(getFastMoney(0.3d, loadMin, loadMax) + "");
        list.add(getFastMoney(0.5d, loadMin, loadMax) + "");
        list.add(loadMax + "");
        return list;
    }

    private int getFastMoney(double rate, int loadMin, int loadMax) {
        int min = Math.max(loadMin, 10);
        int max = Math.max(loadMax, 10);
        int dest = max - min;
        double value = (max - min) * rate + min;
        double money = 0;
        int amount = 0;
        if (dest <= 3000) {
            amount = (int) (Math.round(value / 100) * 100);
        } else {
            String str = String.valueOf(value);
            int length = str.indexOf(".");
            money = Math.round(value / Math.pow(10, length - 2)) * Math.pow(10, length - 2);
            amount = (int) (Math.round(money / 100) * 100);
        }
        //CfLog.d("amount: " + amount);
        return amount;
    }

}
