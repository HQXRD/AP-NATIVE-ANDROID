package com.xtree.mine.vo;

import com.xtree.base.widget.FilterView;

/**
 * 报表-选择类型
 */
public class OrderTypeVo implements FilterView.IBaseVo {

    public String id; // 1,
    public String cntitle; // "充值",

    //public int[] ordertypeid; // [ 31,38,322 ]

    public OrderTypeVo(int id, String cntitle) {
        this.id = String.valueOf(id);
        this.cntitle = cntitle;
    }

    public OrderTypeVo(String id, String cntitle) {
        this.id = id;
        this.cntitle = cntitle;
    }

    @Override
    public String toString() {
        return "OrderTypeVo {" +
                "id=" + id +
                ", cntitle='" + cntitle + '\'' +
                '}';
    }

    @Override
    public String getShowId() {
        return id;
    }

    @Override
    public String getShowName() {
        return cntitle;
    }

}
