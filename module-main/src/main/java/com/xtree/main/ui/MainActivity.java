package com.xtree.main.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.widget.MenuItemView;
import com.xtree.main.BR;
import com.xtree.main.R;
import com.xtree.main.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseViewModel;

/**
 * Created by goldze on 2018/6/21
 */
@Route(path = RouterActivityPath.Main.PAGER_MAIN)
public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> {
    private List<Fragment> mFragments;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //初始化Fragment
        initFragment();
        //初始化底部Button
        initBottomTab();
    }

    private void initFragment() {
        //ARouter拿到多Fragment(这里需要通过ARouter获取，不能直接new,因为在组件独立运行时，宿主app是没有依赖其他组件，所以new不到其他组件的Fragment)
        Fragment homeFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation();
        Fragment activityFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Activity.PAGER_ACTIVITY).navigation();
        Fragment rechargeFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Recharge.PAGER_RECHARGE).navigation();
        Fragment mineFragment = (Fragment) ARouter.getInstance().build(RouterFragmentPath.Mine.PAGER_MINE).navigation();
        mFragments = new ArrayList<>();
        mFragments.add(homeFragment);
        mFragments.add(activityFragment);
        mFragments.add(rechargeFragment);
        mFragments.add(mineFragment);
        if (homeFragment != null) {
            //默认选中第一个
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameLayout, homeFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    //创建一个Item
    private BaseTabItem newItem(int drawable, int drawableSelect, String text) {
        MenuItemView normalItemView = new MenuItemView(this);
        normalItemView.initialize(drawable, drawable, text);
        normalItemView.setDefaultDrawable(getResources().getDrawable(drawable));
        normalItemView.setSelectedDrawable(getResources().getDrawable(drawableSelect));
        normalItemView.setTextDefaultColor(getResources().getColor(R.color.textColorVice));
        normalItemView.setTextCheckedColor(getResources().getColor(R.color.colorPrimary));
        normalItemView.setIconTopMargin(16);
        normalItemView.setTextTopMarginOnIcon(4);
        return normalItemView;
    }

    private void initBottomTab() {
        NavigationController navigationController = binding.pagerBottomTab.custom()
                .addItem(newItem(R.mipmap.yingyong_unselected, R.mipmap.yingyong_selected, "首页"))
                .addItem(newItem(R.mipmap.huanzhe_unselected, R.mipmap.huanzhe_selected, "活动"))
                .addItem(newItem(R.mipmap.xiaoxi_unselected, R.mipmap.xiaoxi_selected, "充值"))
                .addItem(newItem(R.mipmap.wode_unselected, R.mipmap.wode_selected, "我的"))
                .build();
        //底部按钮的点击事件监听
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Fragment currentFragment = mFragments.get(index);
                if (currentFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout, currentFragment);
                    transaction.commitAllowingStateLoss();
                }
            }

            @Override
            public void onRepeat(int index) {
            }
        });
    }
}
