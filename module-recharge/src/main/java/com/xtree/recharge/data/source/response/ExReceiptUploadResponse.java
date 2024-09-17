package com.xtree.recharge.data.source.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KAKA on 2024/5/30.
 * Describe:
 */
public class ExReceiptUploadResponse {

    /**
     * returncode
     */
    @SerializedName("returncode")
    private String returncode;
    /**
     * message
     */
    @SerializedName("message")
    private String message;

    public String getReturncode() {
        return returncode;
    }

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
