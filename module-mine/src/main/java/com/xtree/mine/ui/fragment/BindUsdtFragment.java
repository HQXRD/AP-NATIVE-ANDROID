package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentBindUsdtBinding;
import com.xtree.mine.databinding.ItemBindCardBinding;
import com.xtree.mine.ui.viewmodel.BindUsdtViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.UsdtVo;
import com.xtree.mine.vo.UserUsdtJumpVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 绑定USDT 列表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_USDT)
public class BindUsdtFragment extends BaseFragment<FragmentBindUsdtBinding, BindUsdtViewModel> {

    private String tokenSign = "";
    private String mark = "bindusdt";

    UserUsdtJumpVo mUserUsdtJumpVo;

    ItemBindCardBinding binding2;

    CachedAutoRefreshAdapter<UsdtVo> mAdapter;

    public BindUsdtFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvwTitle.setText(mUserUsdtJumpVo.title);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCardList(); // 获取卡列表
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.tvwAdd.setOnClickListener(v -> {
            CfLog.i("****** add");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_USDT_ADD, getArguments());
        });

        mAdapter = new CachedAutoRefreshAdapter<UsdtVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_bind_card, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemBindCardBinding.bind(holder.itemView);
                UsdtVo vo = get(position);
                binding2.tvwUserName.setText(vo.user_name);
                binding2.tvwBindTime.setText(vo.atime);
                binding2.tvwType.setText(vo.usdt_type);
                binding2.tvwAccount.setText(vo.usdt_card);
                binding2.tvwStatus.setVisibility(View.GONE);
                binding2.tvwTypeTitle.setText(R.string.txt_type_c);
                binding2.tvwAccTitle.setText(R.string.txt_wallet_address_c);

                if (mUserUsdtJumpVo.isShowType) {
                    binding2.llType.setVisibility(View.VISIBLE);
                } else {
                    binding2.llType.setVisibility(View.GONE);
                }

                if (vo.status.equals("1")) {
                    binding2.tvwRebind.setVisibility(View.VISIBLE);
                } else {
                    binding2.tvwRebind.setVisibility(View.GONE);
                }

                binding2.tvwRebind.setOnClickListener(v -> {
                    Bundle bundle = getArguments();
                    //bundle.putString("id", vo.id);
                    mUserUsdtJumpVo.type = vo.usdt_type;
                    mUserUsdtJumpVo.id = vo.id;
                    bundle.putParcelable("obj", mUserUsdtJumpVo);
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_USDT_REBIND, bundle);
                });

            }
        };

        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            mUserUsdtJumpVo = getArguments().getParcelable("obj");
            CfLog.i(mUserUsdtJumpVo.toString());
            tokenSign = mUserUsdtJumpVo.tokenSign;
            mark = mUserUsdtJumpVo.mark;
            viewModel.key = mUserUsdtJumpVo.key;
        }

        if (mUserUsdtJumpVo == null) {
            CfLog.e("***** args is null... ");
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_usdt;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BindUsdtViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(BindUsdtViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataCardList.observe(this, vo -> {

            if (vo.status == 1) {
                binding.tvwAdd.performClick(); // 跳到增加绑定页
                getActivity().finish();
                return;
            }
            if (vo.banklist != null) {
                mAdapter.clear();
                mAdapter.addAll(vo.banklist);
            }

            if (!TextUtils.isEmpty(vo.num)) {
                String txt = "<font color=#EE5A5A>" + vo.num + "</font>";
                String html = getString(R.string.txt_bind_most_usdt, txt, mUserUsdtJumpVo.key.toUpperCase());
                binding.tvwTip.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));

                int max = Integer.parseInt(vo.num);
                int count = Integer.parseInt(vo.binded);
                if (count < max) {
                    binding.tvwAdd.setVisibility(View.VISIBLE);
                } else {
                    binding.tvwAdd.setVisibility(View.GONE);
                }
            }

        });
    }

    private void getCardList() {
        HashMap map = new HashMap();
        map.put("check", tokenSign);
        map.put("mark", mark);
        map.put("client", "m");
        viewModel.getCardList(map);
    }

}