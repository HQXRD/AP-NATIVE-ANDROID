package com.xtree.mine.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.xtree.base.global.Constant;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityMyWalletBinding;
import com.xtree.mine.ui.fragment.AccountMgmtDialog;
import com.xtree.mine.ui.fragment.MyWalletAdapter;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.GameMenusVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;

@Route(path = RouterActivityPath.Mine.PAGER_MY_WALLET)
public class MyWalletActivity extends BaseActivity<ActivityMyWalletBinding, MyWalletViewModel> {
    private final static int MSG_GAME_BALANCE = 1004;
    private List<GameMenusVo> walletGameList = new ArrayList<>();
    private List<GameBalanceVo> transGameBalanceList = new ArrayList<>();
    private int count = 0;

    private MyWalletAdapter myWalletAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.readCache(); // 读取缓存

        viewModel.getBalance(); // 平台中心余额

        viewModel.getTransThirdGameType(this);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GAME_BALANCE:
                    for (GameMenusVo vo : walletGameList) {
                        viewModel.getGameBalance(vo.key);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_my_wallet;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> finish());
        binding.ivwRefreshBlc.setOnClickListener(v -> {
            viewModel.getBalance();
            //Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_loading);
            //binding.ivwRefreshBlc.startAnimation(animation);
        });
        binding.ivwGoDeposit.setOnClickListener(v -> {
            CfLog.d("************");
            goRecharge();
        });
        binding.tvwDeposit.setOnClickListener(v -> {
            CfLog.d("************");
            goRecharge();
        });
        binding.tvwTrans.setOnClickListener(v -> {
            CfLog.d("************");
            startContainerFragment(RouterFragmentPath.Wallet.PAGER_TRANSFER);
        });
        binding.tvwWithdraw.setOnClickListener(v -> {
            CfLog.d("************");
            //startActivity(new Intent(getBaseContext(), WithdrawActivity.class));
            //startContainerFragment(RouterFragmentPath.Wallet.PAGER_WITHDRAW);
            goWebView(v, Constant.URL_WITHDRAW);
        });
        binding.tvwMgmt.setOnClickListener(v -> {
            CfLog.d("************");
            new XPopup.Builder(this).asCustom(new AccountMgmtDialog(this)).show();
        });
        binding.tvwRecord.setOnClickListener(v -> {
            CfLog.d("************");
            //goWebView(v, Constant.URL_DW_RECORD);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_RECHARGE_WITHDRAW); // 充提记录
        });

        int spanCount = 4; // 每行的列数
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        binding.rcvWalletDetails.setLayoutManager(layoutManager);
    }

    @Override
    public MyWalletViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return new ViewModelProvider(this, factory).get(MyWalletViewModel.class);
    }

    @Override
    public void initData() {
        //super.initData();
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataBalance.observe(this, vo -> {
            binding.tvwBalance.setText(vo.balance);
            binding.ivwRefreshBlc.clearAnimation();
        });

        viewModel.liveDataGameBalance.observe(this, vo -> {
            transGameBalanceList.add(vo);
            count++;

            CfLog.d("count : " + count + " walletGameList.size() : " + walletGameList.size());
            if (count >= walletGameList.size()) {
                Collections.sort(transGameBalanceList);
                myWalletAdapter = new MyWalletAdapter(this);
                binding.rcvWalletDetails.setAdapter(myWalletAdapter);
                myWalletAdapter.setData(transGameBalanceList);
            }
        });

        viewModel.liveDataGameBalance.observe(this, vo -> {
            transGameBalanceList.add(vo);
            count++;

            if (count >= walletGameList.size()) {
                Collections.sort(transGameBalanceList);
                myWalletAdapter = new MyWalletAdapter(this);
                binding.rcvWalletDetails.setAdapter(myWalletAdapter);
                myWalletAdapter.setData(transGameBalanceList);
            }
        });

        viewModel.listSingleLiveData.observe(this, vo -> {
            myWalletAdapter = new MyWalletAdapter(this);
            binding.rcvWalletDetails.setAdapter(myWalletAdapter);
            Collections.sort(vo);
            myWalletAdapter.setData(vo);
        });

        viewModel.liveDataTransGameType.observe(this, list -> {
            walletGameList = list;

            // 某个场馆的余额
            mHandler.sendEmptyMessage(MSG_GAME_BALANCE);
        });
    }

    private void goWebView(View v, String path) {
        String title = ((TextView) v).getText().toString();
        String url = DomainUtil.getDomain2() + path;
        BrowserActivity.start(this, title, url, true);
    }

    private void goRecharge() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowBack", true);
        startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle);
    }

}
