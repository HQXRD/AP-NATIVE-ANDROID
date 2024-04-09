package com.xtree.bet.bean.request.fb;

import java.util.ArrayList;
import java.util.List;

public class BtCashOutPriceReq {
    private String languageType = "CMN";
    private List<String> orderIds = new ArrayList<>();

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }
}
