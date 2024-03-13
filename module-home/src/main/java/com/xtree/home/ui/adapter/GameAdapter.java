package com.xtree.home.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.home.BuildConfig;
import com.xtree.home.R;
import com.xtree.home.databinding.HmItemGameBinding;
import com.xtree.home.vo.GameVo;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class GameAdapter extends CachedAutoRefreshAdapter<GameVo> {
    Context ctx;
    HmItemGameBinding binding;

    ICallBack mCallBack;

    public final static String PLATFORM_FBXC = "fbxc";
    public final static String PLATFORM_FB = "fb";
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

        if (vo.twoImage) {
            binding.ivwSplit.setVisibility(View.INVISIBLE);
            binding.ivwCoverLeft.setVisibility(View.VISIBLE);
            binding.ivwCoverRight.setVisibility(View.VISIBLE);
            binding.ivwCoverLeft.setOnClickListener(view -> jump(vo, true));
            binding.ivwCoverRight.setOnClickListener(view -> jump(vo, false));
        } else {
            binding.ivwSplit.setVisibility(View.GONE);
            binding.ivwCoverLeft.setVisibility(View.GONE);
            binding.ivwCoverRight.setVisibility(View.GONE);
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
            if (isLeft) {
                goApp(vo);
            } else {
                goWeb(vo);
            }
            return;
        }

        if (vo.isH5) {
            goWeb(vo);
        } else {
            // 跳原生
            CfLog.d("跳原生");
            goApp(vo);
        }
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

    private void goApp(GameVo vo) {
        String cgToken;
        if (TextUtils.equals(vo.alias, PLATFORM_FBXC)) {
            cgToken = SPUtils.getInstance().getString(SPKeyGlobal.FBXC_TOKEN);
        } else if (TextUtils.equals(vo.alias, PLATFORM_FB)) {
            cgToken = SPUtils.getInstance().getString(SPKeyGlobal.FB_TOKEN);
        } else {
            cgToken = SPUtils.getInstance().getString(SPKeyGlobal.PM_TOKEN);
        }

        if (TextUtils.isEmpty(cgToken)) {
            ToastUtils.showShort("场馆初始化中，请稍候...");
        } else {
            ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).withString("KEY_PLATFORM", vo.alias).navigation();
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
