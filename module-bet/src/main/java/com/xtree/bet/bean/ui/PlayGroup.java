package com.xtree.bet.bean.ui;

import java.util.List;

public interface PlayGroup{
    List<PlayGroup> getPlayGroupList(String sportId);
    List<PlayType> getOriginalPlayTypeList();
    List<PlayType> getPlayTypeList();
}
