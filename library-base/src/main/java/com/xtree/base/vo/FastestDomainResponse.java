package com.xtree.base.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KAKA on 2024/8/14.
 * Describe: 接口测速返回体
 */
public class FastestDomainResponse {

    //{"status":10000,"message":"success","data":{"microtime":1723623023.724247},"timestamp":1723623023}
    /**
     * status
     */
    @SerializedName("status")
    private int status;
    /**
     * message
     */
    @SerializedName("message")
    private String message;
    /**
     * data
     */
    @SerializedName("data")
    private DataDTO data;
    /**
     * timestamp
     */
    @SerializedName("timestamp")
    private long timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataDTO {
        /**
         * microtime
         */
        @SerializedName("microtime")
        private float microtime;

        public float getMicrotime() {
            return microtime;
        }

        public void setMicrotime(float microtime) {
            this.microtime = microtime;
        }
    }
}
