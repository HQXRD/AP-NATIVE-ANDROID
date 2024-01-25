package me.xtree.mvvmhabit.http;

/**
 * 业务异常类
 */
public class BusinessException extends Exception {
    public int code;
    public String message;
    public Object data; // 处理异常,传数据,备用

    @Override
    public String toString() {
        return "BusinessException {" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
