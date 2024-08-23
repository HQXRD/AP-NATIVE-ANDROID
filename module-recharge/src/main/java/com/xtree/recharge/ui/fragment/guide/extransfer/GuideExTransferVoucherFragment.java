package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.binioter.guideview.Guide;
import com.binioter.guideview.GuideBuilder;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.databinding.FragmentExtransferVoucherGuideBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Map;
import java.util.Stack;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账-上传凭证流程 - 回执单
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_VOUCHER_GUI)
public class GuideExTransferVoucherFragment extends BaseFragment<FragmentExtransferVoucherGuideBinding, ExTransferViewModel> {
    private static  final int MSG_DISS_MASK = 0x110119;
    private static  final int MSG_SHOW_GUIDE = 0x110120;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case MSG_DISS_MASK:
                    dismissMskGuide();
                    break;
                case MSG_SHOW_GUIDE:
                    showReceiptGuide();
                    break;
                default:
                    CfLog.i("****** default");
                    break;
            }
        }
    };
    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        //显示遮罩
        showMaskGuide();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_extransfer_voucher_guide;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public ExTransferViewModel initViewModel() {
        Stack<Activity> activityStack = AppManager.getActivityStack();
        FragmentActivity fragmentActivity = requireActivity();
        for (Activity activity : activityStack) {
            try {
                FragmentActivity fa = (FragmentActivity) activity;
                for (Fragment fragment : fa.getSupportFragmentManager().getFragments()) {
                    if (fragment.getClass().getCanonicalName().equals(RechargeFragment.class.getCanonicalName())) {
                        fragmentActivity = fa;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ExTransferViewModel viewmodel = new ViewModelProvider(fragmentActivity).get(ExTransferViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(requireActivity().getApplication());
        viewmodel.setModel(instance.getmRepository());
        viewmodel.setRechargeViewModel(new ViewModelProvider(fragmentActivity).get(RechargeViewModel.class));
        return viewmodel;
    }

    @Override
    public void initData() {
        super.initData();
        binding.getModel().setActivity(getActivity());
        binding.getModel().canonicalName = getClass().getCanonicalName();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.getUC().getStartContainerActivityEvent().removeObservers(this);
        viewModel.getUC().getStartContainerActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startContainerFragment(canonicalName, bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getModel().setActivity(getActivity());
        //跳转充值页面确认付款
       /* startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CONFIRM_GUI);*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 回执单 示例 展示
     */
    private Guide singleReceiptGuide;

    private void showReceiptGuide(){

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.llStep1.clMain)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(false);

        builder.addComponent(new ExVoucherSingleReceiptComponent(new ExVoucherSingleReceiptComponent.IRechargeNameCallback() {
            @Override
            public void rechargeNamePrevious() {
                dismissReceiptGuide();
                getActivity().finish();
            }

            @Override
            public void rechargeNameJump() {
                dismissReceiptGuide();
            }

            @Override
            public void rechargeNameNext() {
                dismissReceiptGuide();
                //showPayeeUploadGuide();
                showLoadUpGuide();
            }

        }));
        singleReceiptGuide = builder.createGuide();
        singleReceiptGuide.show(getActivity());
        singleReceiptGuide.setShouldCheckLocInWindow(false);
    }
    private void  dismissReceiptGuide(){
        if (singleReceiptGuide !=null){
            singleReceiptGuide.dismiss();;
            singleReceiptGuide = null;
        }
    }
    /**
     * 回执单 确定 展示
     */
    private Guide singleLoadUpGuide;
    private void showLoadUpGuide(){
         /* GuideBuilder builder = new GuideBuilder();
        //ll_step_1
      builder.setTargetView( binding.llStep1.ivw01.findViewById(R.id.tv_tip))
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10);
        builder.addComponent(new RechargeBankComponent(getContext(),new RechargeBankComponent.IRechargeBankCallback() {
            @Override
            public void rechargeBankJump() {
                dismissNextGuide();
            }

            @Override
            public void rechargeBankNext() {
                dismissNextGuide();
                //下一步 上传凭证 -回执单
                startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_VOUCHER_GUI);
            }
        }));
        singleReceiptGuide = builder.createGuide();
        singleReceiptGuide.show(getActivity());
        singleReceiptGuide.setShouldCheckLocInWindow(false);*/

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.llStep1.tvwRight)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(false);

        builder.addComponent(new ExVoucherSingleLoadUpComponent(new ExVoucherSingleLoadUpComponent.IRechargeNameCallback() {
            @Override
            public void rechargeNamePrevious() {
                dismissLoadUpGuide();
                getActivity().finish();
            }

            @Override
            public void rechargeNameJump() {
                dismissLoadUpGuide();
                showLoadUpGuide1();
            }

            @Override
            public void rechargeNameNext() {
                dismissLoadUpGuide();
                //showPayeeUploadGuide();
                //跳转充值页面确认付款
                 startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CONFIRM_GUI);
                getActivity().finish();
            }

        }));
        singleLoadUpGuide = builder.createGuide();
        singleLoadUpGuide.show(getActivity());
        singleLoadUpGuide.setShouldCheckLocInWindow(false);
    }
    private void  dismissLoadUpGuide(){
        if (singleLoadUpGuide !=null){
            singleLoadUpGuide.dismiss();;
            singleLoadUpGuide = null;
        }
    }

    /**
     * 回执单 确定 展示
     */
    private Guide dismissLoadUpGuide1;
    private void showLoadUpGuide1(){

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.llStep1.tvwLeft)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(false);

        builder.addComponent(new ExVoucherSingleLoadUpComponent(new ExVoucherSingleLoadUpComponent.IRechargeNameCallback() {
            @Override
            public void rechargeNamePrevious() {
                dismissLoadUpGuide();
                getActivity().finish();
            }

            @Override
            public void rechargeNameJump() {
                dismissLoadUpGuide();
            }

            @Override
            public void rechargeNameNext() {
                dismissLoadUpGuide();
                //showPayeeUploadGuide();
                //跳转充值页面确认付款
                startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CONFIRM_GUI);
            }

        }));
        dismissLoadUpGuide1 = builder.createGuide();
        dismissLoadUpGuide1.show(getActivity());
        dismissLoadUpGuide1.setShouldCheckLocInWindow(false);
    }
    private void  dismissLoadUpGuide1(){
        if (singleLoadUpGuide !=null){
            singleLoadUpGuide.dismiss();;
            singleLoadUpGuide = null;
        }
    }

    /**
     * 遮罩
     */
    private Guide maskGuide;

    private void  showMaskGuide(){

        binding.slMain.scrollTo(0 , 1000);

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView( binding.llStep1.clMain)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setAutoDismiss(true);

        builder.addComponent(new ExTransferMaskComponet());
        maskGuide = builder.createGuide();
        maskGuide.show(getActivity());
        maskGuide.setShouldCheckLocInWindow(true);

        Message msg2 = new Message();
        msg2.what = MSG_DISS_MASK;
        mHandler.sendMessageDelayed(msg2, 350L);
    }
    private void  dismissMskGuide(){
        if (maskGuide !=null){
            maskGuide.dismiss();;
            maskGuide = null;
        }
        Message msg2 = new Message();
        msg2.what = MSG_SHOW_GUIDE;
        mHandler.sendMessageDelayed(msg2, 10L);
    }
}
