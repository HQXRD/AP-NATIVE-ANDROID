package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.MsgDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMyWalletBinding;
import com.xtree.mine.ui.viewmodel.MyWalletViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AwardsRecordVo;
import com.xtree.mine.vo.GameBalanceVo;
import com.xtree.mine.vo.GameMenusVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterFragmentPath.Mine.PAGER_MY_WALLET)
public class MyWalletFragment extends BaseFragment<FragmentMyWalletBinding, MyWalletViewModel> {
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

    private AwardsRecordVo awardsRecordVo;//礼物流水

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
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.ivwRefreshBlc.setOnClickListener(v -> {
            viewModel.getBalance();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_loading_normal);
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
            new XPopup.Builder(getContext()).asCustom(new AccountMgmtDialog(getContext())).show();
        });

        binding.tvwRecord.setOnClickListener(v -> {
            CfLog.d("************");
            //goWebView(v, Constant.URL_DW_RECORD);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_RECHARGE_WITHDRAW); // 充提记录
        });

        binding.tvwBalance.setOnClickListener(v -> {
            //账号已登出，请重新登录

        });

        //显示钱包流水
        //binding.tvwAwardRecord.setOnClickListener(v -> {
        //    ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_MY_WALLET_FLOW).navigation();
        //});

        int spanCount = 3; // 每行的列数
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        binding.rcvWalletDetails.setLayoutManager(layoutManager);

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_my_wallet;
    }

    @Override
    public MyWalletViewModel initViewModel() {
        // return super.initViewModel();
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MyWalletViewModel.class);
    }

    @Override
    public void initData() {
        //super.initData();

        viewModel.readCache(); // 读取缓存

        viewModel.getBalance(); // 平台中心余额

        viewModel.getAwardRecord();//获取礼物流水

        viewModel.getTransThirdGameType(getContext());
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataProfile.observe(this, vo -> {
            mProfileVo = vo;
        });

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
                myWalletAdapter = new MyWalletAdapter(getContext());
                binding.rcvWalletDetails.setAdapter(myWalletAdapter);
                myWalletAdapter.setData(transGameBalanceList);
            }
        });

        viewModel.listSingleLiveData.observe(this, vo -> {
            myWalletAdapter = new MyWalletAdapter(getContext());
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
        //viewModel.awardrecordVoMutableLiveData.observe(this, vo -> {
        //    awardsRecordVo = vo;
        //    if (awardsRecordVo != null && awardsRecordVo.list.size() > 0) {
        //        binding.tvwAwardRecord.setText(awardsRecordVo.locked_award_sum);
        //    } else {
        //        binding.tvwAwardRecord.setText("0.0000");
        //    }
        //});
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.readCache();
    }

    private void goRecharge() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowBack", true);
        startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle);
    }

    /**
     * 显示提款页面
     */
    private void showChoose() {
        if (!mProfileVo.is_binding_phone && !mProfileVo.is_binding_email) {
            CfLog.i("未绑定手机/邮箱");
            toBindPhoneNumber();
        } else {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_CHOOSE_WITHDRAW).navigation();
        }
    }

    private void toBindPhoneNumber() {

        String msg = getString(R.string.txt_rc_bind_phone_email_pls);
        String left = getString(R.string.txt_rc_bind_phone);
        String right = getString(R.string.txt_rc_bind_email);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
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
        ppw2 = new XPopup.Builder(getContext()).asCustom(dialog);
        ppw2.show();
    }

    private void toBindCard() {
        String msg = getString(R.string.txt_rc_bind_bank_card_pls);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, true, new MsgDialog.ICallBack() {
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
        ppw2 = new XPopup.Builder(getContext()).asCustom(dialog);
        ppw2.show();

    }

    private void toBindPhoneOrEmail(String type) {
        isBinding = true;
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
    }
}
