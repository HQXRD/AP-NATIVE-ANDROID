package com.xtree.bet.bean.request;

import java.util.ArrayList;
import java.util.List;

public class BtMultipleListReq {
    private int currencyId = 1;
    private String languageType = "CMN";
    private List<BtOptionReq> betOptionList = new ArrayList<>();
    private List<BtCgReq> betMultipleData = new ArrayList<>();
    public void addBtOptionList(BtOptionReq btOptionReq) {
        this.betOptionList.add(btOptionReq);
    }
    public void addBtMultipleData(BtCgReq btCgReq) {
        this.betMultipleData.add(btCgReq);
    }
}
