package com.xtree.home.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentHomeBinding;
import com.xtree.home.ui.GameAdapter;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.GameVo;
import com.xtree.home.vo.NoticeVo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;

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
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public HomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.readCache(); // 从缓存读取数据并显示

        viewModel.getSettings(); // 获取公钥,配置信息
        viewModel.getBanners(); // 获取banner
        viewModel.getGameStatus(getContext()); // 获取游戏状态列表

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            viewModel.getCookie();
            viewModel.getFBGameTokenApi();
            viewModel.getFBXCGameTokenApi();
            viewModel.getPMGameTokenApi();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

        viewModel.liveDataCookie.observe(getViewLifecycleOwner(), cookieVo -> {
            KLog.d("************");
            viewModel.getNotices(); // 获取公告
            viewModel.getProfile(); // 获取个人信息
            viewModel.getVipInfo(); // 获取VIP信息
            //viewModel.getFBGameTokenApi();
            //viewModel.getPMGameTokenApi();
        });

        viewModel.liveDataBanner.observe(getViewLifecycleOwner(), list -> {
            // banner
            binding.bnrTop.setDatas(list);
        });

        viewModel.liveDataNotice.observe(getViewLifecycleOwner(), list -> {
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
        });

        viewModel.liveDataGames.observe(getViewLifecycleOwner(), list -> {
            KLog.i("size: " + list.size());
            //KLog.d(list.get(0));
            gameAdapter.clear();
            gameAdapter.addAll(list);
        });

        viewModel.liveDataPlayUrl.observe(getViewLifecycleOwner(), map -> {
            CfLog.d("*** " + new Gson().toJson(map));
            // 跳转到游戏H5
            gameAdapter.playGame(map.get("url").toString());
        });
        viewModel.liveDataProfile.observe(getViewLifecycleOwner(), vo -> {
            CfLog.d("*** " + new Gson().toJson(vo));
            binding.clLoginNot.setVisibility(View.GONE);
            binding.clLoginYet.setVisibility(View.VISIBLE);

            binding.tvwName.setText(vo.username);
            binding.tvwBalance.setText("￥" + vo.availablebalance); // creditwallet.balance_RMB
        });
        viewModel.liveDataVipInfo.observe(getViewLifecycleOwner(), vo -> {
            CfLog.d("*** " + vo.toString());
            binding.ivwVip.setImageLevel(vo.display_level); // display_level
        });

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

        binding.bnrTop.setOnBannerListener((OnBannerListener<BannersVo>) (data, position) -> {
            // 如果banner有链接 跳转到链接
            if (!TextUtils.isEmpty(data.link)) {
                String url = DomainUtil.getDomain() + data.link;
                //Uri uri = Uri.parse(url);
                //Intent it = new Intent(Intent.ACTION_VIEW, uri);
                //Intent it = new Intent(getContext(), BrowserActivity.class);
                //it.setData(uri);
                //startActivity(it);
                BrowserActivity.start(getContext(), data.title, url);
            }
        });

        binding.ivwCs.setOnClickListener(view -> {
            String title = getString(R.string.txt_custom_center);
            String url = DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE;
            new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url)).show();
        });
        binding.ivwClose.setOnClickListener(view -> {
            binding.llNotice.setVisibility(View.GONE);
            binding.ivwNotice.setVisibility(View.VISIBLE);
        });
        binding.ivwNotice.setOnClickListener(view -> {
            binding.llNotice.setVisibility(View.VISIBLE);
            binding.ivwNotice.setVisibility(View.INVISIBLE);
        });
        binding.llNotice.setOnClickListener(view -> {
            String title = getString(R.string.txt_msg_center);
            String url = DomainUtil.getDomain2() + Constant.URL_MY_MESSAGES;
            BrowserActivity.start(getContext(), title, url, true);
        });

        //cl_login_not
        binding.clLoginNot.setOnClickListener(view -> {
            // 登录
            KLog.i("**************");
            //binding.btnLogin.setVisibility(View.VISIBLE);
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
        });
        binding.tvwDeposit.setOnClickListener(view -> {
            // 存款
            KLog.i("**************");
            goRecharge();
        });
        binding.tvwWithdraw.setOnClickListener(view -> {
            // 提现
            KLog.i("**************");
            //ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_WITHDRAW).navigation();
            //startContainerFragment(RouterFragmentPath.Wallet.PAGER_WITHDRAW);
            String title = getString(R.string.txt_withdraw);
            String url = DomainUtil.getDomain2() + Constant.URL_WITHDRAW;
            BrowserActivity.start(getContext(), title, url, true);
        });
        binding.tvwTrans.setOnClickListener(view -> {
            // 转账
            KLog.i("**************");
            startContainerFragment(RouterFragmentPath.Wallet.PAGER_TRANSFER);
        });
        binding.tvwMember.setOnClickListener(view -> {
            // 会员
            KLog.i("**************");
            String title = getString(R.string.txt_vip_center);
            String url = DomainUtil.getDomain2() + Constant.URL_VIP_CENTER;
            BrowserActivity.start(getContext(), title, url, true);
            //new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url, true)).show();
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
                // int end = manager.findLastCompletelyVisibleItemPosition(); // 最后一个完整可见的
                // View view = manager.findViewByPosition(position);
                if (position < 0 || position > gameAdapter.size() - 1) {
                    return;
                }
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

            rbtn.setOnClickListener(v -> {
                String tag = v.getTag().toString();
                int pid = Integer.parseInt(tag.replace("tp_", ""));
                smoothToPosition(pid);
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

    private void goRecharge() {
        //Intent intent = new Intent(getContext(), ContainerActivity.class);
        //intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Recharge.PAGER_RECHARGE);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowBack", true);
        //intent.putExtra(ContainerActivity.BUNDLE, bundle);
        //startActivity(intent);
        startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle);
    }

}
