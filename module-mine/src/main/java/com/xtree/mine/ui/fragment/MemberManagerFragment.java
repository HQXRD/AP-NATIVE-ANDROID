package com.xtree.mine.ui.fragment;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.ListDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.FragmentMemberManageBinding;
import com.xtree.mine.ui.viewmodel.MineViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.mine.vo.MemberManagerVo;
import com.xtree.mine.vo.MemberUserInfoVo;

import java.util.ArrayList;
import java.util.HashMap;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import project.tqyb.com.library_res.databinding.ItemTextBinding;

@Route(path = RouterFragmentPath.Mine.PAGER_MEMBER_MANAGER)
public class MemberManagerFragment extends BaseFragment<FragmentMemberManageBinding, MineViewModel> {
    BasePopupView ppw = null; // 底部弹窗 (选择**菜单)
    ItemTextBinding binding2;
    MemberManagerAdapter adapter;
    int curPage = 1;

    @Override
    public void initView() {
        binding.ivwBack.setOnClickListener(v -> getActivity().finish());

        binding.tvwChooseSort.setOnClickListener(v -> showChooseDialog());

        binding.btnSearch.setOnClickListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }

            binding.refreshLayout.setEnableLoadMore(true);
            binding.refreshLayout.setEnableRefresh(true);
            curPage = 1;
            LoadingDialog.show(getContext());
            adapter.clear();
            searchMember(curPage);
        });

        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (ClickUtil.isFastClick()) {
                return;
            }
            binding.refreshLayout.setEnableLoadMore(true);
            binding.refreshLayout.setEnableRefresh(true);
            curPage = 1;
            adapter.clear();
            searchMember(curPage);
        });

        binding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            curPage++;
            searchMember(curPage);
        });
    }

    @Override
    public void initData() {
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setEnableRefresh(false);

        adapter = new MemberManagerAdapter(getContext(), new MemberManagerAdapter.ICallBack() {
            @Override
            public void onClick(MemberUserInfoVo vo, String msg) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", vo.userid);
                bundle.putString("userName", vo.username);
                CfLog.i("vo.userid :　" + vo.userid);
                if (msg.equals(MemberManagerAdapter.BAT_RECORD)) {
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_BT_REPORT, bundle); // 投注记录
                } else if (msg.equals(MemberManagerAdapter.ACCOUNT_RECORD)) {
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_ACCOUNT_CHANGE, bundle); // 账变记录
                } else if (msg.equals(MemberManagerAdapter.TRANSFER_MEMBER)) {
                    bundle.putString("page", RouterFragmentPath.Mine.PAGER_MEMBER_TRANSFER);
                    startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle); // 下级转账
                }
            }

            @Override
            public void onSearch(String name) {
                binding.etUsername.setText(name);

                if (ClickUtil.isFastClick()) {
                    return;
                }
                binding.refreshLayout.setEnableLoadMore(true);
                binding.refreshLayout.setEnableRefresh(true);
                curPage = 1;
                LoadingDialog.show(getContext());
                adapter.clear();
                searchMember(curPage);
            }
        });
        binding.rcvMemberManger.setAdapter(adapter);
        binding.rcvMemberManger.setLayoutManager(new LinearLayoutManager(getContext()));

        //binding.etUsername.setText(SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME));
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_member_manage;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public MineViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(MineViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.liveDataMemberManager.observe(this, vo -> {
            adapter.setIsShow(vo.isshow);
            binding.refreshLayout.finishRefresh();
            binding.refreshLayout.finishLoadMore();

            binding.refreshLayout.setEnableLoadMore(!vo.mobile_page.p.equals(vo.mobile_page.total_page));

            if (vo.users != null && !vo.users.isEmpty()) {
                binding.tvwNoData.setVisibility(View.GONE);
                adapter.addAll(vo.users);
            }

            // 本级
            if (vo.bread != null && !vo.bread.isEmpty()) {
                if (vo.bread != null && !vo.bread.isEmpty()) {
                    SpannableStringBuilder sb = new SpannableStringBuilder();

                    for (MemberManagerVo.UserVo t : vo.bread) {
                        // 由于名称为 apple@00 进行分割
                        String modifiedString = t.username.split("@")[0];
                        sb.append(modifiedString).append(" > ");

                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View view) {
                                binding.etUsername.setText(modifiedString);
                                LoadingDialog.show(getContext());
                                curPage = 0;
                                searchMember(1);
                            }
                        };

                        int startIndex = sb.length() - modifiedString.length() - 3;
                        int endIndex = sb.length() - 3;

                        sb.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.cl_gap)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (sb.toString().endsWith(" > ")) {
                        sb.replace(sb.length() - 3, sb.length(), "");
                    }

                    binding.tvwBread.setText(sb);
                    binding.tvwBread.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        });
    }

    private void searchMember(int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", binding.etUsername.getText().toString());
        map.put("userid", SPUtils.getInstance().getString(SPKeyGlobal.USER_ID));
        map.put("p", String.valueOf(page));
        checkSort(map);
        viewModel.getMemberManager(map);
    }

    private void showChooseDialog() {
        CachedAutoRefreshAdapter adapter = new CachedAutoRefreshAdapter<String>() {

            @NonNull
            @Override
            public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text, parent, false));
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
                binding2 = ItemTextBinding.bind(holder.itemView);
                String txt = get(position);
                binding2.tvwTitle.setText(txt);
                binding2.tvwTitle.setOnClickListener(v -> {
                    binding.tvwChooseSort.setText(txt);
                    ppw.dismiss();
                });
            }
        };

        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.txt_userpoint_desc));
        list.add(getString(R.string.txt_userpoint_asc));
        list.add(getString(R.string.txt_username_desc));
        list.add(getString(R.string.txt_username_asc));
        list.add(getString(R.string.txt_children_desc));
        list.add(getString(R.string.txt_children_asc));
        list.add(getString(R.string.txt_available_balance_desc));
        list.add(getString(R.string.txt_available_balance_asc));
        list.add(getString(R.string.txt_team_balance_desc));
        list.add(getString(R.string.txt_team_balance_asc));
        adapter.addAll(list);
        ppw = new XPopup.Builder(getContext()).asCustom(new ListDialog(getContext(), "", adapter));
        ppw.show();
    }

    private void checkSort(HashMap<String, String> map) {
        if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_userpoint_desc))) {
            map.put("orderby", "userpoint");
            map.put("sort", "desc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_userpoint_asc))) {
            map.put("orderby", "userpoint");
            map.put("sort", "asc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_username_desc))) {
            map.put("orderby", "username");
            map.put("sort", "desc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_username_asc))) {
            map.put("orderby", "username");
            map.put("sort", "asc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_children_desc))) {
            map.put("orderby", "children_num");
            map.put("sort", "desc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_children_asc))) {
            map.put("orderby", "children_num");
            map.put("sort", "asc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_available_balance_desc))) {
            map.put("orderby", "availablebalance");
            map.put("sort", "desc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_available_balance_asc))) {
            map.put("orderby", "availablebalance");
            map.put("sort", "asc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_team_balance_desc))) {
            map.put("orderby", "team_balance");
            map.put("sort", "desc");
        } else if (binding.tvwChooseSort.getText().toString().equals(getString(R.string.txt_team_balance_asc))) {
            map.put("orderby", "team_balance");
            map.put("sort", "asc");
        } else {
            map.put("orderby", "children_num");
            map.put("sort", "desc");
        }
    }
}
