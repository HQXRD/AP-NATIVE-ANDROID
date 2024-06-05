package com.xtree.recharge.data.source.request;

/**
 * Created by KAKA on 2024/5/28.
 * Describe:
 */
public class ExBankInfoRequest {
    //pid=176&merchantOrder=PAY00172920240527175410000005
    private String pid;
    private String merchantOrder;

    public ExBankInfoRequest(String pid, String merchantOrder) {
        this.pid = pid;
        this.merchantOrder = merchantOrder;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMerchantOrder() {
        return merchantOrder;
    }

    public void setMerchantOrder(String merchantOrder) {
        this.merchantOrder = merchantOrder;
    }
}
