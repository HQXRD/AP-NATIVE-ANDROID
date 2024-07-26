package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.comm100.livechat.VisitorClientInterface;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.databinding.FragmentExtransferConfirmBinding;
import com.xtree.recharge.databinding.FragmentExtransferConfirmGuideBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.ui.widget.Comm100ChatWindows;

import java.util.Map;
import java.util.Stack;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账-确认付款流程
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CONFIRM_GUI)
public class GuideExTransferConfirmFragment extends BaseFragment<FragmentExtransferConfirmGuideBinding, ExTransferViewModel> {

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
        binding.ivwBack.setOnClickListener(v -> viewModel.finish());
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        showMaskGuide();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_extransfer_confirm_guide;
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

        ExCreateOrderRequest createOrderInfo = RxBus.getDefault().getStickyEvent(ExCreateOrderRequest.class);
        if (createOrderInfo != null) {
            RxBus.getDefault().removeAllStickyEvents();
            binding.getModel().initData(getActivity(),createOrderInfo);
            binding.getModel().serviceChatTimeKeeping();
        }
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean isBackPressed() {

        if (viewModel != null) {
            viewModel.finish();
        }

        return true;
    }

    /**
     * 回执单 示例 展示
     */
    private Guide singleKnowGuide;

    private void showReceiptGuide(){

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.llStep1.llInfo)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10);

        builder.addComponent(new ExConfirmNextComponent(new ExConfirmNextComponent.IRechargeNextCallback() {
            @Override
            public void rechargeNextPrevious() {
                dismissReceiptGuide();
                getActivity().finish();
            }

            @Override
            public void rechargeNextJump() {
                dismissReceiptGuide();
                getActivity().finish();
            }

            @Override
            public void rechargeNextNext() {
                dismissReceiptGuide();
                //showPayeeUploadGuide();
              getActivity().finish();
            }

        }));
        singleKnowGuide = builder.createGuide();
        singleKnowGuide.show(getActivity());
        singleKnowGuide.setShouldCheckLocInWindow(false);
    }
    private void  dismissReceiptGuide(){
        if (singleKnowGuide !=null){
            singleKnowGuide.dismiss();;
            singleKnowGuide = null;
        }
    }

    /**
     * 遮罩
     */
    private Guide maskGuide;

    private void  showMaskGuide(){
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView( binding.llStep1.tvwOk)
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
