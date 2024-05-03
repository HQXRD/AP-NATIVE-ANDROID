package com.xtree.bet.ui.fragment;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FB;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FBXC;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;

import android.app.Application;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BtRecordTime;
import com.xtree.bet.bean.ui.BtResult;
import com.xtree.bet.databinding.BtDialogBtRecordBinding;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.adapter.BtRecordAdapter;
import com.xtree.bet.ui.viewmodel.TemplateBtRecordModel;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBBtRecordModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.pm.PMBtRecordModel;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 投注记录页面
 */
public class BtRecordDialogFragment extends BaseDialogFragment<BtDialogBtRecordBinding, TemplateBtRecordModel> {

    public final static String KEY_ISSETTLED = "KEY_ISSETTLED";

    public static String[] TAB_TITLE = new String[]{"未结算", "已结算"};

    private BtRecordAdapter btRecordAdapter;

    private boolean isSettled;
    private List<BtRecordTime> btRecordTimes;

    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
    private String mPlatformName;
    private View mHeader;
    private TextView tvHeaderName;
    private Disposable timerDisposable;

    private int mGroupPosition, mChildPosition;
    private BtAdvanceSettlementFragment btAdvanceSettlementFragment;
    private BtResult mBtCashOut;

    public static BtRecordDialogFragment getInstance(boolean isSettled) {
        BtRecordDialogFragment btRecordDialogFragment = new BtRecordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_ISSETTLED, isSettled);
        btRecordDialogFragment.setArguments(bundle);
        return btRecordDialogFragment;
    }

    @Override
    public void initView() {
        isSettled = getArguments().getBoolean(KEY_ISSETTLED);
        for (int i = 0; i < TAB_TITLE.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(TAB_TITLE[i]);
            ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_record_top_tab_item_text);
            textView.setTextColor(colorStateList);
            textView.setTextSize(16);
            binding.tabTitle.addTab(binding.tabTitle.newTab().setCustomView(textView));
        }
        if (isSettled) {
            binding.tabTitle.getTabAt(1).select();
        } else {
            binding.tabTitle.getTabAt(0).select();
        }
        initLeagueListView();
        binding.rvRecord.setOnGroupClickListener((parent, v, groupPosition, id) -> true);
        binding.tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.betRecord(tab.getPosition() != 0);
                isSettled = tab.getPosition() != 0;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.tvClose.setOnClickListener(this);
        initToRecordText();
    }

    private void initToRecordText() {
        binding.llEmpty.tvToRecord.setOnClickListener(this);
        SpannableString spannableString = new SpannableString(getActivity().getResources().getString(R.string.bt_txt_search_record));
        int startIndex = 12; // "这"的索引值
        int endIndex = spannableString.length();   // "是"的索引值 + 1
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.bt_color_car_dialog_hight_line2));
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.llEmpty.tvToRecord.setText(spannableString);
    }

    private void initLeagueListView() {
        mHeader = LayoutInflater.from(getContext()).inflate(R.layout.bt_layout_bt_record_time, binding.rvRecord, false);
        tvHeaderName = mHeader.findViewById(R.id.tv_name);
        binding.rvRecord.addHeader(mHeader);
        binding.rvRecord.setOnScrollListener(new AnimatedExpandableListViewMax.OnScrollListenerImpl(mHeader, binding.rvRecord) {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(absListView, firstVisibleItem, visibleItemCount, totalItemCount);
                long position = binding.rvRecord.getExpandableListPosition(firstVisibleItem);
                int groupPosition = binding.rvRecord.getPackedPositionGroup(position);
                if (tvHeaderName != null && groupPosition != -1) {
                    tvHeaderName.setText(TimeUtils.longFormatString(btRecordTimes.get(groupPosition).getTime(), TimeUtils.FORMAT_YY_MM_DD));
                }
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
            if (btRecordTimes == null || btRecordTimes.isEmpty()) {
                binding.nsvOption.setVisibility(View.GONE);
                binding.cslBottom.setVisibility(View.GONE);
                binding.llEmpty.llEmpty.setVisibility(View.VISIBLE);
                binding.llEmpty.tvBtNow.setOnClickListener(this);
                if (timerDisposable != null) {
                    viewModel.removeSubscribe(timerDisposable);
                }
            } else {
                binding.nsvOption.setVisibility(View.VISIBLE);
                binding.cslBottom.setVisibility(View.VISIBLE);
                binding.llEmpty.llEmpty.setVisibility(View.GONE);
                this.btRecordTimes = btRecordTimes;
                if (btRecordAdapter == null) {
                    btRecordAdapter = new BtRecordAdapter(getContext(), btRecordTimes);
                    btRecordAdapter.setAdvanceSettlementCallBack((groupPosition, childPosition, btResult, acceptoddschange, parlay) -> {
                        mGroupPosition = groupPosition;
                        mChildPosition = childPosition;
                        btAdvanceSettlementFragment = BtAdvanceSettlementFragment.getInstance(btResult.getBtAmount(), btResult.getAdvanceSettleAmount()
                                , btResult.getId(), acceptoddschange, parlay, btResult.getUnitCashOutPayoutStake());
                        btAdvanceSettlementFragment.show(getActivity().getSupportFragmentManager(), "btAdvanceSettlementFragment");
                        btAdvanceSettlementFragment.setOnDismissListener(dialog -> {
                            viewModel.betRecord(isSettled);
                        });
                    });
                    binding.rvRecord.setAdapter(btRecordAdapter);
                    binding.rvRecord.scroll(0);
                } else {
                    btRecordAdapter.setData(btRecordTimes);
                }
                initTimer();
                for (int i = 0; i < binding.rvRecord.getExpandableListAdapter().getGroupCount(); i++) {
                    binding.rvRecord.expandGroup(i);
                }
            }
        });

        viewModel.tokenInvalidEvent.observe(this, unused -> {
            viewModel.betRecord(isSettled);
        });

        viewModel.btUpdateCashOutPrice.observe(this, unused -> {
            if (btRecordTimes != null && !btRecordTimes.isEmpty()) {
                if(mGroupPosition > btRecordTimes.size() - 1
                        || btRecordTimes.get(mGroupPosition).getBtResultList() == null
                        || btRecordTimes.get(mGroupPosition).getBtResultList().isEmpty()
                        || mChildPosition > btRecordTimes.get(mGroupPosition).getBtResultList().size() - 1){
                    return;
                }
                mBtCashOut = btRecordTimes.get(mGroupPosition).getBtResultList().get(mChildPosition);
                if (btAdvanceSettlementFragment != null && btAdvanceSettlementFragment.isAdded()) {
                    btAdvanceSettlementFragment.updatePrice(mBtCashOut);
                }
            }
        });
    }

    private void initTimer() {
        if (timerDisposable == null) {
            timerDisposable = Observable.interval(0, 5, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if(!isSettled) {
                            viewModel.cashOutPrice();
                            CfLog.e("===========viewModel.cashOutPrice();==================");
                        }
                    });
            viewModel.addSubscribe(timerDisposable);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_close || id == R.id.tv_bt_now) {
            dismiss();
        } else if (id == R.id.tv_to_record) {
            Bundle bundle = new Bundle();
            bundle.putString("typeId", mPlatform);
            if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                mPlatformName = getString(R.string.bt_platform_name_fbxc);
            } else if (TextUtils.equals(mPlatform, PLATFORM_FB)) {
                mPlatformName = getString(R.string.bt_platform_name_fb);
            } else {
                mPlatformName = getString(R.string.bt_platform_name_pm);
            }
            bundle.putString("typeName", mPlatformName);
            bundle.putInt("status", isSettled ? 1 : 2);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BT_REPORT, bundle);
        }
    }

    @Override
    public TemplateBtRecordModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(FBBtRecordModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(PMBtRecordModel.class);
        }
    }
}
