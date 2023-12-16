package com.xtree.base.widget;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.xtree.base.R;

public class ListDialog extends BottomPopupView {

    String title; // 标题
    RecyclerView.Adapter adapter;
    int maxHeight = 40; // 最大高度百分比 10-100

    TextView tvwTitle;
    ImageView ivwClose;
    RecyclerView rcvMain;

    public ListDialog(@NonNull Context context) {
        super(context);
    }

    public ListDialog(@NonNull Context context, String title, RecyclerView.Adapter adapter) {
        super(context);
        this.title = title;
        this.adapter = adapter;
    }

    /**
     * @param context   Context
     * @param title     标题
     * @param adapter   适配器
     * @param maxHeight 最大高度, 占屏幕高度的百分比 (推荐 30-90)
     */
    public ListDialog(@NonNull Context context, String title, RecyclerView.Adapter adapter, int maxHeight) {
        super(context);
        this.title = title;
        this.adapter = adapter;
        this.maxHeight = maxHeight;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        ivwClose = findViewById(R.id.ivw_close);
        rcvMain = findViewById(R.id.rcv_main);

        tvwTitle.setText(title);
        rcvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvMain.setAdapter(adapter);

        ivwClose.setOnClickListener(v -> dismiss());

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_list;
    }

    @Override
    protected int getMaxHeight() {
        //return super.getMaxHeight();
        if (maxHeight < 5 || maxHeight > 100) {
            maxHeight = 40;
        }
        return (XPopupUtils.getScreenHeight(getContext()) * maxHeight / 100);
    }

}
