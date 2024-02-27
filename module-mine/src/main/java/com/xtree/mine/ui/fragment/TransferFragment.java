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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentTransferBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.GameMenusVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterFragmentPath.Wallet.PAGER_TRANSFER)
public class TransferFragment extends BaseFragment<FragmentTransferBinding, MyWalletViewModel> {
    private List<GameBalanceVo> transGameBalanceList = new ArrayList<>();
    private List<GameBalanceVo> transGameBalanceWithOwnList = new ArrayList<>();
    private List<GameMenusVo> transGameList = new ArrayList<>();
    private List<GameBalanceVo> hasMoneyGame = new ArrayList<>();
    private final static int MSG_GAME_BALANCE_NO_ZERO = 1001;
    private final static int MSG_GAME_BALANCE = 1002;
    private final static int MSG_UPDATE_RCV = 1003;
    private int count = 0;
    private boolean filterNoMoney = false;
    private boolean isAutoTransfer = false; // 调用接口前标记/接口返回后使用

    HashMap<String, GameBalanceVo> map = new HashMap<>();
    ArrayList<GameBalanceVo> listGameBalance = new ArrayList<>();
    BalanceVo mBalanceVo = new BalanceVo("0"); // 中心钱包
    BasePopupView ppw = null; // 底部弹窗
    BasePopupView loading = null;
    TransferBalanceAdapter mTransferBalanceAdapter;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GAME_BALANCE_NO_ZERO:
                    setGameBalanceNoZero();
                    break;
                case MSG_GAME_BALANCE:
                    if (loading == null || !loading.isShow()) {
                        showLoading();
                    }
                    for (GameMenusVo vo : transGameList) {
                        viewModel.getGameBalance(vo.key);
                    }
                    break;
                case MSG_UPDATE_RCV:
                    if (filterNoMoney) {
                        mTransferBalanceAdapter.setData(hasMoneyGame);
                    } else {
                        mTransferBalanceAdapter.setData(transGameBalanceList);
                    }
                    mTransferBalanceAdapter.notifyDataSetChanged();
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
        viewModel.readCache(); // 先读取缓存数据
        refreshBalance();
    }

    private void refreshBalance() {
        count = 0;
        transGameList = new ArrayList<>();
        transGameBalanceList = new ArrayList<>();
        transGameBalanceWithOwnList = new ArrayList<>();
        viewModel.getBalance(); // 平台中心余额
        viewModel.getTransThirdGameType(getContext()); // 获取馆场资讯
    }

    @Override
    public void initView() {
        int auto_thrad_status = SPUtils.getInstance().getInt(SPKeyGlobal.USER_AUTO_THRAD_STATUS);
        if (auto_thrad_status == 1) {
            binding.llCenterWallet.ckbAuto.setChecked(true);
            isAutoTransfer = true;
            binding.llTransfer.setVisibility(View.GONE);
        } else {
            binding.llCenterWallet.ckbAuto.setChecked(false);
            isAutoTransfer = false;
            binding.llTransfer.setVisibility(View.VISIBLE);
        }

        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.ivwMsg.setOnClickListener(v -> startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG));

        binding.ivwCs.setOnClickListener(v -> {
            String title = getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).moveUpToKeyboard(false).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });

        binding.llCenterWallet.tvw1kRecycle.setOnClickListener(v -> {
            showDialog1kAutoRecycle();
        });

        binding.llCenterWallet.ivwAuto.setOnClickListener(v -> {
            if (!binding.llCenterWallet.ckbAuto.isChecked()) {
                showDialogAutoTrans();
            } else {
                // 关闭自动免转, 不用弹窗提示
                isAutoTransfer = false;
                binding.llCenterWallet.ckbAuto.setChecked(false);
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

        showLoading();
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
                showLoading();
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
                isAutoTransfer = true;
                binding.llCenterWallet.ckbAuto.setChecked(true);
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

        WalletRoomAdapter adapter = new WalletRoomAdapter(getContext(), alias, vo -> {
            tvw.setText(vo.gameName);
            tvw.setTag(vo.gameAlias);
            ppw.dismiss();
        });
        Collections.sort(transGameBalanceWithOwnList);
        adapter.addAll(transGameBalanceWithOwnList);

        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), title, adapter, 75));
        ppw.show();
    }

    @Override
    public void initData() {
        binding.rcvAllGameBalance.setLayoutManager(new GridLayoutManager(getContext(), 3));

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
        viewModel.liveDataBalance.observe(this, vo -> {
            binding.llCenterWallet.tvwBalance.setText(vo.balance);
            mBalanceVo = vo;
        });

        viewModel.liveDataGameBalance.observe(this, vo -> {
            if (transGameBalanceWithOwnList.size() == 0) {
                transGameBalanceWithOwnList.add(new GameBalanceVo("lottery", getString(R.string.txt_central_wallet), 0, mBalanceVo.balance));
            }
            for (int i = 0; i < transGameBalanceList.size(); i++) {
                if (transGameBalanceList.get(i).equals(vo.gameAlias)) {
                    return;
                }
            }

            transGameBalanceList.add(vo);
            transGameBalanceWithOwnList.add(vo);

            map.put(vo.gameAlias, vo);
            count++;

            CfLog.d("count : " + count + " transGameList.size() : " + transGameList.size());
            if (count >= transGameList.size()) {
                Collections.sort(transGameBalanceList);
                mTransferBalanceAdapter = new TransferBalanceAdapter(getContext());
                binding.rcvAllGameBalance.setAdapter(mTransferBalanceAdapter);
                mTransferBalanceAdapter.setData(transGameBalanceList);
                loading.dismiss();
            }

            // 这里是要处理转账前,如果勾选了'隐藏无余额场馆'这种情况
            if (binding.ckbHide.isChecked()) {
                //setGameBalanceNoZero(); // 如果直接调用,会有6个场馆要调用6次出现卡顿,改用Handler
                mHandler.removeMessages(MSG_GAME_BALANCE_NO_ZERO);
                mHandler.sendEmptyMessageDelayed(MSG_GAME_BALANCE_NO_ZERO, 1000L);
            }
        });

        viewModel.listSingleLiveData.observe(this, vo -> {
            mTransferBalanceAdapter = new TransferBalanceAdapter(getContext());
            binding.rcvAllGameBalance.setAdapter(mTransferBalanceAdapter);
            Collections.sort(vo);
            mTransferBalanceAdapter.setData(vo);

            // 这里是要处理转账前,如果勾选了'隐藏无余额场馆'这种情况
            if (binding.ckbHide.isChecked()) {
                //setGameBalanceNoZero(); // 如果直接调用,会有6个场馆要调用6次出现卡顿,改用Handler
                mHandler.removeMessages(MSG_GAME_BALANCE_NO_ZERO);
                mHandler.sendEmptyMessageDelayed(MSG_GAME_BALANCE_NO_ZERO, 1000L);
            }
        });

        viewModel.liveDataTransfer.observe(this, isSuccess -> {
            if (isSuccess) {
                ToastUtils.showLong(R.string.txt_transfer_success);
                refreshBalance();
            } else {
                ToastUtils.showLong(R.string.txt_transfer_fail);
            }
        });

        viewModel.liveDataAutoTrans.observe(this, isSuccess -> {
            CfLog.i("isSuccess: " + isSuccess);
            if (isSuccess) {
                ToastUtils.showLong(R.string.txt_set_succ);
                binding.llTransfer.setVisibility(View.GONE);
                if (isAutoTransfer) {
                    binding.llTransfer.setVisibility(View.GONE);
                } else {
                    binding.llTransfer.setVisibility(View.VISIBLE);
                }
                refreshBalance();
                // 加入缓存
                int auto_thrad_status = binding.llCenterWallet.ckbAuto.isChecked() ? 1 : 0; // 1-开启,2-关闭
                SPUtils.getInstance().put(SPKeyGlobal.USER_AUTO_THRAD_STATUS, auto_thrad_status);
            } else {
                ToastUtils.showLong(R.string.txt_set_fail);
                // 设置失败,退回去
                isAutoTransfer = !isAutoTransfer;
                binding.llCenterWallet.ckbAuto.setChecked(isAutoTransfer);
                if (isAutoTransfer) {
                    binding.llTransfer.setVisibility(View.GONE);
                } else {
                    binding.llTransfer.setVisibility(View.VISIBLE);
                }
            }

        });

        viewModel.liveData1kRecycle.observe(this, isSuccess -> {
            if (isSuccess) {
                ToastUtils.showLong(R.string.txt_recycle_succ);
                refreshBalance();
            } else {
                ToastUtils.showLong(R.string.txt_recycle_fail);
                loading.dismiss();
            }
        });

        viewModel.liveDataTransGameType.observe(this, list -> {
            transGameList = list;

            // 某个场馆的余额
            mHandler.sendEmptyMessage(MSG_GAME_BALANCE);
        });

    }

    /**
     * 显示所有的
     */
    private void setGameBalanceAll() {
        listGameBalance.clear();
        listGameBalance.addAll(map.values());
        showGameBlc();
    }

    /**
     * 去掉无余额的
     */
    private void setGameBalanceNoZero() {

        listGameBalance.clear();
        listGameBalance.addAll(map.values());

        for (int i = listGameBalance.size() - 1; i >= 0; i--) {
            try {
                if (Double.parseDouble(listGameBalance.get(i).balance) == 0.0d) {
                    listGameBalance.remove(i);
                }
            } catch (NumberFormatException e) {
                listGameBalance.remove(i);
            }
        }
        showGameBlc();
    }

    private void showGameBlc() {
        hasMoneyGame = new ArrayList<>();
        for (int i = 0; i < transGameBalanceList.size(); i++) {
            try {
                if (Double.parseDouble(transGameBalanceList.get(i).balance) > 0) {
                    hasMoneyGame.add(transGameBalanceList.get(i));
                }
            } catch (NumberFormatException e) {

            }
        }
        Collections.sort(hasMoneyGame);
        filterNoMoney = !filterNoMoney;
        mHandler.sendEmptyMessage(MSG_UPDATE_RCV);
    }

    private void showLoading() {
        loading = new XPopup.Builder(getContext()).asCustom(new LoadingDialog(getContext()));
        loading.show();
    }
}