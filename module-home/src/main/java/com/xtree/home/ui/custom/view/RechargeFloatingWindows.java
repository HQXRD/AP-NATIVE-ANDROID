package com.xtree.home.ui.custom.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.xtree.base.widget.FloatingWindows;
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

public class RechargeFloatingWindows extends FloatingWindows {
    private CompositeDisposable mCompositeDisposable;
    private boolean isSearch = true;
    RechargeReportAdapter rechargeReportAdapter;
    HomeApiService apiService = RetrofitClient.getInstance().create(HomeApiService.class);
    HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);

    public RechargeFloatingWindows(Context context) {
        super(context);
        mCompositeDisposable = new CompositeDisposable();
        onCreate(R.layout.floating_recharge);
    }

    @Override
    public void initData() {
        setStartLocation();

        RecyclerView rcvData = secondaryLayout.findViewById(R.id.rcv_data);
        rechargeReportAdapter = new RechargeReportAdapter(mContext, vo -> {
            secondaryLayout.findViewById(R.id.cl_floating_window).setVisibility(View.GONE);
            llLine.setVisibility(View.GONE);
            if (vo.orderurl.isEmpty()) {
                //new XPopup.Builder(ctx).asCustom(new BrowserDialog(ctx, vo.payport_nickname, DomainUtil.getDomain2()
                // + "/webapp/#/depositetail/" + vo.id)).show();
                goOrderDetail(vo.id);
            } else {
                new XPopup.Builder(mContext).asCustom(new BrowserDialog(mContext, vo.payport_nickname, vo.orderurl)).show();
            }
        });
        rcvData.setAdapter(rechargeReportAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rcvData.setLayoutManager(manager);

        secondaryLayout.findViewById(R.id.ivw_close).setOnClickListener(v -> {
            secondaryLayout.findViewById(R.id.cl_floating_window).setVisibility(View.GONE);
            llLine.setVisibility(View.GONE);
        });

        secondaryLayout.findViewById(R.id.tvw_record).setOnClickListener(v -> {
            if (ClickUtil.isFastClick()) {
                return;
            }

            Intent intent = new Intent(mContext, ContainerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ContainerActivity.ROUTER_PATH, RouterFragmentPath.Mine.PAGER_RECHARGE_WITHDRAW);
            mContext.startActivity(intent);
            secondaryLayout.findViewById(R.id.cl_floating_window).setVisibility(View.GONE);
            llLine.setVisibility(View.GONE);
        });

        mainLayout.setOnClickListener(v -> {
            secondaryLayout.findViewById(R.id.cl_floating_window).setVisibility(View.VISIBLE);
            llLine.setVisibility(View.VISIBLE);
            if (!isSearch) {
                CfLog.i("search the data");
                getReportData();
                isSearch = true;
            }
        });

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
                                secondaryLayout.findViewById(R.id.cl_floating_content).setVisibility(View.VISIBLE);
                                secondaryLayout.findViewById(R.id.tvw_no_data).setVisibility(View.GONE);
                            } else {
                                rechargeReportAdapter.clear();
                                secondaryLayout.findViewById(R.id.tvw_no_data).setVisibility(View.VISIBLE);
                                secondaryLayout.findViewById(R.id.cl_floating_content).setVisibility(View.GONE);
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
