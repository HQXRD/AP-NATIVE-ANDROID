package com.xtree.bet.bean.ui;

import java.util.ArrayList;
import java.util.List;

public class BtRecordTime {
    private long time;

    private List<BtResult> btResultList = new ArrayList<>();

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<BtResult> getBtResultList() {
        return btResultList;
    }

    public void addBtResultList(BtResult btResult) {
        this.btResultList.add(btResult);
    }
}
