package me.xtree.mvvmhabit.http;


import retrofit2.HttpException;

/**
 * Created by goldze on 2017/5/11.
 */

public class ResponseThrowable extends Exception {
    public int code;
    public String message;
    /**
     * 是否网络错误，如404，503等
     */
    public boolean isHttpError;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ResponseThrowable(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseThrowable(Throwable throwable, int code, boolean isHttpError) {
        super(throwable);
        this.code = code;
        if(throwable instanceof HttpException){
            this.code = ((HttpException) throwable).code();
        }
        this.isHttpError = isHttpError;
    }
}
