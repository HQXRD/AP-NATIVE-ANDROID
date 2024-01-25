package com.xtree.mine.vo;

public class ForgetPasswordCheckInfoVo {
    public String phone;
    public String email;
    public String msg;

    @Override
    public String toString() {
        return "ForgetPasswordCheckInfoVo{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", msg='" + msg +
                '}';
    }
}
