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

    TextView tvwTitle;
    ImageView ivwClose;
    RecyclerView rcvMain;
    String title;
    RecyclerView.Adapter adapter;

    public ListDialog(@NonNull Context context) {
        super(context);
    }

    public ListDialog(@NonNull Context context, String title, RecyclerView.Adapter adapter) {
        super(context);
        this.title = title;
        this.adapter = adapter;
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
        return (XPopupUtils.getScreenHeight(getContext()) * 4 / 10);
    }

}
