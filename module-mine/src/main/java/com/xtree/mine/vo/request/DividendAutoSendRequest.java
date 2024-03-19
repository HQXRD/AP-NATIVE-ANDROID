package com.xtree.mine.vo.request;

import android.webkit.URLUtil;

import com.xtree.base.utils.UuidUtil;

/**
 * Created by KAKA on 2024/3/15.
 * Describe:
 */
public class DividendAutoSendRequest {
    private String flag = "all";
    private String type;
    private String cycle_id;
    private String nonce;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCycle_id() {
        return cycle_id;
    }

    public void setCycle_id(String cycle_id) {
        this.cycle_id = cycle_id;
    }

    public String getNonce() {
        return UuidUtil.getID16();
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
