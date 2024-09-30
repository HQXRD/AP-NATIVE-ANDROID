package com.xtree.home.ui.fragment;

import static com.xtree.home.ui.adapter.GameAdapter.PLATFORM_FB;
import static com.xtree.home.ui.adapter.GameAdapter.PLATFORM_FBXC;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.AppUpdateVo;
import com.xtree.base.vo.ProfileVo;
import com.xtree.base.widget.AppUpdateDialog;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.MsgDialog;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentHomeBinding;
import com.xtree.home.ui.adapter.GameAdapter;
import com.xtree.home.ui.custom.view.CustomFloatWindows;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.home.vo.BannersVo;
import com.xtree.home.vo.GameVo;
import com.xtree.home.vo.NoticeVo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.KLog;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * 首页
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    CustomFloatWindows customFloatWindows;
    GameAdapter gameAdapter;
    private int curPId = 0; // 当前选中的游戏大类型 1体育,2真人,3电子,4电竞,5棋牌,6彩票
    private ProfileVo mProfileVo; //最新的用戶信息
    private BasePopupView ppw = null; // 底部弹窗
    private BasePopupView ppw2 = null; // 底部弹窗
    boolean isBinding = false; // 是否正在跳转到其它页面绑定手机/YHK (跳转后回来刷新用)
    private boolean needScroll;
    private int position;
    private LinearLayoutManager manager;
    private String token;
    private boolean isFloating = false;
    private int clickCount = 0; // 点击次数 debug model
    private boolean selectUpdate;//手动更新余额
    private AppUpdateVo updateVo;//更新
    private boolean isSelectedGame = false;
    private int gameGroup = -1;
    private BasePopupView updateView;
    private String nativeAppUpdate;//App本地强制更新标志为 0不存在强更

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    /**
     * 使用hide和show后，可见不可见切换时，不再执行fragment生命周期方法，
     * 需要刷新时，使用onHiddenChanged代替
     */
    @Override
    public void onResume() {
        super.onResume();
        refresh();

        KLog.i("测试显示隐藏", "onResumeHome");
    }

    private void refresh() {
        //viewModel.readCache(); // 读取缓存,用户信息可能发生了变更
        TagUtils.tagDailyEvent(getContext());
        checkUpdate(); // 检查更新
        if (!TextUtils.isEmpty(token)) {
            CfLog.i("******");
            viewModel.getProfile();
            //checkRedPocket();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        CfLog.i("****** hidden: " + hidden);
        if (hidden) {   // 隐藏
            KLog.i("测试显示隐藏", "HideOnHiddenChangedHome");
        } else {  // 第一次可见，不会执行到这里，只会执行onResume
            //网络数据刷新
            KLog.i("测试显示隐藏", "ShowOnHiddenChangedHome");
            refresh();
        }
    }

    @Override
    protected void initImmersionBar() {
        //不实现父类方法
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
        setViewClickable(false); // 设置菜单不可点击
        viewModel.readCache(); // 从缓存读取数据并显示

        viewModel.getSettings(); // 获取公钥,配置信息
        viewModel.getBanners(); // 获取banner
        viewModel.getGameStatus(getContext()); // 获取游戏状态列表

        token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            setViewClickable(true);
            viewModel.getCookie();
            viewModel.getFBGameTokenApi();
            viewModel.getFBXCGameTokenApi();
            viewModel.getPMGameTokenApi();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("platform", "android");
        map.put("platform_set", getResources().getString(R.string.platform_set));
        viewModel.getUpdate(map);
    }

    @Override
    public void initViewObservable() {

        viewModel.liveDataCookie.observe(getViewLifecycleOwner(), cookieVo -> {
            KLog.d("************");
            viewModel.getNotices(); // 获取公告
            //viewModel.getProfile(); // 获取个人信息
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
            //RadioButton rBtn = (RadioButton) binding.rgpType.getChildAt(gameGroup);
            //rBtn.setChecked(true);
            //smoothToPosition(gameGroup);
        });

        viewModel.liveDataPlayUrl.observe(getViewLifecycleOwner(), map -> {
            CfLog.d("*** " + new Gson().toJson(map));
            // 跳转到游戏H5
            gameAdapter.playGame(map.get("url").toString(), Objects.requireNonNull(map.get("name")).toString());
        });
        viewModel.liveDataProfile.observe(getViewLifecycleOwner(), vo -> {
            CfLog.d("*** " + new Gson().toJson(vo));
            mProfileVo = vo;
            binding.clLoginNot.setVisibility(View.GONE);
            binding.clLoginYet.setVisibility(View.VISIBLE);

            binding.tvwName.setText(vo.username);
            binding.tvwBalance.setText("￥" + vo.availablebalance); // creditwallet.balance_RMB
            if (selectUpdate) {
                ToastUtils.show(this.getString(R.string.txt_rc_tip_latest_balance), ToastUtils.ShowType.Success);
                selectUpdate = false;
            }

        });
        viewModel.liveDataVipInfo.observe(getViewLifecycleOwner(), vo -> {
            CfLog.d("*** " + vo.toString());
            //意昂3去除 VIP选项
           /* if (vo.sp.equals("1")) {
                binding.tvwVip.setText("VIP " + vo.display_level); // display_level
            } else {
                binding.tvwVip.setText("VIP " + vo.level); // level
            }*/
        });
        //App更新
        viewModel.liveDataUpdate.observe(getViewLifecycleOwner(), vo -> {
            updateVo = vo;
            if (updateVo != null) {
                //存储服务器设置时间间隔
                SPUtils.getInstance().put(SPKeyGlobal.APP_INTERVAL_TIME, updateVo.interval_duration);
                //请求更新服务时间
                SPUtils.getInstance().put(SPKeyGlobal.APP_LAST_CHECK_TIME, System.currentTimeMillis());
                long versionCode = Long.valueOf(StringUtils.getVersionCode(getContext()));
                CfLog.i("versionCode = " + versionCode);
                if (versionCode < updateVo.version_code) {
                    //线上版本大于本机版本
                    if (updateVo.type == 0) {
                        //弱更
                        if (versionCode >= vo.version_code_min) {
                            showUpdate(true, updateVo); // 弱更
                        } else {
                            showUpdate(false, updateVo); // 强更
                        }

                    } else if (updateVo.type == 1) {
                        //强更
                        showUpdate(false, updateVo);
                    } else if (updateVo.type == 2) {
                        //热更
                    }
                }
            }

        });

        viewModel.liveDataRedPocket.observe(getViewLifecycleOwner(), vo -> {
            CfLog.e("Check has money : " + vo.money);
            //if (vo.status == 0) {
            //    binding.tvwMember.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.hm_ic_member_has_red, 0, 0);
            //} else {
            //    binding.tvwMember.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.hm_ic_member, 0, 0);
            //}
        });
    }

    public void initView() {
        CfLog.e(String.valueOf(getActivity().getClass()));
        if (!isFloating) {
            CfLog.e("customFloatWindows.show");
            customFloatWindows = new CustomFloatWindows(getActivity());
            customFloatWindows.show();
            isFloating = true;
        }
        if (SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN).equals("")) {
            customFloatWindows.removeView();
            isFloating = false;
        }
        //用户余额点击
        binding.clLoginYet.setOnClickListener(v -> {
            selectUpdate = true;
            viewModel.getProfile(); // 获取个人信息（刷新用户余额）
        });
        binding.bnrTop.setIndicator(new CircleIndicator(getContext())); // 增加小圆点
        //binding.bnrTop.setBannerGalleryEffect(20, 12, 0.8f);// 画廊效果
        //binding.bnrTop.setBannerRound2(20);
        binding.bnrTop.setAdapter(new BannerImageAdapter<BannersVo>(new ArrayList<>()) {
            @Override
            public void onBindView(BannerImageHolder holder, BannersVo data, int position, int size) {
                holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getContext()).load(data.picture).placeholder(R.mipmap.hm_bnr_01).into(holder.imageView);
            }
        });

        binding.bnrTop.setOnBannerListener((OnBannerListener<BannersVo>) (data, position) -> {
            if (data.link.equals("")) {
                EventBus.getDefault().post("");
                return;
            }
            char lastChar = data.link.charAt(data.link.length() - 1);
            if (!Character.isDigit(lastChar)) {
                EventBus.getDefault().post("");
                return;
            }
            String aid = "aid=";
            String noAid = "detail/";
            String result = "";
            // 如果banner有链接 跳转到链接
            if (!TextUtils.isEmpty(data.link)) {
                CfLog.e(data.toString());
                if (data.link.contains(aid)) {
                    int index = data.link.indexOf(aid) + aid.length();
                    result = data.link.substring(index);
                } else {
                    int index = data.link.indexOf(noAid) + noAid.length();
                    result = data.link.substring(index);
                }
                token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
                if (TextUtils.isEmpty(token) && result.equals("135")) {
                    ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                    return;
                }
                String url = getString(result);
                CfLog.e(url);
                BrowserActivity.start(getContext(), data.title, url, true);
            }
        });

        binding.ivwCs.setOnClickListener(v -> AppUtil.goCustomerService(getContext()));
        binding.ivwClose.setOnClickListener(view -> {
            binding.llNotice.setVisibility(View.GONE);
            binding.ivwNotice.setVisibility(View.VISIBLE);
        });
        binding.ivwNotice.setOnClickListener(view -> {
            binding.llNotice.setVisibility(View.VISIBLE);
            binding.ivwNotice.setVisibility(View.INVISIBLE);
        });
        binding.llNotice.setOnClickListener(view -> {
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG);
        });

        //cl_login_not
        binding.clLoginNot.setOnClickListener(view -> {
            // 登录
            KLog.i("**************");
            //binding.btnLogin.setVisibility(View.VISIBLE);
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
        });
        binding.llMenu.setOnClickListener(v -> {
            // 未登录时,点击登录右边的4个菜单
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
        });
        binding.tvwDebug.setOnClickListener(v -> {
            if (clickCount++ > 5) {
                clickCount = 0;
                startContainerFragment(RouterFragmentPath.Home.PG_DEBUG);
            }
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
           /* String title = getString(R.string.txt_withdraw);
            String url = DomainUtil.getDomain2() + Constant.URL_WITHDRAW;
            BrowserActivity.start(getContext(), title, url, true);*/
            // 限制多次点击
            if (ClickUtil.isFastClick()) {
                return;
            }
            showChoose();

        });
        binding.tvwTrans.setOnClickListener(view -> {
            // 转账
            KLog.i("**************");
            startContainerFragment(RouterFragmentPath.Wallet.PAGER_TRANSFER);
        });
        binding.tvwMember.setOnClickListener(view -> {
            // 会员
            KLog.i("**************");
            //startContainerFragment(RouterFragmentPath.Mine.PAGER_VIP_UPGRADE);
            BrowserActivity.start(getContext(), getString(R.string.txt_vip_center), DomainUtil.getDomain2() + Constant.URL_VIP_CENTER, true, false, true);
            //new XPopup.Builder(getContext()).asCustom(new BrowserDialog(getContext(), title, url, true)).show();
        });

        GameAdapter.ICallBack mCallBack = new GameAdapter.ICallBack() {
            @Override
            public void onClick(GameVo vo) {
                if (ClickUtil.isFastClick()) {
                    return;
                }
                CfLog.i(vo.toString());
                if (vo.cid == 7) {
                    startContainerFragment(RouterFragmentPath.Home.AUG);
                    return;
                }
                if (vo.cid == 19 || vo.cid == 34 || vo.cid == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("vo", vo);
                    startContainerFragment(RouterFragmentPath.Home.ELE, bundle);
                    return;
                }
                viewModel.getPlayUrl(vo.alias, vo.gameId, vo.name);
            }

            @Override
            public void getToken(GameVo vo) {
                if (ClickUtil.isFastClick()) {
                    return;
                }
                if (TextUtils.equals(vo.alias, PLATFORM_FBXC)) {
                    viewModel.getFBXCGameTokenApi();
                } else if (TextUtils.equals(vo.alias, PLATFORM_FB)) {
                    viewModel.getFBGameTokenApi();
                } else {
                    viewModel.getPMGameTokenApi();
                }
            }

        };

        gameAdapter = new GameAdapter(getContext(), mCallBack);
        binding.rcvList.setAdapter(gameAdapter);
        manager = new LinearLayoutManager(getContext());
        binding.rcvList.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_home));
        binding.rcvList.addItemDecoration(decoration);

        binding.rcvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当他划到找到要的item，将此item移到最上方
                if (needScroll && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    scrollRecycleView();
                }
                // 当他滑动时才让radioButton可以控制
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isSelectedGame = false;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int position = manager.findLastCompletelyVisibleItemPosition();
                if (position < 0 || position > gameAdapter.size() - 1) {
                    return;
                }
                GameVo vo = gameAdapter.get(position);
                if (vo.pId != curPId && !isSelectedGame) {
                    curPId = vo.pId;
                    RadioButton rbtn = binding.rgpType.findViewWithTag("tp_" + curPId);
                    gameGroup = curPId - 1;
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
                isSelectedGame = true;
                String tag = v.getTag().toString();
                int pid = Integer.parseInt(tag.replace("tp_", ""));
                gameGroup = pid - 1;
                smoothToPosition(pid);
            });
        }

    }

    private String getString(String result) {
        String url = "";
        //添加測試id：198 上線後刪除
        if (result.equals("135")) {
            url = DomainUtil.getDomain2() + "/webapp/#/turntable/135";
        } else if (result.equals("173")) {
            url = DomainUtil.getDomain2() + "/webapp/#/newactivity/64/1?aid=173";
        } else if (result.equals("174")) {
            url = DomainUtil.getDomain2() + "/webapp/#/newactivity/64/5?aid=174";
            //} else if (result.equals("198")) {
            //    url = DomainUtil.getDomain2() + "#/newactivity/64/5?aid=198";
        } else {
            url = DomainUtil.getDomain2() + Constant.URL_ACTIVITY + result;
        }
        return url;
    }

    private void setViewClickable(boolean isClickable) {
        binding.tvwDeposit.setClickable(isClickable);
        binding.tvwWithdraw.setClickable(isClickable);
        binding.tvwTrans.setClickable(isClickable);
        binding.tvwMember.setClickable(isClickable);
        binding.llMenu.setClickable(!isClickable); // 设置相反的状态
    }

    private void smoothToPosition(int pid) {
        List<GameVo> list = gameAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).pId == pid) {
                smoothToPositionTop(i);
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

    /**
     * 显示提款页面
     */
    private void showChoose() {

        if (mProfileVo == null) {
            return;
        }
     /*   魔域提现 没有限制绑定手机 邮箱 if (!mProfileVo.is_binding_phone && !mProfileVo.is_binding_email) {
            CfLog.i("未绑定手机/邮箱");
            toBindPhoneNumber();
        }*/
        else if (mProfileVo.has_securitypwd) {
            //金额大于0 才可以跳转提款页面
            if (Double.valueOf(mProfileVo.availablebalance) > 0) {
                ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_CHOOSE_WITHDRAW).navigation();
            } else {
                ToastUtils.showError(getContext().getString(R.string.txt_withdraw_balance_money));
            }
        } else {
            //跳转设定资金密码设定页面
            startContainerFragment(RouterFragmentPath.Mine.PAGER_FUNDS_PWD);
        }
    }

    private void toBindPhoneOrCard() {
        String msg = getString(R.string.txt_rc_bind_personal_info);
        String left = getString(R.string.txt_rc_bind_phone_now);
        String right = getString(R.string.txt_rc_bind_bank_card_now);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                toBindPhoneNumber();
                ppw.dismiss();
            }

            @Override
            public void onClickRight() {
                toBindCard();
                ppw.dismiss();
            }
        });
        ppw = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog);
        ppw.show();
    }

    private void toBindPhoneNumber() {

        String msg = getString(R.string.txt_rc_bind_phone_email_pls);
        String left = getString(R.string.txt_rc_bind_phone);
        String right = getString(R.string.txt_rc_bind_email);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, left, right, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
                toBindPhoneOrEmail(Constant.BIND_PHONE);
                ppw2.dismiss();
            }

            @Override
            public void onClickRight() {
                toBindPhoneOrEmail(Constant.BIND_EMAIL);
                ppw2.dismiss();
            }
        });
        ppw2 = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog);
        ppw2.show();
    }

    private void toBindCard() {

        String msg = getString(R.string.txt_rc_bind_bank_card_pls);
        MsgDialog dialog = new MsgDialog(getContext(), null, msg, true, new MsgDialog.ICallBack() {
            @Override
            public void onClickLeft() {
            }

            @Override
            public void onClickRight() {
                isBinding = true;
                Bundle bundle = new Bundle();
                bundle.putString("type", "bindcard");
                startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY_CHOOSE, bundle);
                ppw2.dismiss();
            }
        });
        ppw2 = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asCustom(dialog);
        ppw2.show();

    }

    private void toBindPhoneOrEmail(String type) {
        isBinding = true;
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        startContainerFragment(RouterFragmentPath.Mine.PAGER_SECURITY_VERIFY, bundle);
    }

    private void checkUpdate() {
        int intervalTime = SPUtils.getInstance().getInt(SPKeyGlobal.APP_INTERVAL_TIME, 30);
        long lastCheckTime = SPUtils.getInstance().getLong(SPKeyGlobal.APP_LAST_CHECK_TIME, 0);
        if (System.currentTimeMillis() - lastCheckTime >= (intervalTime * 60 * 1000)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("platform", "android");
            map.put("platform_set", getResources().getString(R.string.platform_set));
            viewModel.getUpdate(map);
        }
    }

    private void checkRedPocket() {
        viewModel.getRedPocket();
    }

    /**
     * 显示更新
     *
     * @param isWeakUpdate 是否弱更 true:是弱更 false:强更
     * @param vo           UpdateVo
     */
    private void showUpdate(final boolean isWeakUpdate, final AppUpdateVo vo) {
        if (updateView != null && updateView.isShow()) {
            return;
        }
        if (vo.download_url.contains(".apk")){
            AppUpdateDialog dialog = new AppUpdateDialog(getContext(), isWeakUpdate, vo, new AppUpdateDialog.IAppUpdateCallBack() {
                @Override
                public void onUpdateCancel() {
                    updateView.dismiss();
                }

                @Override
                public void onUpdateForce() {
                }

            });

            updateView = new XPopup.Builder(getContext())
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(dialog);
            updateView.show();
        }

    }

    private void smoothToPositionTop(int position) {
        this.position = position;
        //获取第一个和最后一个可见项
        int firstItemPosition = manager.findFirstVisibleItemPosition();
        int lastItemPosition = manager.findLastVisibleItemPosition();
        if (position < firstItemPosition) {
            // 第一种可能:跳转位置在第一个可见位置之前
            binding.rcvList.smoothScrollToPosition(position);
        } else if (position <= lastItemPosition) {
            // 第二种可能:跳转位置在第一个可见位置之后
            binding.rcvList.smoothScrollBy(0, binding.rcvList.getChildAt(position - firstItemPosition).getTop());
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            binding.rcvList.smoothScrollToPosition(position);
            needScroll = true;
        }
    }

    private void scrollRecycleView() {
        needScroll = false;
        int diff = position - manager.findFirstVisibleItemPosition();
        if (diff >= 0 && diff < binding.rcvList.getChildCount()) {
            binding.rcvList.smoothScrollBy(0, binding.rcvList.getChildAt(diff).getTop());
        }
    }

}
