package com.xtree.mine.vo;

import com.xtree.base.widget.FilterView;

/**
 * (盈亏)平台列表-平台类型
 */
public class ThirdGameTypeVo implements FilterView.IBaseVo {

    public String cid; // 41,
    public String code; // "FBXC",
    public String cn_name; // "杏彩体育",
    public boolean selected; // true

    public ThirdGameTypeVo(String cid, String cn_name) {
        this.cid = cid;
        this.cn_name = cn_name;
    }

    @Override
    public String getShowName() {
        return cn_name;
    }

    @Override
    public String getShowId() {
        return cid;
    }

    @Override
    public String toString() {
        return "ThirdGameTypeVo{" +
                "cid='" + cid + '\'' +
                ", code='" + code + '\'' +
                ", cn_name='" + cn_name + '\'' +
                ", selected='" + selected + '\'' +
                '}';
    }

}
