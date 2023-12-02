package com.xtree.home.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentHomeBinding;
import com.xtree.home.ui.GameAdapter;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.CookieVo;
import com.xtree.home.vo.GameVo;
import com.xtree.home.vo.NoticeVo;
import com.xtree.home.vo.ProfileVo;
import com.xtree.home.vo.VipInfoVo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 首页
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

    GameAdapter gameAdapter;
    private int curPId = 0; // 当前选中的游戏大类型 1体育,2真人,3电子,4电竞,5棋牌,6彩票

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
        initLiveData();
        viewModel.readCache(); // 从缓存读取数据并显示

        viewModel.getSettings(); // 获取公钥,配置信息
        viewModel.getBanners(); // 获取banner
        viewModel.getGameStatus(getContext()); // 获取游戏状态列表

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            viewModel.getCookie();
        }

    }

    public void initLiveData() {

        viewModel.liveDataCookie.observe(getViewLifecycleOwner(), new Observer<CookieVo>() {
            @Override
            public void onChanged(CookieVo cookieVo) {
                KLog.d("************");
                viewModel.getNotices(); // 获取公告
                viewModel.getProfile(); // 获取个人信息
                viewModel.getVipInfo(); // 获取VIP信息
            }
        });

        viewModel.liveDataBanner.observe(getViewLifecycleOwner(), new Observer<List<BannersVo>>() {
            @Override
            public void onChanged(List<BannersVo> list) {
                binding.bnrTop.setDatas(list);
            }
        });

        viewModel.liveDataNotice.observe(getViewLifecycleOwner(), new Observer<List<NoticeVo>>() {
            @Override
            public void onChanged(List<NoticeVo> list) {
                if (list.isEmpty()) {
                    binding.llNotice.setVisibility(View.GONE);
                    binding.ivwNotice.setVisibility(View.GONE);
                } else {
                    StringBuffer sb = new StringBuffer();
                    for (NoticeVo vo : list) {
                        sb.append(vo.title + " ");
                    }
                    binding.llNotice.setVisibility(View.VISIBLE);
                    binding.tvwNotice.setText(sb.toString());
                }
            }
        });

        viewModel.liveDataGames.observe(getViewLifecycleOwner(), new Observer<List<GameVo>>() {
            @Override
            public void onChanged(List<GameVo> list) {
                KLog.i("size: " + list.size());
                //KLog.d(list.get(0));
                gameAdapter.addAll(list);
            }
        });

        viewModel.liveDataPlayUrl.observe(getViewLifecycleOwner(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                CfLog.d("*** " + new Gson().toJson(map));
                // 跳转到游戏H5
                gameAdapter.playGame(map.get("url").toString());
            }
        });
        viewModel.liveDataProfile.observe(getViewLifecycleOwner(), new Observer<ProfileVo>() {
            @Override
            public void onChanged(ProfileVo vo) {
                CfLog.d("*** " + new Gson().toJson(vo));
                binding.clLoginNot.setVisibility(View.GONE);
                binding.clLoginYet.setVisibility(View.VISIBLE);

                binding.tvwName.setText(vo.username);
                binding.tvwBalance.setText("￥" + vo.availablebalance); // creditwallet.balance_RMB
            }
        });
        viewModel.liveDataVipInfo.observe(getViewLifecycleOwner(), new Observer<VipInfoVo>() {
            @Override
            public void onChanged(VipInfoVo vo) {
                CfLog.d("*** " + vo.toString());
                binding.ivwVip.setImageLevel(vo.display_level); // display_level
            }
        });

    }

    @Override
    public void initViewObservable() {
        viewModel.itemClickEvent.observe(this, (Observer<String>) s -> ToastUtils.showShort(s));
    }

    public void initView() {

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
                    //Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    Intent it = new Intent(getContext(), BrowserActivity.class);
                    it.setData(uri);
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

        binding.btnLogin.setOnClickListener(view -> {
                    //viewModel.login(getContext(), username, pwd); // 要等公钥接口返回结果以后 才能调用
                }
        );
        //cl_login_not
        binding.clLoginNot.setOnClickListener(view -> {
            // 登录
            KLog.i("**************");
            //binding.btnLogin.setVisibility(View.VISIBLE);
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

        GameAdapter.ICallBack mCallBack = new GameAdapter.ICallBack() {
            @Override
            public void onClick(String gameAlias, String gameId) {
                viewModel.getPlayUrl(gameAlias, gameId);
            }
        };

        gameAdapter = new GameAdapter(getContext(), mCallBack);
        binding.rcvList.setAdapter(gameAdapter);
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
                //int end = manager.findLastCompletelyVisibleItemPosition(); // 最后一个完整可见的
                // View view = manager.findViewByPosition(position);
                GameVo vo = gameAdapter.get(position);
                if (vo.pId != curPId) {
                    //CfLog.d("类型发生了改变 " + curPId + ", " + vo.pId);
                    curPId = vo.pId;
                    RadioButton rbtn = binding.rgpType.findViewWithTag("tp_" + curPId);
                    rbtn.setChecked(true);
                }
            }
        });

        // 1体育, 2真人, 3电子, 4电竞, 5棋牌, 6彩票
        int count = binding.rgpType.getChildCount();
        RadioButton rbtn;
        Drawable dr;

        for (int i = 0; i < count; i++) {
            dr = getResources().getDrawable(R.drawable.hm_game_type_selector);
            rbtn = (RadioButton) binding.rgpType.getChildAt(i);
            dr.setLevel(i + 1);
            rbtn.setButtonDrawable(dr);

            rbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = v.getTag().toString();
                    int pid = Integer.parseInt(tag.replace("tp_", ""));
                    smoothToPosition(pid);
                }
            });
        }

    }

    private void smoothToPosition(int pid) {
        List<GameVo> list = gameAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).pId == pid) {
                binding.rcvList.smoothScrollToPosition(i);
                return;
            }
        }
    }

}
