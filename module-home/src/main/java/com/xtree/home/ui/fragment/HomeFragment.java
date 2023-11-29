package com.xtree.home.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.DomainUtil;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentHomeBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.NoticeVo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 首页
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

    @Override
    public void initData() {
        //viewModel.getBanners();
        viewModel.getSettings(getContext());

        String username = "testkite1002";
        String pwd = "kite123456";
        binding.btnLogin.setOnClickListener(view -> viewModel.login(getContext(), username, pwd));
        //viewModel.login(getContext(), username, pwd); // 要等公钥接口返回结果以后 才能调用

        //binding.btnBanner.setOnClickListener(view -> viewModel.getBanners());
        //binding.btnSetting.setOnClickListener(view -> viewModel.getSettings(getContext()));
        //binding.btnCookie.setOnClickListener(view -> viewModel.getCookie(getContext()));
        //
        //binding.btnLogin2.setOnClickListener(view -> {
        //    String username2 = binding.edtName.getText().toString().trim();
        //    String pwd2 = binding.edtPwd.getText().toString().trim();
        //    if (!username2.isEmpty() && !pwd2.isEmpty()) {
        //        viewModel.login(getContext(), username2, pwd2);
        //    }
        //});

        initView();
        viewModel.liveDataBanner.observe(getViewLifecycleOwner(), new Observer<List<BannersVo>>() {
            @Override
            public void onChanged(List<BannersVo> list) {
                binding.bnrTop.setDatas(list);
            }
        });
        viewModel.getBanners();

        viewModel.liveDataNotice.observe(getViewLifecycleOwner(), new Observer<List<NoticeVo>>() {
            @Override
            public void onChanged(List<NoticeVo> noticeVos) {
                if (noticeVos.isEmpty()) {
                    binding.llNotice.setVisibility(View.GONE);
                    binding.ivwNotice.setVisibility(View.GONE);
                } else {
                    StringBuffer sb = new StringBuffer();
                    for (NoticeVo vo : noticeVos) {
                        sb.append(vo.title + " ");
                    }
                    binding.llNotice.setVisibility(View.VISIBLE);
                    binding.tvwNotice.setText(sb.toString());
                }
            }
        });
        viewModel.getNotices();

    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));
    }

    private void initView() {

        binding.bnrTop.setIndicator(new CircleIndicator(getContext())); // 增加小圆点
        //binding.bnrTop.setBannerGalleryEffect(20, 12, 0.8f);// 画廊效果
        //binding.bnrTop.setBannerRound2(20);
        binding.bnrTop.setAdapter(new BannerImageAdapter<BannersVo>(new ArrayList<>()) {
            @Override
            public void onBindView(BannerImageHolder holder, BannersVo data, int position, int size) {
                Glide.with(getContext()).load(data.picture).placeholder(R.mipmap.hm_bnr_01).into(holder.imageView);
            }
        });

        binding.bnrTop.setOnBannerListener(new OnBannerListener<BannersVo>() {
            @Override
            public void OnBannerClick(BannersVo data, int position) {
                // 如果banner有链接 跳转到链接
                if (!TextUtils.isEmpty(data.link)) {
                    String url = DomainUtil.getDomain() + data.link;
                    Uri uri = Uri.parse(url);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                }
            }
        });

        binding.ivwClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llNotice.setVisibility(View.GONE);
                binding.ivwNotice.setVisibility(View.VISIBLE);
            }
        });
        binding.ivwNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llNotice.setVisibility(View.VISIBLE);
                binding.ivwNotice.setVisibility(View.INVISIBLE);
            }
        });

        //cl_login_not
        binding.clLoginNot.setOnClickListener(view -> {
            // 登录
            KLog.i("**************");
            binding.btnLogin.setVisibility(View.VISIBLE);
        });
        binding.tvwDeposit.setOnClickListener(view -> {
            // 存款
            KLog.i("**************");
        });
        binding.tvwWithdraw.setOnClickListener(view -> {
            // 提现
            KLog.i("**************");
        });
        binding.tvwTrans.setOnClickListener(view -> {
            // 转账
            KLog.i("**************");
        });
        binding.tvwMember.setOnClickListener(view -> {
            // 会员
            KLog.i("**************");
        });

        //RoomAdapter roomAdapter = new RoomAdapter(getContext());
        //binding.rcvList.setAdapter(roomAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.rcvList.setLayoutManager(manager);

        binding.rcvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int position = manager.findFirstVisibleItemPosition();
                // manager.findLastCompletelyVisibleItemPosition(); // 最后一个完整可见的
                View view = manager.findViewByPosition(position);

            }
        });

    }

}
