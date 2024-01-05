package com.xtree.bet.bean.ui;

import com.stx.xhb.androidx.entity.BaseBannerInfo;
import com.xtree.bet.bean.response.fb.PlayTypeInfo;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.constant.SPKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public interface PlayGroup{
    List<PlayGroup> getPlayGroupList();
    List<PlayType> getOriginalPlayTypeList();
    List<PlayType> getPlayTypeList();
}
