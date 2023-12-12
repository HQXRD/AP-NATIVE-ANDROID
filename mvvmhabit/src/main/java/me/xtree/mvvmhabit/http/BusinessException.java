package me.xtree.mvvmhabit.http;

/**
 * 业务异常类
 */

public class BusinessException extends Exception {
    public int code;
    public String message;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
