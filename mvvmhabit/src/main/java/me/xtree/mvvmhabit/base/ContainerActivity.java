package me.xtree.mvvmhabit.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;

import me.xtree.mvvmhabit.R;

/**
 * 盛装Fragment的一个容器(代理)Activity
 * 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 */
public class ContainerActivity extends RxAppCompatActivity {
    private static final String FRAGMENT_TAG = "content_fragment_tag";
    public static final String FRAGMENT = "fragment";
    public static final String BUNDLE = "bundle";
    public static final String ROUTER_PATH = "routerPath";
    protected WeakReference<Fragment> mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;
        if (savedInstanceState != null) {
            fragment = fm.getFragment(savedInstanceState, FRAGMENT_TAG);
        }

        String routerPath = getIntent().getStringExtra(ROUTER_PATH);
        if (!TextUtils.isEmpty(routerPath)) {
            fragment = (Fragment) ARouter.getInstance().build(routerPath).navigation();
            Bundle args = getIntent().getBundleExtra(BUNDLE);
            if (args != null) {
                fragment.setArguments(args);
            }
        }

        if (fragment == null) {
            fragment = initFromIntent(getIntent());
        }
        if (fragment == null) {
            Log.e("TAG", "fragment is null, routerPath is " + routerPath);
            finish();
            return;
        }
        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        trans.replace(R.id.content, fragment);
        trans.commitAllowingStateLoss();
        mFragment = new WeakReference<>(fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mFragment.get());
    }

    protected Fragment initFromIntent(Intent data) {
        if (data == null) {
            //throw new RuntimeException("you must provide a page info to display");
            Log.e("TAG", "you must provide a page info to display");
            return null;
        }
        try {
            String fragmentName = data.getStringExtra(FRAGMENT);
            if (fragmentName == null || "".equals(fragmentName)) {
                //throw new IllegalArgumentException("can not find page fragmentName");
                Log.e("TAG", "can not find page fragmentName");
                return null;
            }
            Class<?> fragmentClass = Class.forName(fragmentName);
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            Bundle args = data.getBundleExtra(BUNDLE);
            if (args != null) {
                fragment.setArguments(args);
            }
            return fragment;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("fragment initialization failed!");
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof BaseFragment) {
            if (!((BaseFragment<?, ?>) fragment).isBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
