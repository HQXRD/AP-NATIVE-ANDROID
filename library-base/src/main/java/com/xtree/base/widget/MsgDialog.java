package com.xtree.base.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;

public class MsgDialog extends CenterPopupView {
    TextView tvwTitle;
    TextView tvwMsg;
    TextView tvwLeft;
    TextView tvwRight;

    String title;
    String msg;
    String txtLeft;
    String txtRight;
    ICallBack mCallBack;

    public interface ICallBack {
        void onClickLeft();

        void onClickRight();
    }

    public MsgDialog(@NonNull Context context) {
        super(context);
    }

    public MsgDialog(@NonNull Context context, String title, String msg, ICallBack mCallBack) {
        super(context);
        this.title = title;
        this.msg = msg;
        this.mCallBack = mCallBack;
    }

    public MsgDialog(@NonNull Context context, String title, String msg, String txtLeft, String txtRight, ICallBack mCallBack) {
        super(context);
        this.title = title;
        this.msg = msg;
        this.txtLeft = txtLeft;
        this.txtRight = txtRight;
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        tvwMsg = findViewById(R.id.tvw_msg);
        tvwLeft = findViewById(R.id.tvw_left);
        tvwRight = findViewById(R.id.tvw_right);

        if (!TextUtils.isEmpty(title)) {
            tvwTitle.setText(title);
        }

        tvwMsg.setText(msg);

        if (!TextUtils.isEmpty(txtLeft)) {
            tvwLeft.setText(txtLeft);
        }
        if (!TextUtils.isEmpty(txtRight)) {
            tvwRight.setText(txtRight);
        }

        tvwLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClickLeft();
            }
        });
        tvwRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onClickRight();
            }
        });

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_msg;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        return (XPopupUtils.getScreenHeight(getContext()) * 4 / 10);
    }

}
