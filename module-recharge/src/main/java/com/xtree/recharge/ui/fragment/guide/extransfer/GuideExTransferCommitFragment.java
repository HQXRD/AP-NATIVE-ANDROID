package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.databinding.FragmentExtransferCommitGuideBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.fragment.guide.RechargeNameComponent;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.vo.RechargeVo;

import java.util.Map;
import java.util.Stack;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;

/**
 * 银行卡充值 引导页面
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_COMMIT_GUI)
public class GuideExTransferCommitFragment extends BaseFragment<FragmentExtransferCommitGuideBinding, ExTransferViewModel> {
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
                   showGuideByNext();
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
       // binding.mainScrollview.scrollTo(0, 800);
        binding.slMain.scrollTo(0 , 2000);
        showMaskGuide();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_extransfer_commit_guide;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public ExTransferViewModel initViewModel() {
        //以充值页作为共享载体
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



     /*   binding.getModel().setActivity(getActivity());
        binding.getModel().setFlowWindow(serviceChatFlow);
        binding.getModel().canonicalName = getClass().getCanonicalName();*/

        ExCreateOrderRequest createOrderInfo = RxBus.getDefault().getStickyEvent(ExCreateOrderRequest.class);
        if (createOrderInfo != null) {
            RxBus.getDefault().removeAllStickyEvents();
          /*  binding.getModel().initData(getActivity(),createOrderInfo);
            binding.getModel().serviceChatTimeKeeping();*/
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
                getActivity().finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        /*binding.getModel().setActivity(getActivity());*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean isBackPressed() {
        return true;
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


    private Guide nextGuide;
    /**
     * 显示银行卡 充值 下一步 引导页面
     */
    private void showGuideByNext(){

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.llStep1.tvwOk)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10);

        builder.addComponent(new ExTransferCommitComponent(new ExTransferCommitComponent.IExTransferCommitCallback() {
            @Override
            public void rechargeNamePrevious() {
                dismissNextGuide();
                getActivity().finish();
            }

            @Override
            public void rechargeNameJump() {
                dismissNextGuide();

            }

            @Override
            public void rechargeNameNext() {
                dismissNextGuide();
                //跳转充值银行卡上传凭证信息页面
                startContainerFragment(RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_PAYEE_GUI);
                getActivity().finish();
            }

        }));
        nextGuide = builder.createGuide();
        nextGuide.show(requireActivity());
    }
    private void  dismissNextGuide(){
        if (nextGuide !=null){
            nextGuide.dismiss();;
            nextGuide = null;
        }
    }
}
