package com.xtree.mine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityMyWalletBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BalanceVo;
import com.xtree.mine.vo.GameBalanceVo;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.ContainerActivity;

@Route(path = RouterActivityPath.Mine.PAGER_MY_WALLET)
public class MyWalletActivity extends BaseActivity<ActivityMyWalletBinding, MyWalletViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        binding.tvwWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.d("************");
                startActivity(new Intent(getBaseContext(), WithdrawActivity.class));
            }
        });
        binding.tvwMgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.d("************");

            }
        });
        binding.tvwRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CfLog.d("************");

            }
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

    private void goRecharge() {
        Intent intent = new Intent(getBaseContext(), ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Recharge.PAGER_RECHARGE);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowBack", true);
        intent.putExtra(ContainerActivity.BUNDLE, bundle);
        startActivity(intent);
    }

}
