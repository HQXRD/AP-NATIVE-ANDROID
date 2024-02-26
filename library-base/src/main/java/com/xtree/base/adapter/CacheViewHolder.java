package com.xtree.base.adapter;

import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CacheViewHolder extends RecyclerView.ViewHolder implements LayoutContainer {
    public CountDownTimer countDownTimer; // 2024/02/26 此为倒数计时，在遇到需要单

    public CacheViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
