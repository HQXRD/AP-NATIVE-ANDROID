package com.xtree.bet.weight;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.AttachPopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.bet.R;
import com.xtree.bet.ui.adapter.BtDomainAdapter;
import com.xtree.base.utils.BtDomainUtil;

import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public class DomainChangeDialog extends AttachPopupView {
    private Context mContext;
    private ICallBack mICallBack;
    private RecyclerView rvAgent;
    private NestedScrollView nsvAgent;
    private CheckBox cbAgent;
    private RelativeLayout rlAgent;

    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

    public DomainChangeDialog(@NonNull Context context, ICallBack iCallBack) {
        super(context);
        mContext = context;
        this.mICallBack = iCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        cbAgent = findViewById(R.id.cb_agent);
        rvAgent = findViewById(R.id.rv_agent);
        nsvAgent = findViewById(R.id.nsv_agent);
        rlAgent = findViewById(R.id.rl_agent);

        boolean bGameSwitch = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_GAME_SWITCH + mPlatform);
        rlAgent.setVisibility(bGameSwitch ? VISIBLE : GONE);

        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);
        cbAgent.setChecked(isAgent);
        cbAgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_AGENT + mPlatform, isChecked);
            mICallBack.onDomainChange(isChecked, false, cbAgent);
        });

        if (!isAgent && !TextUtils.equals(mPlatform, PLATFORM_PM)) {
            nsvAgent.setVisibility(VISIBLE);
            rvAgent.setLayoutManager(new LinearLayoutManager(mContext));
            if (TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                rvAgent.setAdapter(new BtDomainAdapter(mContext, BtDomainUtil.getFbxcDomainUrl(), mICallBack, cbAgent));
            } else {
                rvAgent.setAdapter(new BtDomainAdapter(mContext, BtDomainUtil.getFbDomainUrl(), mICallBack, cbAgent));
            }
        } else {
            nsvAgent.setVisibility(GONE);
        }

        nsvAgent.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (nsvAgent.getHeight() > ConvertUtils.dp2px(130)) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) nsvAgent.getLayoutParams();
                params.height = ConvertUtils.dp2px(130);
                nsvAgent.setLayoutParams(params);
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.bt_layout_domain_agent;
    }

    public interface ICallBack {
        /**
         * 监听线路变更
         *
         * @param useAgent       是否使用代理
         * @param isChangeDomain 是否点击切换按钮
         * @param checkBox
         */
        void onDomainChange(boolean useAgent, boolean isChangeDomain, CheckBox checkBox);
    }
}