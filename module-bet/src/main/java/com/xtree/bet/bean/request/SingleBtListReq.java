package com.xtree.bet.bean.request;

import java.util.ArrayList;
import java.util.List;

public class SingleBtListReq {
    private int currencyId = 1;
    private String languageType = "CMN";
    private List<BtCgReq> singleBetList = new ArrayList<>();
    public void addSingleBetList(BtCgReq singleBetReq) {
        this.singleBetList.add(singleBetReq);
    }
}
