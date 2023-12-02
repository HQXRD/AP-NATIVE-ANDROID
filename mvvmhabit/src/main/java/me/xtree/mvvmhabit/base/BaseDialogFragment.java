package me.xtree.mvvmhabit.base;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trello.rxlifecycle4.components.support.RxDialogFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;


public abstract class BaseDialogFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends RxDialogFragment implements View.OnClickListener {
    protected V binding;
    protected VM viewModel;

    public abstract void initView();

    public VM initViewModel() {
        return null;
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public void initData() {

    }

    //这里用反射来获取布局文件
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewDataBinding();
        initView();
        //页面数据初始化方法
        initData();
    }

    private void initViewDataBinding() {
        viewModel = initViewModel();
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding.setLifecycleOwner(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getDialog()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        //设置显示在底部
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        View decorView = window.getDecorView();
        decorView.setPadding(100, 100, 100, 0);
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        //设置点击空白处关闭，也能启动从上到下的动画
        decorView.setOnTouchListener((view, motionEvent) -> {
            dismiss();
            return false;
        });
    }
    /*private void slideDown() {
        AnimationUtil.slideToDown(mRootView, new AnimationUtil.AnimationEndListener() {
            @Override
            public void onFinish() {
                dismiss();
            }
        });
    }*/

}
