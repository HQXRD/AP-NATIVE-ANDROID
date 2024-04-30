package com.xtree.bet.weight;

import static com.xtree.bet.ui.activity.MainActivity.KEY_PLATFORM;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_FBXC;
import static com.xtree.bet.ui.activity.MainActivity.PLATFORM_PM;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.AttachPopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.DomainUtil;
import com.xtree.bet.R;
import com.xtree.bet.ui.adapter.BtDomainAdapter;
import com.xtree.bet.util.BtDomainUtil;

import me.xtree.mvvmhabit.utils.SPUtils;

public class DomainChangeDialog extends AttachPopupView {
    private Context mContext;
    private ICallBack mICallBack;
    private RecyclerView rvAgent;
    private CheckBox cbAgent;
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
        boolean isAgent = SPUtils.getInstance().getBoolean(SPKeyGlobal.KEY_USE_AGENT + mPlatform);
        cbAgent.setChecked(isAgent);
        cbAgent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_LINE_POSITION + mPlatform, 0);
            SPUtils.getInstance().put(SPKeyGlobal.KEY_USE_AGENT + mPlatform, isChecked);
            mICallBack.onDomainChange(isChecked, cbAgent);
        });
        if (!TextUtils.equals(mPlatform, PLATFORM_PM)) {
            rvAgent.setLayoutManager(new LinearLayoutManager(mContext));
            if(TextUtils.equals(mPlatform, PLATFORM_FBXC)) {
                rvAgent.setAdapter(new BtDomainAdapter(mContext, BtDomainUtil.getFbxcDomainUrl(), mICallBack, cbAgent));
            }else{
                rvAgent.setAdapter(new BtDomainAdapter(mContext, BtDomainUtil.getFbDomainUrl(), mICallBack, cbAgent));
            }
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.bt_layout_domain_agent;
    }

    public interface ICallBack {
        void onDomainChange(boolean isChecked, CheckBox checkBox);
    }
}
