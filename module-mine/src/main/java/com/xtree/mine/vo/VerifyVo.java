package com.xtree.mine.vo;

public class VerifyVo {

    public long timeout; // 1702981804 (发送验证码 返回的)
    public int timeoutsec; // 60 (发送验证码 返回的)

    public String tokenSign; // c660da80fbb863cb8862d0f74a62e4f4 (点击确定 返回的)
    public String mark; // bind (点击确定 返回的)

    @Override
    public String toString() {
        return "VerifyVo { " +
                "timeout=" + timeout +
                ", timeoutsec=" + timeoutsec +
                ", tokenSign='" + tokenSign + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
