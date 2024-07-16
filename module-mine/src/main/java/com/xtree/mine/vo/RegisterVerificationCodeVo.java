package com.xtree.mine.vo;

import me.xtree.mvvmhabit.http.BaseResponse2;

/**
 * 注册 验证码 Vo
 */
public class RegisterVerificationCodeVo extends BaseResponse2 {
    public String  key ;
    public String image_url;
    public String refresh_url;
    public String expires_in;//刷新时间 秒？

}
