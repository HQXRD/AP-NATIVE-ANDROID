package com.xtree.home.ui.custom.view;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.RetrofitClient;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.widget.BrowserDialog;
import com.xtree.home.R;
import com.xtree.home.data.source.HomeApiService;
import com.xtree.home.data.source.HttpDataSource;
import com.xtree.home.data.source.http.HttpDataSourceImpl;
import com.xtree.home.ui.adapter.RechargeReportAdapter;
import com.xtree.home.vo.RechargeOrderVo;
import com.xtree.home.vo.RechargeReportVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public class CustomFloatWindows extends RelativeLayout {
    private Context ctx;
    private WindowManager windowManager;
    private View floatView;
    private CompositeDisposable mCompositeDisposable;
    private boolean isSearch = true;
    private boolean isShow = false;
    RechargeReportAdapter rechargeReportAdapter;
    HomeApiService apiService = RetrofitClient.getInstance().create(HomeApiService.class);
    HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
    WindowManager.LayoutParams floatLp;

    public CustomFloatWindows(Context context) {
        super(context);
        ctx = context;

        mCompositeDisposable = new CompositeDisposable();

        initView();

        initData();
    }

    public void removeView() {
        CfLog.e("Close floatView start");
        if (windowManager != null && floatView != null && isShow) {
            CfLog.e("Close floatView");
            windowManager.removeView(floatView);
            isShow = false;
        }
    }

    public void show() {
        if (!isShow) {
            windowManager.addView(floatView, floatLp);
            isShow = true;
        }
    }

    private void initView() {
        windowManager = (WindowManager) ctx.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        floatView = inflater.inflate(R.layout.floating_icon, null);

        RecyclerView rcvData = floatView.findViewById(R.id.rcv_data);
        rechargeReportAdapter = new RechargeReportAdapter(ctx, vo -> {
            floatView.findViewById(R.id.cl_floating_window).setVisibility(View.GONE);
            floatView.findViewById(R.id.ll_line).setVisibility(View.GONE);
            if (vo.orderurl.isEmpty()) {
                //new XPopup.Builder(ctx).asCustom(new BrowserDialog(ctx, vo.payport_nickname, DomainUtil.getDomain2()
                // + "/webapp/#/depositetail/" + vo.id)).show();
                goOrderDetail(vo.id);
            } else {
                new XPopup.Builder(ctx).asCustom(new BrowserDialog(ctx, vo.payport_nickname, vo.orderurl)).show();
            }
        });
        rcvData.setAdapter(rechargeReportAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(ctx);
        rcvData.setLayoutManager(manager);

        floatLp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        floatLp.gravity = Gravity.TOP;
        floatLp.x = displayMetrics.widthPixels / 2 - 100;
        floatLp.y = displayMetrics.heightPixels / 2 + 100;

        floatView.findViewById(R.id.ivw_close).setOnClickListener(v -> {
            floatView.findViewById(R.id.cl_floating_window).setVisibility(View.GONE);
            floatView.findViewById(R.id.ll_line).setVisibility(View.GONE);
        });

        floatView.findViewById(R.id.tvw_record).setOnClickListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }

            Intent intent = new Intent(ctx, ContainerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_RECHARGE_WITHDRAW);
            ctx.startActivity(intent);
            floatView.findViewById(R.id.cl_floating_window).setVisibility(View.GONE);
            floatView.findViewById(R.id.ll_line).setVisibility(View.GONE);
        });

        floatView.findViewById(R.id.ivw_icon).setOnTouchListener(new View.OnTouchListener() {
            final WindowManager.LayoutParams floatWindowLayoutUpdateParam = floatLp;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = floatWindowLayoutUpdateParam.x;
                        y = floatWindowLayoutUpdateParam.y;
                        px = event.getRawX();
                        py = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        floatWindowLayoutUpdateParam.x = (int) ((x + event.getRawX()) - px);
                        floatWindowLayoutUpdateParam.y = (int) ((y + event.getRawY()) - py);
                        windowManager.updateViewLayout(floatView, floatWindowLayoutUpdateParam);
                        break;
                }
                return false;
            }
        });

        floatView.findViewById(R.id.ivw_icon).setOnClickListener(v -> {
            floatView.findViewById(R.id.cl_floating_window).setVisibility(View.VISIBLE);
            floatView.findViewById(R.id.ll_line).setVisibility(View.VISIBLE);
            if (!isSearch) {
                CfLog.i("search the data");
                getReportData();
                isSearch = true;
            }
        });
    }

    private void initData() {
        getReportData();
    }

    private void goOrderDetail(String id) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowBack", true);
        bundle.putBoolean("isShowOrderDetail", true);
        bundle.putString("orderDetailId", id);
        Intent intent = new Intent(getContext(), ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Recharge.PAGER_RECHARGE);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        getContext().startActivity(intent);
    }

    private void getReportData() {
        new Thread(() -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("userid", SPUtils.getInstance().getString(SPKeyGlobal.USER_ID));
            map.put("p", "1");
            map.put("page_size", "30");
            map.put("recharge_json", "true");
            map.put("client", "m");

            Disposable disposable = (Disposable) httpDataSource.getApiService().getRechargeReport(map)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribeWith(new HttpCallBack<RechargeReportVo>() {
                        @Override
                        public void onResult(RechargeReportVo vo) {
                            CfLog.d("******");
                            isSearch = false;
                            List<RechargeOrderVo> rechargeOrderVoList = new ArrayList<>();
                            if (vo.result != null) {
                                for (RechargeOrderVo rechargeOrderVo : vo.result) {
                                    if (rechargeOrderVo.status.equals("0") && !rechargeOrderVo.recharge_json_exporetime.equals("-1")) {
                                        rechargeOrderVoList.add(rechargeOrderVo);
                                    }
                                }
                            }

                            if (SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN).equals("")) {
                                rechargeReportAdapter.clear();
                            }

                            if (rechargeOrderVoList.size() > 0) {
                                rechargeReportAdapter.clear();
                                rechargeReportAdapter.addAll(rechargeOrderVoList);
                                floatView.findViewById(R.id.rcv_data).setVisibility(View.VISIBLE);
                                floatView.findViewById(R.id.tvw_no_data).setVisibility(View.GONE);
                            } else {
                                rechargeReportAdapter.clear();
                                floatView.findViewById(R.id.tvw_no_data).setVisibility(View.VISIBLE);
                                floatView.findViewById(R.id.rcv_data).setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            CfLog.e("error, " + t.toString());
                            super.onError(t);
                        }
                    });
            mCompositeDisposable.add(disposable);
        }).start();
    }
}
