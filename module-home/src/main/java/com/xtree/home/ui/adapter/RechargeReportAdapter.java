package com.xtree.home.ui.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.base.utils.CfLog;
import com.xtree.base.vo.RechargeOrderVo;
import com.xtree.home.R;

public class RechargeReportAdapter extends CachedAutoRefreshAdapter<RechargeOrderVo> {
    Context ctx;
    ICallBack callBack;

    public RechargeReportAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public RechargeReportAdapter(Context ctx, ICallBack callBack) {
        this.ctx = ctx;
        this.callBack = callBack;
    }

    public interface ICallBack {
        void onCallBack(RechargeOrderVo vo);
    }

    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CacheViewHolder holder = new CacheViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_recharge_service, parent, false));
        return holder;
    }

    @Override
    public void onViewRecycled(@NonNull CacheViewHolder holder) {
        super.onViewRecycled(holder);
        // 在項目被回收時，停止或重置倒數計時器
        CfLog.i("holder.countDownTimer check");
        if (holder.countDownTimer != null) {
            CfLog.i("holder.countDownTimer is cancel");
            holder.countDownTimer.cancel();
            holder.countDownTimer = null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {
        RechargeOrderVo vo = getData().get(position);

        TextView itemMoney = holder.itemView.findViewById(R.id.item_money);
        TextView itemWay = holder.itemView.findViewById(R.id.item_way);
        TextView itemTime = holder.itemView.findViewById(R.id.item_time);

        itemTime.setTextColor(ctx.getResources().getColor(R.color.black));
        itemMoney.setText(vo.money);
        itemWay.setText(vo.payport_nickname);

        if (holder.countDownTimer != null) {
            holder.countDownTimer.cancel();
        }

        holder.countDownTimer = new CountDownTimer(Integer.parseInt(vo.recharge_json_exporetime) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String message = (String.valueOf(seconds / 60).length() == 2 ? String.valueOf((seconds / 60)) : "0" + (seconds / 60)) + ":"
                        + (String.valueOf(seconds % 60).length() == 2 ? (seconds % 60) : "0" + (seconds % 60));

                //CfLog.d(message);
                itemTime.setText(message);
            }

            @Override
            public void onFinish() {
                itemTime.setTextColor(ctx.getResources().getColor(R.color.red));
                itemTime.setText("支付超时");
            }
        };

        holder.countDownTimer.start();

        if (position % 2 == 1) {
            itemMoney.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white, ctx.getResources().newTheme()));
            itemWay.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white, ctx.getResources().newTheme()));
            itemTime.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_white, ctx.getResources().newTheme()));
        } else {
            itemMoney.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple, ctx.getResources().newTheme()));
            itemWay.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple, ctx.getResources().newTheme()));
            itemTime.setBackground(ctx.getResources().getDrawable(R.drawable.bg_floating_data_purple, ctx.getResources().newTheme()));
        }

        itemWay.setOnClickListener(v -> {
            callBack.onCallBack(vo);
        });
    }
}