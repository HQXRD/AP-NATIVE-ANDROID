package com.xtree.mine.vo;

import com.xtree.base.widget.FilterView;

/**
 * 报表-选择器-状态
 */
public class StatusVo implements FilterView.IBaseVo {

    public String id;
    public String title;

    public StatusVo(int id, String title) {
        this.id = String.valueOf(id);
        this.title = title;
    }

    public StatusVo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "StatusVo {" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public String getShowName() {
        return title;
    }

    @Override
    public String getShowId() {
        return String.valueOf(id);
    }
}
