package com.xtree.base.widget.impl;

/**
 * Created by KAKA on 2024/3/9.
 * Describe:FilterView的点击回调接口
 */
public interface FilterViewOnClickListerner {
    void onClick(String startDate, String endDate, String startTime, String endTime, String typeId, String typeId2, String statusId);
}
