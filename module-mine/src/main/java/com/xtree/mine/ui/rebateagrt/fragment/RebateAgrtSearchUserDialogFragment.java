package com.xtree.mine.ui.rebateagrt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.DialogRebateagrtSearchuserBinding;
import com.xtree.mine.ui.rebateagrt.model.RebateAgrtDetailModel;
import com.xtree.mine.ui.rebateagrt.viewmodel.RebateAgrtSearchUserViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/3/18.
 * Describe:
 */
@Route(path = RouterFragmentPath.Mine.PAGER_REBATEAGRT_SEARCHUSER_DIALOG)
public class RebateAgrtSearchUserDialogFragment extends BaseFragment<DialogRebateagrtSearchuserBinding, RebateAgrtSearchUserViewModel> {
    @Override
    public void initView() {
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.dialog_rebateagrt_searchuser;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public RebateAgrtSearchUserViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(requireActivity(), factory).get(RebateAgrtSearchUserViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        RebateAgrtDetailModel stickyEvent = RxBus.getDefault().getStickyEvent(RebateAgrtDetailModel.class);
        if (stickyEvent != null) {
            viewModel.setActivity(getActivity());
            viewModel.initData(stickyEvent);
        }
    }

    //    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Window window = Objects.requireNonNull(getDialog()).getWindow();
//        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(params);
//        View decorView = window.getDecorView();
//        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
//    }
}
