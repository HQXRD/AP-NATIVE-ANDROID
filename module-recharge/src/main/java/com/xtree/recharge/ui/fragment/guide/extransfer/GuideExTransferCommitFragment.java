package com.xtree.recharge.ui.fragment.guide.extransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.databinding.FragmentExtransferCommitBinding;
import com.xtree.recharge.databinding.FragmentExtransferCommitGuideBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.fragment.guide.RechargeNextComponent;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.recharge.ui.widget.Comm100ChatWindows;
import com.xtree.recharge.vo.RechargeVo;

import java.util.Map;
import java.util.Stack;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账-提交订单流程
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_COMMIT_GUI)
public class GuideExTransferCommitFragment extends BaseFragment<FragmentExtransferCommitGuideBinding, ExTransferViewModel> {

    private Comm100ChatWindows serviceChatFlow;

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> viewModel.finish());
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        binding.mainScrollview.scrollTo(0, 100);

        showGuideByNext();

        serviceChatFlow = new Comm100ChatWindows(requireActivity());
       /* serviceChatFlow.setOnClickListener(new Comm100ChatWindows.OnClickListener() {
            @Override
            public void onClick(View view, String url) {

                String chatUrl = url;
                if (viewModel != null && viewModel.payOrderData.getValue() != null) {
                    String merchantOrder = viewModel.payOrderData.getValue().getMerchantOrder();
                    if (!TextUtils.isEmpty(merchantOrder)) {
                        chatUrl += merchantOrder;
                    }
                }

                VisitorClientInterface.setChatUrl(chatUrl);

                Intent intent = new Intent(getContext(), ContainerActivity.class);
                intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_CHAT);
                requireActivity().startActivity(intent);

                viewModel.close();
            }
        });
        serviceChatFlow.show();*/
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
        if (serviceChatFlow != null) {
            serviceChatFlow.removeView();
            serviceChatFlow = null;
        }
    }

    @Override
    public boolean isBackPressed() {

        if (viewModel != null) {
            viewModel.finish();
        }

        return true;
    }

    private Guide nextGuide;
    /**
     * 显示充值 下一步 引导页面
     */
    private void showGuideByNext(){

        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(binding.tvRcExpBankStatus)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10);

        builder.addComponent(new ExTransferCommitComponent(new ExTransferCommitComponent.IExTransferCommitCallback() {
            @Override
            public void rechargeNamePrevious() {
                dismissNextGuide();
            }

            @Override
            public void rechargeNameJump() {
                dismissNextGuide();
            }

            @Override
            public void rechargeNameNext() {
                dismissNextGuide();
            }

        }));
        nextGuide = builder.createGuide();
        nextGuide.show(getActivity());
    }
    private void  dismissNextGuide(){
        if (nextGuide !=null){
            nextGuide.dismiss();;
            nextGuide = null;
        }
    }
}
