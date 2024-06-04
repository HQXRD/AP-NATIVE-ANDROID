package com.xtree.home.ui.adapter;

import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.AppUtil;
import com.xtree.base.utils.BtDomainUtil;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.base.widget.TipGameDialog;
import com.xtree.home.BuildConfig;
import com.xtree.home.R;
import com.xtree.home.databinding.HmItemGameBinding;
import com.xtree.home.ui.custom.view.TipPMDialog;
import com.xtree.home.vo.GameVo;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class GameAdapter extends CachedAutoRefreshAdapter<GameVo> {
    Context ctx;
    HmItemGameBinding binding;

    ICallBack mCallBack;

    public final static String PLATFORM_FBXC = "fbxc";
    public final static String PLATFORM_FB = "fb";
    public final static String PLATFORM_PM = "obg";
    private BasePopupView basePopupView;

    public interface ICallBack {
        void onClick(GameVo vo); // String gameAlias, String gameId
    }

    public GameAdapter(Context ctx, ICallBack mCallBack) {
        this.ctx = ctx;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.hm_item_game, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        GameVo vo = get(position);
        binding = HmItemGameBinding.bind(holder.itemView);
        binding.ivwImg.setImageLevel(vo.typeId);
        binding.ivwImg.setOnClickListener(view -> jump(vo));

        if (vo.status == 0) {
            String txt = ctx.getString(R.string.hm_txt_maintaining, vo.maintenance_start, vo.maintenance_end);
            binding.tvwMaintenance.setText(txt);
            binding.tvwMaintenance.setVisibility(View.VISIBLE);
            binding.ivwGreyCover.setVisibility(View.VISIBLE);
        } else if (vo.status == 2) {
            binding.tvwMaintenance.setText("已下架");
            binding.tvwMaintenance.setVisibility(View.VISIBLE);
            binding.ivwGreyCover.setVisibility(View.VISIBLE);
        } else {
            binding.tvwMaintenance.setVisibility(View.GONE);
            binding.ivwGreyCover.setVisibility(View.GONE);
        }

        //if (vo.twoImage) {
        //    binding.layoutFc.setVisibility(View.VISIBLE);
        //    setFastCommon(position, vo);
        //} else {
        //    binding.layoutFc.setVisibility(View.GONE);
        //}

        CfLog.i(getData().size() + "  " + position);
        if ((getData().size() - 1) > position) {
            binding.rlSpace.setVisibility(View.GONE);
        } else {
            binding.rlSpace.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 极速版普通版切换
     */
    private void setFastCommon(int position, GameVo vo) {
        String key;
        if (vo.cid == 5) {
            //熊猫体育
            key = SPKeyGlobal.PM_FAST_COMMON;
        } else if (vo.cid == 26) {
            //FB体育
            key = SPKeyGlobal.FB_FAST_COMMON;
        } else {
            key = "";
        }
        boolean isFast = SPUtils.getInstance().getBoolean(key, true);
        if (isFast) {
            //如果是极速版
            binding.ivFc.setBackgroundResource(R.mipmap.hm_bt_fast);
            binding.ivwImg.setOnClickListener(view -> jump(vo, true));
            binding.ivLeft.setOnClickListener(null);
            binding.ivRight.setOnClickListener(view -> {
                CfLog.i("ivRight");
                SPUtils.getInstance().put(key, false);
                notifyItemChanged(position);
            });
        } else {
            //普通版
            binding.ivFc.setBackgroundResource(R.mipmap.hm_bt_common);
            binding.ivwImg.setOnClickListener(view -> jump(vo, false));
            binding.ivRight.setOnClickListener(null);
            binding.ivLeft.setOnClickListener(view -> {
                CfLog.i("ivLeft");
                SPUtils.getInstance().put(key, true);
                notifyItemChanged(position);
            });
        }
    }

    private void jump(GameVo vo) {
        jump(vo, true);
    }

    private void jump(GameVo vo, boolean isLeft) {
        CfLog.d(vo.toString());
        // 非正常状态 (且 非debug模式下 方便调试),不跳转
        if (vo.status != 1 && !BuildConfig.DEBUG) {
            // 0是维护, 1是正常, 2是下架
            return;
        }

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
            return;
        }
        // alias 为空时是 杏彩彩票, 为ag时分4个类型
        String gameId = vo.alias == null ? "xccp" : vo.alias.equals("ag") ? "ag_" + vo.id : vo.alias;
        TagUtils.tagEvent(ctx, "gm", gameId);

        if (vo.cid == 7 || vo.cid == 19 || vo.cid == 34 || (vo.cid == 1 && vo.cateId.equals("4"))) {
            mCallBack.onClick(vo);
            return;
        }

        if (vo.twoImage) {
            //熊猫场馆弹窗判断
            if (vo.alias.equals(PLATFORM_PM) && AppUtil.isTipToday(SPKeyGlobal.PM_NOT_TIP_TODAY)) {
                showPMDialog(vo, isLeft);
                return;
            }

            if (isLeft) {
                goApp(vo);
            } else {
                goWeb(vo);
            }
            return;
        }

        if (vo.isH5) {
            if (vo.cid == 3 && AppUtil.isTipToday(SPKeyGlobal.AG_NOT_TIP_TODAY)) {
                showTipDialog(SPKeyGlobal.AG_NOT_TIP_TODAY, "AG真人", vo);
                return;
            } else if (vo.cid == 31 && AppUtil.isTipToday(SPKeyGlobal.DB_NOT_TIP_TODAY)) {
                showTipDialog(SPKeyGlobal.DB_NOT_TIP_TODAY, "DB真人", vo);
                return;
            }
            goWeb(vo);
        } else {
            // 跳原生
            CfLog.d("跳原生");
            goApp(vo);
        }
    }

    private void showPMDialog(GameVo vo, boolean isLeft) {
        if (basePopupView != null && basePopupView.isShow()) {
            return;
        }
        //点击熊猫体育，弹出弹窗
        basePopupView = new XPopup.Builder(ctx)
                .dismissOnTouchOutside(false)
                .asCustom(new TipPMDialog(ctx, new TipPMDialog.ICallBack() {
                    @Override
                    public void onClickPM() {
                        //熊猫体育
                        if (isLeft) {
                            goApp(vo);
                        } else {
                            LoadingDialog.show(ctx);
                            goWeb(vo);
                        }
                        basePopupView.dismiss();

                    }

                    @Override
                    public void onClickFB() {
                        //杏彩体育
                        GameVo vo1 = new GameVo();
                        vo1.alias = PLATFORM_FBXC;
                        goApp(vo1);
                        basePopupView.dismiss();
                    }
                }));
        basePopupView.show();
    }

    /**
     * 当是AG真人或DB真人时弹出弹窗
     */
    private void showTipDialog(String key, String title, GameVo vo) {
        BasePopupView basePopupView = new XPopup.Builder(ctx)
                .dismissOnTouchOutside(false)
                .asCustom(new TipGameDialog(ctx, title, key, () -> goWeb(vo)));
        basePopupView.show();
    }

    private void goWeb(GameVo vo) {
        if (TextUtils.isEmpty(vo.playURL)) {
            // 去请求网络接口
            CfLog.d("request api...");
            mCallBack.onClick(vo); // vo.alias, vo.gameId
        } else {
            // 拼装URL
            if (vo.id.equals("601")) {
                playGame(DomainUtil.getDomain() + vo.playURL, vo.name, vo.id.equals("601"));
            } else {
                playGame(DomainUtil.getDomain() + vo.playURL, vo.name);
            }
        }
    }
    int time;
    private void goApp(GameVo vo) {
        String cgToken;
        if (TextUtils.equals(vo.alias, PLATFORM_FBXC)) {
            cgToken = SPUtils.getInstance().getString(SPKeyGlobal.FBXC_TOKEN);
        } else if (TextUtils.equals(vo.alias, PLATFORM_FB)) {
            cgToken = SPUtils.getInstance().getString(SPKeyGlobal.FB_TOKEN);
        } else if (TextUtils.equals(vo.alias, PLATFORM_PMXC)) {
            cgToken = SPUtils.getInstance().getString(SPKeyGlobal.PMXC_TOKEN);
        } else {
            cgToken = SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN);
        }

        if (TextUtils.isEmpty(cgToken) || !BtDomainUtil.hasDefaultLine(vo.alias)) {
            if(time < 3) {
                ToastUtils.showShort("场馆初始化中，请稍候...");
                time++;
            }else {
                ToastUtils.showShort("场馆初始化失败，即将重启应用，请保证手机网络通畅");
                binding.getRoot().postDelayed(() -> {
                    AppManager.getAppManager().AppExit();
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_SPLASH).
                            withString("KEY_PLATFORM", vo.alias).navigation();
                }, 2000);

            }
        } else {
            ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).
                    withString("KEY_PLATFORM", vo.alias).navigation();
        }
    }

    public void playGame(String playUrl, String title, Boolean isLottery) {
        CfLog.i("URL: " + playUrl);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra("url", playUrl);
        it.putExtra("isLottery", isLottery);
        it.putExtra("title", title);
        it.putExtra(BrowserActivity.ARG_IS_GAME, true);
        ctx.startActivity(it);
    }

    public void playGame(String playUrl, String title) {
        CfLog.i("URL: " + playUrl);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra("url", playUrl);
        it.putExtra("title", title);
        it.putExtra(BrowserActivity.ARG_IS_GAME, true);
        ctx.startActivity(it);
    }

}
