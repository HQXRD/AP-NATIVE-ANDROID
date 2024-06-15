package com.xtree.recharge.ui.fragment.extransfer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.recharge.ui.widget.Comm100ChatWindows;
import com.xtree.recharge.BR;
import com.xtree.recharge.R;
import com.xtree.recharge.data.source.request.ExCreateOrderRequest;
import com.xtree.recharge.databinding.FragmentExtransferCommitBinding;
import com.xtree.recharge.ui.fragment.RechargeFragment;
import com.xtree.recharge.ui.viewmodel.ExTransferViewModel;
import com.xtree.recharge.ui.viewmodel.RechargeViewModel;
import com.xtree.recharge.ui.viewmodel.factory.AppViewModelFactory;

import java.util.Map;
import java.util.Stack;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/5/28.
 * Describe: 极速转账-提交订单流程
 */
@Route(path = RouterFragmentPath.Transfer.PAGER_TRANSFER_EX_COMMIT)
public class ExTransferCommitFragment extends BaseFragment<FragmentExtransferCommitBinding, ExTransferViewModel> {

    private Comm100ChatWindows serviceChatFlow;

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());
        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        serviceChatFlow = new Comm100ChatWindows(requireActivity());
        serviceChatFlow.show();
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_extransfer_commit;
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

        binding.getModel().setActivity(getActivity());
        binding.getModel().canonicalName = getClass().getCanonicalName();

        ExCreateOrderRequest createOrderInfo = RxBus.getDefault().getStickyEvent(ExCreateOrderRequest.class);
        if (createOrderInfo != null) {
            RxBus.getDefault().removeAllStickyEvents();
            binding.getModel().initData(getActivity(),createOrderInfo);
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
    public void onDestroy() {
        super.onDestroy();
        if (serviceChatFlow != null) {
            serviceChatFlow.removeView();
        }
    }

    @Override
    public boolean isBackPressed() {

        if (viewModel != null) {
            viewModel.finish();
        }

        return true;
    }
}
