package com.xtree.home.vo;

public class CreditWalletVo {
    public boolean credit_USDT_enable; // false,
    public boolean credit_RMB_enable; // false,
    public String balance_U; // "0.0000"
    public String balance_RMB; // "0.0000"

    @Override
    public String toString() {
        return "CreditWalletVo{" +
                "credit_USDT_enable=" + credit_USDT_enable +
                ", credit_RMB_enable=" + credit_RMB_enable +
                ", balance_U='" + balance_U + '\'' +
                ", balance_RMB='" + balance_RMB + '\'' +
                '}';
    }
}
