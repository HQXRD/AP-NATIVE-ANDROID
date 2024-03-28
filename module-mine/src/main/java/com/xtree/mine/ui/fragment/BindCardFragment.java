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
import com.xtree.mine.databinding.FragmentBindCardBinding;
import com.xtree.mine.databinding.ItemBindCardBinding;
import com.xtree.mine.ui.viewmodel.BindCardViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.BankCardVo;

import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;

/**
 * 绑定银行卡
 */
@Route(path = RouterFragmentPath.Mine.PAGER_BIND_CARD)
public class BindCardFragment extends BaseFragment<FragmentBindCardBinding, BindCardViewModel> {
    private static final String ARG_TOKEN_SIGN = "tokenSign";
    private static final String ARG_MARK = "mark";

    private String tokenSign;
    private String mark = "bindcard";
    private boolean hasCard = false;

    ItemBindCardBinding binding2;

    CachedAutoRefreshAdapter<BankCardVo> mAdapter;

    public BindCardFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        getBankCardList(); // 获取银行卡列表
    }

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.tvwAdd.setOnClickListener(v -> {
            CfLog.i("****** add");
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_CARD_ADD, getArguments());
        });

        binding.tvwLock.setOnClickListener(v -> {
            CfLog.i("****** lock");
            if (mAdapter.isEmpty()) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("id", mAdapter.get(0).id);
            startContainerFragment(RouterFragmentPath.Mine.PAGER_BIND_CARD_LOCK, bundle);
        });

        mAdapter = new CachedAutoRefreshAdapter<BankCardVo>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_bind_card, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemBindCardBinding.bind(holder.itemView);
                BankCardVo vo = get(position);
                binding2.tvwUserName.setText(vo.user_name);
                binding2.tvwBindTime.setText(vo.atime);
                binding2.tvwType.setText(vo.bank_name);
                binding2.tvwAccount.setText(vo.account);
                binding2.tvwTypeTitle.setText(R.string.txt_open_acc_bank);
                binding2.tvwAccTitle.setText(R.string.txt_card_num);

                if (vo.status.equals("1")) {
                    binding2.tvwStatus.setVisibility(View.GONE);
                } else if (vo.status.equals("3")) {
                    binding2.tvwStatus.setVisibility(View.VISIBLE);
                    binding2.tvwStatus.setText(R.string.txt_card_locked);
                } else {
                    // 2-可能是删除的, 目前只看到1和3
                    binding2.tvwStatus.setVisibility(View.GONE);
                }

            }
        };

        binding.rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvMain.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            tokenSign = getArguments().getString(ARG_TOKEN_SIGN);
            mark = getArguments().getString(ARG_MARK);
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bind_card;
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
        viewModel.liveDataCardList.observe(this, vo -> {
            CfLog.i("******");
            // 如果是列表为空的情况,跳到增加页,并关闭当前页(关闭是因为有时会提示最多只能绑定0张卡,或者死循环)
            if (vo.status == 1) {
                binding.tvwAdd.performClick(); // 跳到增加绑定页
                getActivity().finish();
                return;
            }

            if (vo.banklist != null && !vo.banklist.isEmpty()) {
                CfLog.i("****** 这是列表");
                hasCard = true;
                mAdapter.clear();
                mAdapter.addAll(vo.banklist);
                for (int i = 0; i < vo.banklist.size(); i++) {
                    if (vo.banklist.get(i).status.equals("3")) {
                        binding.tvwLock.setVisibility(View.GONE);
                        binding.tvwAdd.setVisibility(View.GONE);
                    }
                }
            }

            if (!TextUtils.isEmpty(vo.num)) {
                String txt = "<font color=#EE5A5A>" + vo.num + "</font>";
                String html = getString(R.string.txt_bind_most_cards, txt);
                binding.tvwTip.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));

                int max = Integer.parseInt(vo.num);
                int count = Integer.parseInt(vo.binded);
                if (count < max) {
                    //binding.tvwAdd.setVisibility(View.VISIBLE); // 锁定时不要显示
                } else {
                    binding.tvwAdd.setVisibility(View.GONE);
                }
            }
        });

    }

    private void getBankCardList() {
        HashMap map = new HashMap();
        map.put("check", tokenSign);
        map.put("mark", mark);
        map.put("client", "m");
        viewModel.getBankCardList(map);
    }

}