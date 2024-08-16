package com.xtree.bet.ui.fragment;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FB;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.xtree.base.global.Constant;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.bet.R;
import com.xtree.bet.constant.SPKey;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtDialogSettingBinding;
import com.xtree.bet.ui.activity.MainActivity;
import com.xtree.bet.ui.viewmodel.TemplateBtCarViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBBtCarViewModel;
import com.xtree.bet.ui.viewmodel.pm.PMBtCarViewModel;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseDialogFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 设置菜单
 */
public class BtSettingDialogFragment extends BaseDialogFragment<BtDialogSettingBinding, TemplateBtCarViewModel> {
    public final static String KEY_LEAGUEIDS = "KEY_LEAGUEIDS";

    // 已选联赛
    private List<Long> mLeagueIdList = new ArrayList<>();
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

    public static BtSettingDialogFragment getInstance(List<Long> leagueIdList) {
        BtSettingDialogFragment btResultDialogFragment = new BtSettingDialogFragment();
        Bundle bundle = new Bundle();
        if (!leagueIdList.isEmpty()) {
            long[] leagueIdArray = new long[leagueIdList.size()];
            for (int i = 0; i < leagueIdList.size(); i++) {
                leagueIdArray[i] = leagueIdList.get(i);
            }
            bundle.putLongArray(KEY_LEAGUEIDS, leagueIdArray);
        }
        btResultDialogFragment.setArguments(bundle);
        return btResultDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        binding.tvClose.setOnClickListener(this);
        binding.tvMore.setOnClickListener(this);
        binding.svSort.setSwitchText(new String[]{"热门盘口", "时间"});
        binding.svMarket.setSwitchText(new String[]{"欧洲盘", "香港盘"});
        binding.svSort.setCheckedListener(index -> {
            int orderBy = viewModel.getOrderBy(index);
            RxBus.getDefault().post(new BetContract(BetContract.ACTION_SORT_CHANGE, orderBy));
            SPUtils.getInstance().put(SPKey.BT_MATCH_LIST_ORDERBY, orderBy);
        });
        binding.svMarket.setCheckedListener(index -> {
            int market = viewModel.getMarket(index);
            RxBus.getDefault().post(new BetContract(BetContract.ACTION_MARKET_CHANGE, market));
            SPUtils.getInstance().put(SPKey.BT_MATCH_LIST_ODDTYPE, market);
        });

        binding.ivGoSportRegular.setOnClickListener(v -> {
            if (TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
                goWebView(getString(R.string.txt_sport_official), Constant.URL_SPORT_RULES_OFFICIAL, false);
            } else if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                goWebView(getString(R.string.txt_sport_inter), Constant.URL_SPORT_RULES_INTER, false);
            }
        });
        long[] leagues = getArguments().getLongArray(KEY_LEAGUEIDS);
        if (leagues != null && leagues.length > 0) {
            for (int i = 0; i < leagues.length; i++) {
                mLeagueIdList.add(leagues[i]);
            }
        }
        if (mLeagueIdList == null || mLeagueIdList.isEmpty()) {
            binding.llLeague.setVisibility(View.VISIBLE);
            binding.tvHaschoised.setVisibility(View.GONE);
        } else {
            binding.llLeague.setVisibility(View.GONE);
            binding.tvHaschoised.setVisibility(View.VISIBLE);
            binding.tvHaschoised.setText("(已选" + mLeagueIdList.size() + ")");
        }
        if (TextUtils.equals(mPlatform, PLATFORM_PM) || TextUtils.equals(mPlatform, PLATFORM_FB)) {
            binding.layoutGoWeb.setVisibility(View.VISIBLE);
            binding.ivGoWeb.setOnClickListener(v -> {
                if (ClickUtil.isFastClick()) {
                    return;
                }
                viewModel.getPlayUrl(mPlatform);
            });
        } else {
            binding.layoutGoWeb.setVisibility(View.GONE);
        }

        if (mPlatform.equals(PLATFORM_FBXC) || mPlatform.equals(PLATFORM_PMXC)) {
            binding.layoutGoSportRegular.setVisibility(View.VISIBLE);
        } else {
            binding.layoutGoSportRegular.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {

        int orderBy = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ORDERBY, 1);
        binding.svSort.setCurrentPosition(viewModel.getOrderByPosition(orderBy));
        binding.svSort.setChecked(viewModel.getOrderByPosition(orderBy) == 1);

        int market = SPUtils.getInstance().getInt(SPKey.BT_MATCH_LIST_ODDTYPE, 1);
        binding.svMarket.setCurrentPosition(viewModel.getMarketPosition(market));
        binding.svMarket.setChecked(viewModel.getMarketPosition(market) == 1);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.bt_dialog_setting;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataPlayUrl.observe(getViewLifecycleOwner(), map -> {
            String url = map.get("url").toString();
            AppUtil.goBrowser(getContext(), url);
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_close) {
            dismiss();
        } else if (id == R.id.tv_more) {
            dismiss();
            if (getActivity() instanceof MainActivity) {
                MainActivity activity = (MainActivity) getActivity();
                BtLeagueDialogFragment btLeagueDialogFragment = BtLeagueDialogFragment.getInstance(activity.getSettingLeagueList(), activity.getSportId(), activity.getPlayMethodType(), mLeagueIdList);
                btLeagueDialogFragment.show(getParentFragmentManager(), "BtLeagueDialogFragment");
            }
        }
    }

    @Override
    public TemplateBtCarViewModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(FBBtCarViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(PMBtCarViewModel.class);
        }
    }

    private void goWebView(String title, String path, boolean isContainTitle) {
        String url = DomainUtil.getH5Domain2() + path;
        BrowserActivity.start(getContext(), title, url, isContainTitle);
    }
}
