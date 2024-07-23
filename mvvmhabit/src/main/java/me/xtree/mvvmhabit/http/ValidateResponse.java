package me.xtree.mvvmhabit.http;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by KAKA on 2024/7/19.
 * Describe:
 */
public class ValidateResponse {

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
    private Map<String, String> data;
    /**
     * timestamp
     */
    @SerializedName("timestamp")
    private int timestamp;

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

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

}
