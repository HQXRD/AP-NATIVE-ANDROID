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
import com.xtree.mine.databinding.FragmentBindAwBinding;
import com.xtree.mine.databinding.ItemBindAwBinding;
import com.xtree.mine.ui.viewmodel.BindCardViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.AWVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 绑定支付宝、微信列表
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_ALIPAY_WECHAT)
public class BindAlipayWechatFragment extends BaseFragment<FragmentBindAwBinding, BindCardViewModel> {
    private final String ARG_TOKEN_SIGN = "tokenSign";
    private final String ARG_MARK = "mark";
    private final String ARG_TYPE = "type";
    private final String TYPE_ALIPAY = "alipay";
    private final String TYPE_WECHAT = "wechat";

    private String tokenSign;
    private String mark;
    private String type;

    CachedAutoRefreshAdapter<AWVo> mAdapter;

    public BindAlipayWechatFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getAWList(); // 获取支付宝或微信列表
    }

    @Override
    public void initView() {
        initArguments();
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.tvwAdd.setOnClickListener(v -> {
            CfLog.i("****** add");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_AW_ADD, getArguments());
        });

        mAdapter = new CachedAutoRefreshAdapter<AWVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_bind_aw, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                ItemBindAwBinding binding2 = ItemBindAwBinding.bind(holder.itemView);
                AWVo vo = get(position);
                switch (type) {
                    case TYPE_ALIPAY: {
                        binding2.tvName.setText(R.string.txt_alipay_name);
                        binding2.tvNickname.setText(R.string.txt_alipay_name);
                        binding2.tvCode.setText(R.string.txt_alipay_name);
                        break;
                    }
                    case TYPE_WECHAT: {
                        binding2.tvName.setText(R.string.txt_wechat_name);
                        binding2.tvNickname.setText(R.string.txt_wechat_name);
                        binding2.tvCode.setText(R.string.txt_wechat_name);
                        break;
                    }
                }
            }
        };

        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);

    }

    public void initArguments() {
        if (getArguments() != null) {
            tokenSign = getArguments().getString(ARG_TOKEN_SIGN);
            mark = getArguments().getString(ARG_MARK);
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public void initData() {
        super.initData();
        switch (type) {
            case TYPE_ALIPAY: {
                binding.tvwTitle.setText(getString(R.string.txt_bind_alipay));
                break;
            }
            case TYPE_WECHAT: {
                binding.tvwTitle.setText(getString(R.string.txt_bind_wechat));
                break;
            }
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_aw;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public BindCardViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(BindCardViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataAWList.observe(this, vo -> {
            CfLog.i("******");
            // 如果是列表为空的情况,跳到增加页,并关闭当前页(关闭是因为有时会提示最多只能绑定0张卡,或者死循环)
            if (vo.banklist.isEmpty()) {
                //binding.tvwAdd.performClick(); // 跳到增加绑定页
                //requireActivity().finish();
                return;
            }

            if (vo.banklist != null && !vo.banklist.isEmpty()) {
                CfLog.i("****** 这是列表");
                mAdapter.clear();
                mAdapter.addAll(vo.banklist);
            }

            if (!TextUtils.isEmpty(vo.num)) {
                String txt = "";
                switch (type) {
                    case TYPE_ALIPAY: {
                        txt = getString(R.string.txt_alipay);
                        break;
                    }
                    case TYPE_WECHAT: {
                        txt = getString(R.string.txt_wechat);
                        break;
                    }
                }
                binding.tvwTip.setText(String.format(getString(R.string.txt_bind_most_aw), vo.num, txt));

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

    private void getAWList() {
        HashMap map = new HashMap();
        map.put("check", tokenSign);
        map.put("mark", mark);
        map.put("client", "m");
        viewModel.getAWList(map, type);
    }

}