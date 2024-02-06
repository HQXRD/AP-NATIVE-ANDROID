package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.databinding.BtFragmentTutorialBinding;
import com.xtree.bet.databinding.BtTutorialItemBinding;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.Utils;

/**
 *
 */
public class BtTutorialItemFragment extends BaseFragment<BtTutorialItemBinding, FBMainViewModel> {
    public final static String KEY_SOUTCE_RES = "key_soutce_res";
    private int[] sourceRes;

    public static BtTutorialItemFragment getInstance(int[] sourceRes){
        BtTutorialItemFragment btTutorialItemFragment = new BtTutorialItemFragment();
        Bundle bundle = new Bundle();
        bundle.putIntArray(KEY_SOUTCE_RES, sourceRes);
        btTutorialItemFragment.setArguments(bundle);
        return btTutorialItemFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_tutorial_item;
    }

    @Override
    public void initParam() {
        sourceRes = getArguments().getIntArray(KEY_SOUTCE_RES);
    }

    @Override
    public void initView() {
        binding.vpTutorial.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return BtTutorialItemContentFragment.getInstance(sourceRes[position]);
            }

            @Override
            public int getCount() {
                return sourceRes.length;
            }
        });

        binding.vpTutorial.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == sourceRes.length - 1) {
                    binding.ivLeft.setVisibility(View.VISIBLE);
                    binding.ivRight.setVisibility(View.INVISIBLE);
                } else {
                    binding.ivLeft.setVisibility(View.INVISIBLE);
                    binding.ivRight.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < sourceRes.length; i++) {
                    if(i == position) {
                        binding.llStep.getChildAt(i).findViewById(R.id.tv_step).setSelected(true);
                    }else {
                        binding.llStep.getChildAt(i).findViewById(R.id.tv_step).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < sourceRes.length; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.bt_tutorial_step_item, null);
            TextView tvStep = view.findViewById(R.id.tv_step);
            ImageView ivStep = view.findViewById(R.id.iv_step);
            tvStep.setText(String.valueOf(i + 1));
            if(i == 0){
                tvStep.setSelected(true);
            }
            if(i == sourceRes.length - 1){
                ivStep.setVisibility(View.GONE);
            }
            binding.llStep.addView(view);
        }

    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public FBMainViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(FBMainViewModel.class);
    }
}
