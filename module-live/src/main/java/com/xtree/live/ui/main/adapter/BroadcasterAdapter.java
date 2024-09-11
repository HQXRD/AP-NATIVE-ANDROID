package com.xtree.live.ui.main.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xtree.base.adapter.CacheViewHolder;
import com.xtree.base.adapter.CachedAutoRefreshAdapter;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

/**
 * 主播列表Adapter
 */
public class BroadcasterAdapter extends CachedAutoRefreshAdapter<LiveViewModel> {
    @NonNull
    @Override
    public CacheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CacheViewHolder holder, int position) {

    }
}
