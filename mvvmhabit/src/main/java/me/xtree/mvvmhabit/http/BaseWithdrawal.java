package me.xtree.mvvmhabit.http;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 针对提款新接口 自定义基类
 * @param <T>
 */
public class BaseWithdrawal<T>{

    private int status = -1;
    private int code = -1;
    private boolean success;
    @SerializedName(value = "message", alternate = {"msg", "sMsg"})
    @Expose
    private String message = ""; // msg, sMsg

    public int timestamp; // 1700702751
    private T data;

    public void setData(T data) {
        this.data = data;

    }

    public boolean isOk() {
        return status == 10000;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
