package com.xtree.mine.vo;

import com.xtree.base.widget.FilterView;

/**
 * 投注记录-平台类型 (同 盈亏报表-类型)
 * 接口 用code,不用cid
 */
public class BtPlatformVo implements FilterView.IBaseVo {
    public String cid = ""; // 41, 30
    //public String mode; // "fbxcv2_game",
    //public String bet_infolist_func; // "fbxcBetInfoList",
    //public String bet_info_func; // "fbxcBetInfo",
    public String code = ""; // "FBXC",
    //public String s_code; // "fbxc",
    //public Object table; // "fbxc_projects",或者 { "3": "imonesp_projects", "6": "imone_projects"},
    //public String type; // 3,
    public String cn_name = ""; // "FB自营体育", "IM电竞/体育"
    public boolean front_show_status; // true

    // ThirdGameTypeVo
    //public String cid; // 41,
    //public String code; // "FBXC",
    //public String cn_name; // "杏彩体育",
    public boolean selected; // true

    public BtPlatformVo(String cid, String code, String cn_name) {
        this.cid = cid;
        this.code = code;
        this.cn_name = cn_name;
    }

    @Override
    public String toString() {
        return "BtPlatformVo {" +
                "cid='" + cid + '\'' +
                ", code='" + code + '\'' +
                ", cn_name='" + cn_name + '\'' +
                ", front_show_status=" + front_show_status +
                '}';
    }

    @Override
    public String getShowId() {
        return code; // 这里要用 code
    }

    @Override
    public String getShowName() {
        return cn_name;
    }


}
