package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.BaseMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.UuidUtil;
import com.xtree.base.vo.BalanceVo;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentTransferBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AwardsRecordVo;
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
    private List<GameBalanceVo> transGameBalanceOnlyCentral = new ArrayList<>(); // 只有中心钱包
    private List<GameBalanceVo> transGameBalanceNoCentral = new ArrayList<>(); // 没有中心钱包
    private final static int MSG_GAME_BALANCE_NO_ZERO = 1001;
    private final static int MSG_GAME_BALANCE = 1002;
    private final static int MSG_UPDATE_RCV = 1003;
    private int count = 0;
    private boolean filterNoMoney = false;
    private boolean isAutoTransfer = false; // 调用接口前标记/接口返回后使用
    private AwardsRecordVo awardsRecordVo;//礼物流水
    private boolean isNetworkAwards = false;//礼物流水网络请求是否已刷新标志位

    HashMap<String, GameBalanceVo> map = new HashMap<>();
    ArrayList<GameBalanceVo> listGameBalance = new ArrayList<>();
    BalanceVo mBalanceVo = new BalanceVo("0"); // 中心钱包
    BasePopupView ppw = null; // 底部弹窗
    TransferBalanceAdapter mTransferBalanceAdapter;
    private AmountViewViewAdapter amountViewViewAdapter;
    private ArrayList<AmountVo> amountVoArrayList = new ArrayList<>();
    private boolean isMax = false ;
    private String maxInput ;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GAME_BALANCE_NO_ZERO:
                    setGameBalanceNoZero();
                    break;
                case MSG_GAME_BALANCE:
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
        viewModel.getAwardRecord();//获取礼物流水
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
        binding.ivwTransfer.setEnabled(false);

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

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));

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
            if (ClickUtil.isFastClick()) {
                return;
            }
            if (transGameBalanceWithOwnList.size() > transGameList.size()) {
                String title = getString(R.string.txt_choose_trans_out);
                String alias = v.getTag() + "";
                showDialogChoose(title, alias, (TextView) v);
            }
        });

        binding.tvwTo.setOnClickListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            if (transGameBalanceWithOwnList.size() > transGameList.size()) {
                String title = getString(R.string.txt_choose_trans_in);
                String alias = v.getTag() + "";
                showDialogChoose(title, alias, (TextView) v);
            }
        });

        binding.ivwSwitch.setOnClickListener(v -> {
            String tmpAlias = binding.tvwFrom.getTag().toString();
            String tmpTitle = binding.tvwFrom.getText().toString();

            binding.tvwFrom.setTag(binding.tvwTo.getTag());
            binding.tvwFrom.setText(binding.tvwTo.getText());
            binding.tvwTo.setTag(tmpAlias);
            binding.tvwTo.setText(tmpTitle);
        });

        initAmountList();
        amountViewViewAdapter = new AmountViewViewAdapter(getContext(), amountVoArrayList, new IAmountCallback() {
            @Override
            public void callbackWithAmount(String txt) {
                if (txt.equalsIgnoreCase("MAX")) {
                    isMax = true;
                    String fromString = binding.tvwFrom.getText().toString().trim();
                    if (TextUtils.equals("中心钱包", fromString)) {
                        binding.edtAmount.setText(mBalanceVo.balance);
                        maxInput = mBalanceVo.balance;
                    } else {
                        for (int i = 0; i < transGameBalanceList.size(); i++) {
                            if (TextUtils.equals(fromString, transGameBalanceList.get(i).gameName)) {
                                if (!TextUtils.equals("获取余额失败", transGameBalanceList.get(i).balance)) {
                                    binding.edtAmount.setText(transGameBalanceList.get(i).balance);
                                } else {
                                    binding.edtAmount.setText("0");
                                }
                            }
                        }
                    }
                } else {
                    isMax = false ;
                    binding.edtAmount.setText(txt);
                }
            }
        });
        binding.rgpAmount.setAdapter(amountViewViewAdapter);

        binding.ivwTransfer.setOnClickListener(v -> doTransfer());

        binding.edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim().toString();
                if (input.length() > 0) {
                    // 设置光标位置
                    binding.edtAmount.setSelection(input.length());
                    if (TextUtils.equals(input, "100")){
                        amountVoArrayList.get(0).flag = true;
                        referAmountArray(amountVoArrayList.get(0) ,amountVoArrayList);
                        amountViewViewAdapter.notifyDataSetChanged();
                    } else if (TextUtils.equals(input ,"1000")) {
                        amountVoArrayList.get(1).flag = true;
                        referAmountArray(amountVoArrayList.get(1) ,amountVoArrayList);
                        amountViewViewAdapter.notifyDataSetChanged();
                    } else if (TextUtils.equals( input, "10000")) {
                        amountVoArrayList.get(2).flag = true;
                        referAmountArray(amountVoArrayList.get(2) ,amountVoArrayList);
                        amountViewViewAdapter.notifyDataSetChanged();
                    } else if (TextUtils.equals(input ,maxInput)) {
                        amountVoArrayList.get(3).flag = true;
                        referAmountArray(amountVoArrayList.get(3) ,amountVoArrayList);
                        amountViewViewAdapter.notifyDataSetChanged();
                    } else {
                        referArray(amountVoArrayList);
                        amountViewViewAdapter.notifyDataSetChanged();
                    }
                } else if (input == null || input.length() == 0) {
                    referArray(amountVoArrayList);
                    amountViewViewAdapter.notifyDataSetChanged();
                }
                binding.ivwTransfer.setEnabled(!binding.edtAmount.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 屏蔽双击选中、长按选中
        binding.edtAmount.setMovementMethod(new BaseMovementMethod());
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

        HashMap map = new HashMap();
        map.put("from", from);
        map.put("to", to);
        map.put("money", money);
        map.put("nonce", UuidUtil.getID16());
        LoadingDialog.show(getActivity());
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
        transGameBalanceNoCentral.clear();
        transGameBalanceOnlyCentral.clear();

        Collections.sort(transGameBalanceWithOwnList);
        transGameBalanceOnlyCentral.add(transGameBalanceWithOwnList.get(0)); // 排列完后gameAlias为lottery的放到第一个

        for (GameBalanceVo vo : transGameBalanceWithOwnList) {
            if (!vo.gameAlias.equals("lottery")) {
                transGameBalanceNoCentral.add(vo);
            }
        }

        Collections.sort(transGameBalanceNoCentral);

        WalletRoomAdapter adapter = new WalletRoomAdapter(getContext(), alias, vo -> {
            if (!vo.gameAlias.equals("lottery") && tvw == binding.tvwFrom) {
                binding.tvwTo.setText(getString(R.string.txt_central_wallet));
                binding.tvwTo.setTag("lottery");
            } else if (vo.gameAlias.equals("lottery") && tvw == binding.tvwFrom) {
                binding.tvwTo.setText(transGameBalanceNoCentral.get(0).gameName);
                binding.tvwTo.setTag(transGameBalanceNoCentral.get(0).gameAlias);
            }
            tvw.setText(vo.gameName);
            tvw.setTag(vo.gameAlias);
            ppw.dismiss();
        });
        CfLog.e("binding.tvwFrom.getTag() : " + binding.tvwFrom.getTag());
        if (binding.tvwFrom.getTag().equals("lottery") && tvw == binding.tvwTo) {
            adapter.addAll(transGameBalanceNoCentral);
        } else if (binding.tvwTo.getTag().equals("lottery") && tvw == binding.tvwTo) {
            adapter.addAll(transGameBalanceOnlyCentral);
        } else {
            adapter.addAll(transGameBalanceWithOwnList);
        }

        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), title, adapter, 75, true));
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
                if (binding.ckbHide.isChecked()) {
                    setGameBalanceNoZero();
                }
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
            }
        });

        viewModel.liveDataTransGameType.observe(this, list -> {
            transGameList = list;

            // 某个场馆的余额
            mHandler.sendEmptyMessage(MSG_GAME_BALANCE);
        });

        //获取礼物流水
        viewModel.awardrecordVoMutableLiveData.observe(this, vo -> {
            awardsRecordVo = vo;
            isNetworkAwards = true;//增加网络回调标识
            if (awardsRecordVo != null && awardsRecordVo.list.size() > 0) {
                binding.llCenterWallet.tvwLockBalance.setText(awardsRecordVo.locked_award_sum);
            } else {
                binding.llCenterWallet.tvwLockBalance.setText("0.0000");
            }
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

    private class AmountVo {
        public String amount;
        public boolean flag;
    }

    /**
     * 定义GridViewViewAdapter 显示大额固额金额选择
     */
    private static class AmountViewViewAdapter extends BaseAdapter {
        public IAmountCallback callback;
        private Context context;
        public ArrayList<AmountVo> arrayList;

        public AmountViewViewAdapter(Context context, ArrayList<AmountVo> list, IAmountCallback callback) {
            super();
            this.context = context;
            this.arrayList = list;
            this.callback = callback;
        }

        @Override
        public int getCount() {
            return this.arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            HolderView holderView = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.transfer_amount, parent, false);
                holderView = new HolderView();
                holderView.textView = (TextView) view.findViewById(R.id.tvw_title);
                view.setTag(holderView);
            } else {
                holderView = (HolderView) view.getTag();
            }
            if (arrayList.get(position).flag) {
                holderView.textView.setBackgroundResource(R.mipmap.cm_ic_bg_selected);
            } else {
                holderView.textView.setBackgroundResource(R.mipmap.ic_bg_blc);
            }
            holderView.textView.setText(arrayList.get(position).amount);
            holderView.getTextView().setOnClickListener(v -> {

                referInlineArray(arrayList.get(position), arrayList);
                notifyDataSetChanged();
                if (callback != null) {
                    callback.callbackWithAmount(arrayList.get(position).amount);
                }
            });
            return view;
        }

        private void referInlineArray(AmountVo vo, ArrayList<AmountVo> arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).amount.equals(vo.amount)) {
                    arrayList.get(i).flag = true;
                } else {
                    arrayList.get(i).flag = false;
                }

            }
        }

        private class HolderView {
            private String showAmount;

            public void setShowAmount(String showAmount) {
                this.showAmount = showAmount;
                this.textView.setText(showAmount);
            }

            public String getShowAmount() {
                return showAmount;
            }

            private TextView textView;
            private LinearLayout linear;

            public void setTextView(TextView textView) {
                this.textView = textView;
            }

            public TextView getTextView() {
                return textView;
            }
        }
    }

    private void referArray(ArrayList<AmountVo> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).flag = false;
        }
    }
    private void  referAmountArray(AmountVo vo ,ArrayList<AmountVo> arrayList){
        for (int i = 0; i < arrayList.size(); i++) {

            if (arrayList.get(i).amount.equals(vo.amount)) {
                arrayList.get(i).flag = true;
            } else {
                arrayList.get(i).flag = false;
            }
        }
    }
    private void initAmountList() {
        AmountVo vo1 = new AmountVo();
        vo1.amount = 100 + "";
        vo1.flag = false;

        AmountVo vo2 = new AmountVo();
        vo2.amount = 1000 + "";
        vo2.flag = false;

        AmountVo vo3 = new AmountVo();
        vo3.amount = 10000 + "";
        vo3.flag = false;

        AmountVo vo4 = new AmountVo();
        vo4.amount = "MAX";
        vo4.flag = false;

        amountVoArrayList.add(vo1);
        amountVoArrayList.add(vo2);
        amountVoArrayList.add(vo3);
        amountVoArrayList.add(vo4);
    }
}