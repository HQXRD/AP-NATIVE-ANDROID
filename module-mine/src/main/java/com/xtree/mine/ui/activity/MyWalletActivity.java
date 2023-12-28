package com.xtree.mine.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;

import me.xtree.mvvmhabit.base.BaseActivity;

@Route(path = RouterActivityPath.Mine.PAGER_MY_WALLET)
public class MyWalletActivity extends BaseActivity<ActivityMyWalletBinding, MyWalletViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.readCache(); // 读取缓存

        viewModel.getBalance(); // 平台中心余额

        // 某个场馆的余额
        viewModel.getGameBalance("pt");
        viewModel.getGameBalance("bbin");
        viewModel.getGameBalance("ag");
        viewModel.getGameBalance("obgdj");
        viewModel.getGameBalance("yy");
        viewModel.getGameBalance("obgqp");
    }

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
        binding.ivwRefreshBlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getBalance();
                //Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_loading);
                //binding.ivwRefreshBlc.startAnimation(animation);
            }
        });
        binding.ivwGoDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.d("************");
                goRecharge();
            }
        });
        binding.tvwDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.d("************");
                goRecharge();
            }
        });
        binding.tvwTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.d("************");
                startContainerFragment(RouterFragmentPath.Wallet.PAGER_TRANSFER);
            }
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
            goWebView(v, Constant.URL_DW_RECORD);
        });

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
        viewModel.liveDataBalance.observe(this, new Observer<BalanceVo>() {
            @Override
            public void onChanged(BalanceVo vo) {
                binding.tvwBalance.setText(vo.balance);
                binding.ivwRefreshBlc.clearAnimation();
            }
        });

        viewModel.liveDataGameBalance.observe(this, new Observer<GameBalanceVo>() {
            @Override
            public void onChanged(GameBalanceVo vo) {
                TextView tvw = binding.llWallet.findViewWithTag(vo.gameAlias);
                if (tvw != null) {
                    tvw.setText(vo.balance);
                }
            }
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
