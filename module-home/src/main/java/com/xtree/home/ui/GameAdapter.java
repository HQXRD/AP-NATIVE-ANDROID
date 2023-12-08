package com.xtree.home.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.home.R;
import com.xtree.home.databinding.HmItemGameBinding;
import com.xtree.home.vo.GameVo;

import me.xtree.mvvmhabit.utils.SPUtils;

public class GameAdapter extends CachedAutoRefreshAdapter<GameVo> {
    Context ctx;
    HmItemGameBinding binding;

    ICallBack mCallBack;

    public interface ICallBack {
        void onClick(String gameAlias, String gameId);
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
    }

    private void jump(GameVo vo) {
        CfLog.d(vo.toString());
        if (vo.status != 1) {
            // 0是维护, 1是正常, 2是下架
            return;
        }

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
            return;
        }

        if (vo.isH5) {
            if (TextUtils.isEmpty(vo.playURL)) {
                // 去请求网络接口
                CfLog.d("request api...");
                mCallBack.onClick(vo.alias, vo.gameId);
            } else {
                // 拼装URL
                playGame(DomainUtil.getDomain() + vo.playURL);
            }

        } else {
            // 跳原生
            CfLog.d("跳原生");
            ARouter.getInstance().build(RouterActivityPath.Bet.PAGER_BET_HOME).navigation();
        }
    }

    public void playGame(String playUrl) {
        CfLog.i("URL: " + playUrl);
        Intent it = new Intent(ctx, BrowserActivity.class);
        it.putExtra("url", playUrl);
        ctx.startActivity(it);
    }

}
