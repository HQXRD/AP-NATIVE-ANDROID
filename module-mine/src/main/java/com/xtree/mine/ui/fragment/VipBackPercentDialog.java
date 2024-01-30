package com.xtree.mine.ui.fragment;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.mine.R;
import com.xtree.mine.vo.VipUpgradeItemVo;

import java.util.List;

public class VipBackPercentDialog extends BottomPopupView {
    int maxHeight = 40; // 最大高度百分比 10-100
    List<VipUpgradeItemVo> vo;
    private int level;
    private VipInfoAdapter mVipInfoAdapter;
    private Context mContext;

    public VipBackPercentDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public VipBackPercentDialog(@NonNull Context context, List<VipUpgradeItemVo> vipUpgradeItemList, int level) {
        super(context);
        mContext = context;
        vo = vipUpgradeItemList;
        this.level = level;
    }

    /**
     * @param context   Context
     * @param maxHeight 最大高度, 占屏幕高度的百分比 (推荐 30-90)
     */
    public VipBackPercentDialog(@NonNull Context context, List<VipUpgradeItemVo> vipUpgradeItemList, int level, int maxHeight) {
        super(context);
        mContext = context;
        vo = vipUpgradeItemList;
        this.level = level;
        this.maxHeight = maxHeight;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        if (maxHeight < 5 || maxHeight > 100) {
            maxHeight = 40;
        }
        return (XPopupUtils.getScreenHeight(getContext()) * maxHeight / 100);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_vip;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        ImageView ivwClose = findViewById(R.id.ivw_close);
        RecyclerView rcv = findViewById(R.id.rcv_back_percent);
        mVipInfoAdapter = new VipInfoAdapter(getContext(), level);
        rcv.setLayoutManager(new LinearLayoutManager(mContext, GridLayoutManager.VERTICAL, false));
        rcv.setAdapter(mVipInfoAdapter);
        mVipInfoAdapter.setData(vo);

        ivwClose.setOnClickListener(v -> dismiss());
    }
}
