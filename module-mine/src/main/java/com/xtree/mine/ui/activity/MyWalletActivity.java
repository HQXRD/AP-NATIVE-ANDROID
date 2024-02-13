package com.xtree.mine.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityMyWalletBinding;
import com.xtree.mine.ui.fragment.AccountMgmtDialog;
import com.xtree.mine.ui.fragment.MyWalletAdapter;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.GameMenusVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterActivityPath.Mine.PAGER_MY_WALLET)
public class MyWalletActivity extends BaseActivity<ActivityMyWalletBinding, MyWalletViewModel> {
    private final static int MSG_GAME_BALANCE = 1004;
    private List<GameMenusVo> walletGameList = new ArrayList<>();
    private List<GameBalanceVo> transGameBalanceList = new ArrayList<>();
    private int count = 0;

    private MyWalletAdapter myWalletAdapter;

    private BasePopupView basePopupView = null;
    private ProfileVo mProfileVo;
    private BasePopupView ppw = null; // 底部弹窗
    private BasePopupView ppw2 = null; // 底部弹窗
    boolean isBinding = false; // 是否正在跳转到其它页面绑定手机/YHK (跳转后回来刷新用)

    private AwardsRecordVo awardsRecordVo ;//礼物流水

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.readCache(); // 读取缓存

        viewModel.getBalance(); // 平台中心余额

        viewModel.getAwardRecord();//获取礼物流水

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
            Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_loading);
            binding.ivwRefreshBlc.startAnimation(animation);
            ToastUtils.show(this.getString(R.string.txt_rc_tip_latest_balance), ToastUtils.ShowType.Success);
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
        //取款
        binding.tvwWithdraw.setOnClickListener(v -> {
            CfLog.d("************");
            showChoose(); // 显示提款方式

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

        binding.tvwBalance.setOnClickListener(v->{
            //账号已登出，请重新登录

        });
        //显示钱包流水
        binding.llAwardRecord.setOnClickListener(v -> {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_MY_WALLET_FLOW).navigation();
        });


        int spanCount = 4; // 每行的列数
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        binding.rcvWalletDetails.setLayoutManager(layoutManager);

        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
        mProfileVo = new Gson().fromJson(json, ProfileVo.class);
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

        //获取礼物流水
        viewModel.awardrecordVoMutableLiveData.observe(this, vo -> {
            awardsRecordVo = vo;
            if (awardsRecordVo != null && awardsRecordVo.list.size() > 0)
            {
                binding.tvwAwardRecord.setText(awardsRecordVo.locked_award_sum);
            }
            else
            {
                binding.tvwAwardRecord.setText("0.0000");
            }
        });
    }

   /* private void goWebView(View v, String path) {
        String title = ((TextView) v).getText().toString();
        String url = DomainUtil.getDomain2() + path;
        BrowserActivity.start(this, title, url, true);
    }*/

    private void goRecharge() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowBack", true);
        startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle);

    }

    /**
     * 显示提款页面
     */
    private void showChoose() {
        if (!mProfileVo.is_binding_phone || !mProfileVo.is_binding_email) {
            CfLog.i("未绑定手机/邮箱");
            toBindPhoneNumber();
        } else if (!mProfileVo.is_binding_card) {
            CfLog.i("未绑定银行卡");
            toBindPhoneOrCard();
        } else {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_CHOOSE_WITHDRAW).navigation();

        }
    }

    private void toBindPhoneOrCard() {
        String msg = getString(R.string.txt_rc_bind_personal_info);
        String left = getString(R.string.txt_rc_bind_phone_now);
        String right = getString(R.string.txt_rc_bind_bank_card_now);
        MsgDialog dialog = new MsgDialog(this, null, msg, left, right, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                toBindPhoneNumber();
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                toBindCard();
                ppw.dismiss();
            }
        });
        ppw = new XPopup.Builder(this)
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog);
        ppw.show();
    }
    private void toBindPhoneNumber() {

        String msg = getString(R.string.txt_rc_bind_phone_email_pls);
        String left = getString(R.string.txt_rc_bind_phone);
        String right = getString(R.string.txt_rc_bind_email);
        MsgDialog dialog = new MsgDialog(this, null, msg, left, right, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                toBindPhoneOrEmail(Constant.BIND_PHONE);
                ppw2.dismiss();
            }

            @Override
            public void onClickRight() {
                toBindPhoneOrEmail(Constant.BIND_EMAIL);
                ppw2.dismiss();
            }
        });
        ppw2 = new XPopup.Builder(this)
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog);
        ppw2.show();
    }

    private void toBindCard() {

        String msg = getString(R.string.txt_rc_bind_bank_card_pls);
        MsgDialog dialog = new MsgDialog(this, null, msg, true, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
            }

            @Override
            public void onClickRight() {
                isBinding = true;
                Bundle bundle = new Bundle();
                bundle.putString("type", "bindcard");
                startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
                ppw2.dismiss();
            }
        });
        ppw2 = new XPopup.Builder(this)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(dialog);
        ppw2.show();

    }

    private void toBindPhoneOrEmail(String type) {
        isBinding = true;
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
    }

}
