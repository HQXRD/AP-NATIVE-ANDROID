package com.xtree.bet.bean.request.fb;

import java.util.ArrayList;
import java.util.List;

public class BtCashOutStatusReq {
    private String languageType = "CMN";
    private List<String> ids = new ArrayList<>();

    public void addId(String id) {
        this.ids.add(id);
    }
}
