package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentTransferBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.ProfileVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterFragmentPath.Wallet.PAGER_TRANSFER)
public class TransferFragment extends BaseFragment<FragmentTransferBinding, MyWalletViewModel> {

    private final static int MSG_GAME_BALANCE_NO_ZERO = 1001;

    HashMap<String, GameBalanceVo> map = new HashMap<>();
    ArrayList<GameBalanceVo> listGameBalance = new ArrayList<>();
    BalanceVo mBalanceVo = new BalanceVo("0"); // 中心钱包
    TextView[] arrayName; // 场馆名称
    TextView[] arrayBlc; // 场馆余额
    BasePopupView ppw = null; // 底部弹窗

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GAME_BALANCE_NO_ZERO:
                    setGameBalanceNoZero();
                    break;
                default:
                    break;
            }
        }
    };

    public TransferFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //viewModel.readCache(); // 先读取缓存数据
        refreshBalance();
    }

    private void refreshBalance() {
        viewModel.getBalance(); // 平台中心余额
        // 某个场馆的余额
        viewModel.getGameBalance("pt");
        viewModel.getGameBalance("bbin");
        viewModel.getGameBalance("ag");
        viewModel.getGameBalance("obgdj");
        viewModel.getGameBalance("yy");
        viewModel.getGameBalance("obgqp");
        //viewModel.getGameBalance("shaba"); // 沙巴体育
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

        binding.ckbHide.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setGameBalanceNoZero();
            } else {
                setGameBalanceAll();
            }
        });

        binding.tvwFrom.setOnClickListener(v -> {
            String title = getString(R.string.txt_choose_trans_out);
            String alias = v.getTag() + "";
            showDialogChoose(title, alias, (TextView) v);
        });

        binding.tvwTo.setOnClickListener(v -> {
            String title = getString(R.string.txt_choose_trans_in);
            String alias = v.getTag() + "";
            showDialogChoose(title, alias, (TextView) v);
        });

        binding.ivwSwitch.setOnClickListener(v -> {
            String tmpAlias = binding.tvwFrom.getTag().toString();
            String tmpTitle = binding.tvwFrom.getText().toString();

            binding.tvwFrom.setTag(binding.tvwTo.getTag());
            binding.tvwFrom.setText(binding.tvwTo.getText());
            binding.tvwTo.setTag(tmpAlias);
            binding.tvwTo.setText(tmpTitle);
        });

        binding.rgpAmount.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rdo = group.findViewById(checkedId);
            if (rdo != null) {
                String txt = rdo.getTag().toString();
                if (txt.equalsIgnoreCase("MAX")) {
                    binding.edtAmount.setText(mBalanceVo.balance);
                } else {
                    binding.edtAmount.setText(txt);
                }
            }
        });

        binding.ivwTransfer.setOnClickListener(v -> doTransfer());

    }

    private void doTransfer() {
        String from = binding.tvwFrom.getTag().toString();
        String to = binding.tvwTo.getTag().toString();
        String money = binding.edtAmount.getText().toString();

        if (from.equals(to)) {
            ToastUtils.showLong(R.string.txt_in_out_cannot_be_same);
            return;
        }
        if (money.isEmpty()) {
            ToastUtils.showLong(R.string.txt_amount_cannot_empty);
            return;
        }

        double a = Double.parseDouble(money); // 想要转出的金额
        double b = 0; // 实际可用的余额
        if ("lottery".equals(from)) {
            b = Double.parseDouble(mBalanceVo.balance);
        } else if (map.containsKey(from)) {
            b = Double.parseDouble(map.get(from).balance);
        } else {
            CfLog.e("****** error, balance is 0...");
        }
        if (a > b) {
            ToastUtils.showLong(binding.tvwFrom.getText() + getString(R.string.txt_insufficient_balance));
            return;
        }

        HashMap map = new HashMap();
        map.put("from", from);
        map.put("to", to);
        map.put("money", money);
        map.put("nonce", UuidUtil.getID16());
        viewModel.doTransfer(map);
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

    private void showDialogChoose(String title, String alias, TextView tvw) {

        WalletRoomAdapter adapter = new WalletRoomAdapter(getContext(), alias, new WalletRoomAdapter.ICallBack() {
            @Override
            public void onClick(GameBalanceVo vo) {
                tvw.setText(vo.gameName);
                tvw.setTag(vo.gameAlias);
                ppw.dismiss();
            }
        });

        ArrayList<GameBalanceVo> list = new ArrayList<>();
        list.add(new GameBalanceVo("lottery", getString(R.string.txt_central_wallet), 0, mBalanceVo.balance));

        list.addAll(map.values());
        Collections.sort(list);
        adapter.addAll(list);

        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), title, adapter, 75));
        ppw.show();
    }

    @Override
    public void initData() {
        arrayName = new TextView[]{binding.tvwName01, binding.tvwName02, binding.tvwName03,
                binding.tvwName04, binding.tvwName05, binding.tvwName06};
        arrayBlc = new TextView[]{binding.tvwBlc01, binding.tvwBlc02, binding.tvwBlc03,
                binding.tvwBlc04, binding.tvwBlc05, binding.tvwBlc06};

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_transfer;
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
                mBalanceVo = vo;
            }
        });

        viewModel.liveDataGameBalance.observe(this, new Observer<GameBalanceVo>() {
            @Override
            public void onChanged(GameBalanceVo vo) {
                TextView tvw = binding.llWallet.findViewWithTag(vo.gameAlias);
                if (tvw != null) {
                    tvw.setText(vo.balance);
                }
                //setGameBalance(vo);
                map.put(vo.gameAlias, vo);

                // 这里是要处理转账前,如果勾选了'隐藏无余额场馆'这种情况
                if (binding.ckbHide.isChecked()) {
                    //setGameBalanceNoZero(); // 如果直接调用,会有6个场馆要调用6次出现卡顿,改用Handler
                    mHandler.removeMessages(MSG_GAME_BALANCE_NO_ZERO);
                    mHandler.sendEmptyMessageDelayed(MSG_GAME_BALANCE_NO_ZERO, 1000L);
                }
            }
        });
        viewModel.liveDataTransfer.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    ToastUtils.showLong(R.string.txt_transfer_success);
                    refreshBalance();
                } else {
                    ToastUtils.showLong(R.string.txt_transfer_fail);
                }

            }
        });
        viewModel.liveDataAutoTrans.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    ToastUtils.showLong(R.string.txt_set_succ);
                    refreshBalance();
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
                    refreshBalance();
                } else {
                    ToastUtils.showLong(R.string.txt_recycle_fail);
                }
            }
        });

    }

    /**
     * 显示所有的
     */
    private void setGameBalanceAll() {
        listGameBalance.clear();
        listGameBalance.addAll(map.values());
        Collections.sort(listGameBalance);
        showGameBlc();
    }

    /**
     * 去掉无余额的
     */
    private void setGameBalanceNoZero() {

        listGameBalance.clear();
        listGameBalance.addAll(map.values());
        Collections.sort(listGameBalance);

        for (int i = listGameBalance.size() - 1; i >= 0; i--) {
            if (Double.parseDouble(listGameBalance.get(i).balance) == 0.0d) {
                listGameBalance.remove(i);
            }
        }

        showGameBlc();

        if (listGameBalance.size() > 3) {
            binding.llRow1.setVisibility(View.VISIBLE);
            binding.llRow2.setVisibility(View.VISIBLE);
        } else if (listGameBalance.size() > 0) {
            binding.llRow1.setVisibility(View.VISIBLE);
            binding.llRow2.setVisibility(View.GONE);
        } else {
            binding.llRow1.setVisibility(View.GONE);
            binding.llRow2.setVisibility(View.GONE);
        }
    }

    private void showGameBlc() {
        binding.llRow1.setVisibility(View.VISIBLE);
        binding.llRow2.setVisibility(View.VISIBLE);

        for (int i = 0; i < arrayName.length; i++) {
            if (i < listGameBalance.size()) {
                arrayName[i].setText(listGameBalance.get(i).gameName);
                if (Double.parseDouble(listGameBalance.get(i).balance) > 0) {
                    arrayBlc[i].setText(listGameBalance.get(i).balance);
                } else {
                    arrayBlc[i].setText("");
                }
            } else {
                arrayName[i].setText(" ");
                arrayBlc[i].setText(" ");
            }
        }
    }

}