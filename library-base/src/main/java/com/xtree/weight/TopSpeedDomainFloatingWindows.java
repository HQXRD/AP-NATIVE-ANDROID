package com.xtree.weight;

import static android.content.Context.WINDOW_SERVICE;
import static com.xtree.base.net.fastest.FastestConfigKt.FASTEST_GOURP_NAME;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.drake.net.Net;
import com.xtree.base.R;
import com.xtree.base.adapter.MainDomainAdapter;
import com.xtree.base.databinding.MainLayoutTopSpeedDomainBinding;
import com.xtree.base.net.fastest.ChangeH5LineUtil;
import com.xtree.base.net.fastest.FastestTopDomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.TopSpeedDomain;
import com.xtree.base.widget.FloatingWindows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.rxjava3.functions.Consumer;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;
import me.xtree.mvvmhabit.utils.Utils;

public class TopSpeedDomainFloatingWindows extends FloatingWindows {
    MainLayoutTopSpeedDomainBinding mBinding;
    MainDomainAdapter mainDomainAdapter;
    public TopSpeedDomainFloatingWindows(Context context) {
        super(context);
        onCreate(R.layout.main_layout_top_speed_domain);
        setIcon(R.mipmap.main_icon_shadow);
    }

    @Override
    protected void initView(int layout) {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        setPosition(displayMetrics.widthPixels / 2 - ConvertUtils.dp2px(30), displayMetrics.heightPixels / 2 + ConvertUtils.dp2px(50));
        super.initView(layout);
    }

    @Override
    public void initData() {
        setStartLocation();
        mBinding = MainLayoutTopSpeedDomainBinding.bind(secondaryLayout.getRootView());
        secondaryLayout.setVisibility(View.GONE);
        mBinding.rvAgent.setLayoutManager(new LinearLayoutManager(mContext));
        floatView.setOnClickListener(v -> {

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

                //每次測速也測一下H5
                ChangeH5LineUtil.getInstance().start();
            }
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
                ChangeH5LineUtil.getInstance().start();
            } else {
                ToastUtils.show("测速过于频繁，请稍后再试!", Toast.LENGTH_SHORT, 0);
            }
        });
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
