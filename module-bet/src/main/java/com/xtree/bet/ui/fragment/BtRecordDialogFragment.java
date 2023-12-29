package com.xtree.bet.ui.fragment;

import android.app.Application;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.bean.ui.CgOddLimit;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.databinding.BtDialogBtRecordBinding;
import com.xtree.bet.databinding.BtLayoutBtCarBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.adapter.BetConfirmOptionAdapter;
import com.xtree.bet.ui.adapter.BtRecordAdapter;
import com.xtree.bet.ui.adapter.CgOddLimitAdapter;
import com.xtree.bet.ui.viewmodel.BtCarViewModel;
import com.xtree.bet.ui.viewmodel.BtRecordModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.weight.KeyboardView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 投注记录页面
 */
public class BtRecordDialogFragment extends BaseDialogFragment<BtDialogBtRecordBinding, BtRecordModel> {

    public final static String KEY_ISSETTLED = "KEY_ISSETTLED";

    public static String[] TAB_TITLE = new String[]{"未结算", "已结算"};

    private BtRecordAdapter btRecordAdapter;

    private boolean isSettled;
    private List<BtRecordTime> btRecordTimes;

    public static BtRecordDialogFragment getInstance(boolean isSettled){
        BtRecordDialogFragment btRecordDialogFragment = new BtRecordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_ISSETTLED, isSettled);
        btRecordDialogFragment.setArguments(bundle);
        return btRecordDialogFragment;
    }

    @Override
    public void initView() {
        for (int i = 0; i < TAB_TITLE.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(TAB_TITLE[i]);
            ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_record_top_tab_item_text);
            textView.setTextColor(colorStateList);
            textView.setTextSize(16);
            binding.tabTitle.addTab(binding.tabTitle.newTab().setCustomView(textView));
        }
        binding.rvRecord.setOnGroupClickListener((parent, v, groupPosition, id) -> true);
        binding.rvRecord.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*int topRowVerticalPosition =
                        (absListView == null || absListView.getChildCount() == 0) ? 0 : absListView.getChildAt(firstVisibleItem).getTop();*/
                long position = binding.rvRecord.getExpandableListPosition(firstVisibleItem);
                int type = binding.rvRecord.getPackedPositionType(position);
                if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPosition = binding.rvRecord.getPackedPositionGroup(position);
                    binding.tvTimeTop.setText(TimeUtils.longFormatString(btRecordTimes.get(groupPosition).getTime(), TimeUtils.FORMAT_MM_DD_1));
                }
            }
        });
        binding.tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.betRecord(tab.getPosition() != 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setIndicatorWidth(@NonNull final TabLayout tabLayout, final int margin) {
        tabLayout.post(() -> {
            try {
                Field slidingTabIndicatorField = tabLayout.getClass().getDeclaredField("slidingTabIndicator");
                slidingTabIndicatorField.setAccessible(true);
                LinearLayout mTabStrip = (LinearLayout) slidingTabIndicatorField.get(tabLayout);
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);
                    Field textViewField = tabView.getClass().getDeclaredField("textView");
                    textViewField.setAccessible(true);
                    TextView mTextView = (TextView) textViewField.get(tabView);
                    tabView.setPadding(0, 0, 0, 0);
                    int width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width;
                    params.leftMargin = margin;
                    params.rightMargin = margin;
                    tabView.setLayoutParams(params);
                    tabView.invalidate();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void initData() {
        isSettled = getArguments().getBoolean(KEY_ISSETTLED);
        viewModel.betRecord(isSettled);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_dialog_bt_record;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        //设置显示在底部
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) (ConvertUtils.getScreenHeight(getContext()) * 0.8);
        window.setAttributes(params);
    }

    @Override
    public void initViewObservable() {
        viewModel.btRecordTimeDate.observe(this, btRecordTimes -> {
            this.btRecordTimes = btRecordTimes;
            if(btRecordAdapter == null) {
                btRecordAdapter = new BtRecordAdapter(getContext(), btRecordTimes);
                binding.rvRecord.setAdapter(btRecordAdapter);
            }else {
                btRecordAdapter.setData(btRecordTimes);
            }
            for (int i = 0; i < binding.rvRecord.getExpandableListAdapter().getGroupCount(); i++) {
                binding.rvRecord.expandGroup(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public BtRecordModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
        return new ViewModelProvider(this, factory).get(BtRecordModel.class);
    }
}
