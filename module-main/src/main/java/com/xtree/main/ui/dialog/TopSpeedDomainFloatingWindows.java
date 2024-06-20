package com.xtree.main.ui.dialog;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.xtree.base.utils.FastestTopDomainUtil;
import com.xtree.base.widget.FloatingWindows;
import com.xtree.main.R;
import com.xtree.main.databinding.MainLayoutTopSpeedDomainBinding;
import com.xtree.main.ui.adapter.MainDomainAdapter;

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
        setPosition(displayMetrics.widthPixels / 2 - 100, displayMetrics.heightPixels / 2 + 300);
        super.initView(layout);
    }

    @Override
    public void initData() {
        setStartLocation();
        mBinding = MainLayoutTopSpeedDomainBinding.bind(secondaryLayout.getRootView());
        secondaryLayout.setVisibility(View.GONE);
        mBinding.rvAgent.setLayoutManager(new LinearLayoutManager(mContext));
        mainDomainAdapter = new MainDomainAdapter(mContext, FastestTopDomainUtil.getInstance().getTopSpeedDomain());
        mBinding.rvAgent.setAdapter(mainDomainAdapter);
        ivwIcon.setOnClickListener(v -> {
            if(secondaryLayout.getVisibility() == VISIBLE){
                secondaryLayout.setVisibility(GONE);
            }else {
                secondaryLayout.setVisibility(VISIBLE);
            }
        });
        mBinding.tvSpeedCheck.setOnClickListener(v -> {
            FastestTopDomainUtil.getInstance().start();
        });
    }

    public void refresh() {
        mainDomainAdapter.setNewData(FastestTopDomainUtil.getInstance().getTopSpeedDomain());
    }

    public boolean isShowing() {
        return secondaryLayout.getVisibility() == VISIBLE;
    }
}
