package com.xtree.mine.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.router.RouterFragmentPath;

import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMineBinding;
import com.xtree.mine.ui.activity.LoginRegisterActivity;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterFragmentPath.Mine.PAGER_MINE)
public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModel> {
    @Override
    public void initView() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    @Override
    public void initData() {
        // 使用 TabLayout 和 ViewPager 相关联
        //binding.tabs.setupWithViewPager(binding.viewPager);
        //binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));
      //  viewModel.addPage();

       binding.textViewLogin.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
             Intent toLogin = new Intent(getContext(), LoginRegisterActivity.class);
             toLogin.putExtra(LoginRegisterActivity.ENTER_TYPE,LoginRegisterActivity.LOGIN_TYPE);
             startActivity(toLogin);
           }
       });

       binding.textViewRegister.setOnClickListener(v -> {
           Intent toRegister = new Intent(getContext(),LoginRegisterActivity.class);
           toRegister.putExtra(LoginRegisterActivity.ENTER_TYPE,LoginRegisterActivity.REGISTER_TYPE);
           startActivity(toRegister);
       });

       binding.iconSetting.setOnClickListener(view -> {
           popup();
       });
    }

    private void popup(){
        showBottomDialog();
    }

    private void showBottomDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(getActivity(),R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(getActivity(),R.layout.mine_account_popup_window,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.me_close_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));
    }
}
