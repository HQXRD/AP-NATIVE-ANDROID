package com.xtree.weight;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;
import static com.xtree.base.net.fastest.FastestConfigKt.FASTEST_GOURP_NAME;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drake.net.Net;
import com.xtree.base.R;
import com.xtree.base.adapter.MainDomainAdapter;
import com.xtree.base.databinding.MainLayoutTopSpeedDomainBinding;
import com.xtree.base.net.fastest.FastestTopDomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.TopSpeedDomain;
import com.xtree.base.widget.FloatingWindows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseApplication;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class TopSpeedDomainFloatingWindows extends FloatingWindows {
    MainLayoutTopSpeedDomainBinding mBinding;
    MainDomainAdapter mainDomainAdapter;

    private View iconView;
    private Disposable tipDisposable;
    /**
     * 监听最快线路
     */
    private final Observer<TopSpeedDomain> fastestDomainObserver = new Observer<TopSpeedDomain>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(TopSpeedDomain topSpeedDomain) {

            if (iconView == null) {
                initIconView();
            }

            if (topSpeedDomain != null && iconView != null) {

                cancle();

                TextView fastestStatusTime = iconView.findViewById(R.id.fastest_status_time);
                ImageView fastestStatusImg = iconView.findViewById(R.id.fastest_status_img);
                fastestStatusTime.setText(topSpeedDomain.speedSec + "ms");

                if (topSpeedDomain.speedSec <= 500) {
                    fastestStatusTime.setTextColor(mContext.getResources().getColor(R.color.clr_txt_fastest_low));
                    fastestStatusImg.setImageResource(R.mipmap.icon_fastest_status_low);
                } else if (topSpeedDomain.speedSec < 1000) {
                    fastestStatusTime.setTextColor(mContext.getResources().getColor(R.color.clr_txt_fastest_medium));
                    fastestStatusImg.setImageResource(R.mipmap.icon_fastest_status_medium);
                } else {
                    fastestStatusTime.setTextColor(mContext.getResources().getColor(R.color.clr_txt_fastest_high));
                    fastestStatusImg.setImageResource(R.mipmap.icon_fastest_status_high);

                    tipDisposable = Observable.interval(30, TimeUnit.MINUTES)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    tick -> {
                                        if (secondaryLayout != null && secondaryLayout.getVisibility() == GONE) {
                                            showTip();
                                        }
                                    },
                                    Throwable::printStackTrace
                            );
                }
                ivwIcon.setImageBitmap(getBitmapFromView(iconView));
            }
        }
    };

    private final Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

        private int activityReferences = 0;
        private boolean isActivityChangingConfigurations = false;
        private boolean isAppInBackground = true;

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            if (activityReferences == 0 && isAppInBackground) {
                // 应用从后台回到前台
                isAppInBackground = false;
                if (FastestTopDomainUtil.Companion.getFastestDomain().getValue() != null) {
                    FastestTopDomainUtil.Companion.getFastestDomain().setValue(FastestTopDomainUtil.Companion.getFastestDomain().getValue());
                }
            }
            activityReferences++;
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            activityReferences--;
            if (activityReferences == 0 && !isActivityChangingConfigurations) {
                // 应用进入后台
                isAppInBackground = true;
                cancle();
            }
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    };
    private View tipView;


    public TopSpeedDomainFloatingWindows(Context context) {
        super(context);
        onCreate(R.layout.main_layout_top_speed_domain);
        setIcon(0);
    }

    @Override
    protected void initView(int layout) {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        setPosition(displayMetrics.widthPixels / 2 - ConvertUtils.dp2px(30), displayMetrics.heightPixels / 2 + ConvertUtils.dp2px(50));
        super.initView(layout);

        llLine.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        llLine.setLayoutParams(layoutParams);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);
        constraintSet.connect(R.id.ll_line, ConstraintSet.END, R.id.ivw_icon, ConstraintSet.START);
        constraintSet.setVerticalBias(R.id.ll_line, 1.0f);
        constraintSet.setHorizontalBias(R.id.ll_line, 1.0f);
        constraintSet.applyTo(mainLayout);
    }

    @Override
    public void initData() {
        setStartLocation();
        mBinding = MainLayoutTopSpeedDomainBinding.bind(secondaryLayout.getRootView());
        secondaryLayout.setVisibility(View.GONE);
        mBinding.rvAgent.setLayoutManager(new LinearLayoutManager(mContext));
        floatView.setOnClickListener(v -> {
            testDomain();
        });
        mBinding.tvSpeedCheck.setOnClickListener(v -> {

            if (FastestTopDomainUtil.Companion.getMIsFinish()) {

                TagUtils.tagEvent(
                        Utils.getContext(),
                        TagUtils.EVENT_FASTEST,
                        TagUtils.KEY_FASTEST_RESTART
                );

                List<TopSpeedDomain> datas = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    datas.add(new TopSpeedDomain());
                }
                mainDomainAdapter.setChecking(true);
                mainDomainAdapter.setNewData(datas);
                FastestTopDomainUtil.getInstance().start();
            } else {
                ToastUtils.show("测速过于频繁，请稍后再试!", Toast.LENGTH_SHORT, 0);
            }
        });


        FastestTopDomainUtil.Companion.getFastestDomain().observeForever(fastestDomainObserver);

        BaseApplication.getInstance().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    private void initIconView() {
        if (mContext == null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        iconView = inflater.inflate(R.layout.layout_fastest_status, null);
    }

    private void testDomain() {

        //如果有提示则取消
        llLine.removeView(tipView);

        Net.INSTANCE.cancelGroup(FASTEST_GOURP_NAME);
        FastestTopDomainUtil.Companion.setMIsFinish(true);

        if(secondaryLayout.getVisibility() == VISIBLE){
            secondaryLayout.setVisibility(GONE);
        }else {
            secondaryLayout.setVisibility(VISIBLE);
            List<TopSpeedDomain> datas = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                datas.add(new TopSpeedDomain());
            }
            if(mainDomainAdapter == null) {
                mainDomainAdapter = new MainDomainAdapter(mContext, datas);
                mainDomainAdapter.setChecking(true);
                mainDomainAdapter.setCallBack(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        //切换线路成功回调
                        secondaryLayout.setVisibility(GONE);
                    }
                });

                mBinding.rvAgent.setAdapter(mainDomainAdapter);
            }else {
                mainDomainAdapter.setChecking(true);
                mainDomainAdapter.setNewData(datas);
            }
            FastestTopDomainUtil.getInstance().start();
        }
    }

    @Override
    public void removeView() {
        super.removeView();
        FastestTopDomainUtil.Companion.getFastestDomain().removeObserver(fastestDomainObserver);
        BaseApplication.getInstance().unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        cancle();
        if (iconView != null) {
            iconView = null;
        }
        if (tipView != null) {
            tipView = null;
        }
    }

    /**
     * 当前线路耗时长，提醒用户
     */
    private void showTip() {

        if (tipView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            tipView = inflater.inflate(R.layout.layout_fastest_tip, null);
        }

        llLine.removeView(tipView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        tipView.findViewById(R.id.fastest_tip_cancle).setOnClickListener(v-> {
            llLine.removeView(tipView);
        });
        tipView.findViewById(R.id.fastest_tip_change).setOnClickListener(v-> {
            llLine.removeView(tipView);
            testDomain();
        });
        tipView.setOnClickListener(v-> {
        });

        llLine.addView(tipView, params);
    }

    private void cancle() {
        // 取消订阅
        if (tipDisposable != null && !tipDisposable.isDisposed()) {
            tipDisposable.dispose();
        }
    }

    /**
     * 将 View 转换为 Bitmap
     */
    private Bitmap getBitmapFromView(View view) {
        // 测量并布局视图
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        // 创建 Bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // 绘制视图到 Canvas
        view.draw(canvas);

        return bitmap;
    }

    public void refresh() {
        if(mainDomainAdapter != null) {
            mainDomainAdapter.setChecking(false);

            CopyOnWriteArrayList<TopSpeedDomain> topSpeedDomain = new CopyOnWriteArrayList<>(FastestTopDomainUtil.getInstance().getTopSpeedDomain());

            if (topSpeedDomain.isEmpty()) {
                onError();
                return;
            }

            List<TopSpeedDomain> datas = mainDomainAdapter.getDatas();
            int oldSize = datas.size();

            for (int i = 0; i < topSpeedDomain.size(); i++) {
                TopSpeedDomain newData = topSpeedDomain.get(i);

                if (oldSize >= (i + 1)) {
                    TopSpeedDomain oldData = datas.get(i);
                    oldData.url = newData.url;
                    oldData.speedSec = newData.speedSec;
                    mainDomainAdapter.notifyItemChanged(i);
                } else {
                    datas.add(newData);
                    mainDomainAdapter.notifyItemInserted(datas.size() - 1);
                }
            }
        }
    }

    public void onError() {
        if(mainDomainAdapter != null) {
            mainDomainAdapter.setChecking(false);
            mainDomainAdapter.setFailed(true);
            mainDomainAdapter.notifyDataSetChanged();
        }
    }
}
