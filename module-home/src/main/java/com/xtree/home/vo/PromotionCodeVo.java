package com.xtree.home.vo;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * 注册新账户获取 第一步 code
 */
public class PromotionCodeVo extends BaseResponse2 {
    public String agency_model;
    public String default_skin;
    public String domian;//回传的code
    public String  top_id;
    public boolean  verifycodeswitch;

    @Override
    public String toString() {
        return "PromotionCodeVo{" +
                "agency_model='" + agency_model + '\'' +
                ", default_skin='" + default_skin + '\'' +
                ", domian='" + domian + '\'' +
                ", top_id='" + top_id + '\'' +
                ", verifycodeswitch=" + verifycodeswitch +
                '}';
    }
}
