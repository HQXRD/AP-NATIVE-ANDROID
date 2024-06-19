package com.xtree.base.utils;

import android.util.Log;

import com.xtree.base.BuildConfig;

/**
 * 日志接口 <功能详细描述>
 *
 * @author Administrator
 * @version [版本号]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CfLog {

    private final static String TAG = "TAG";
    static String className;
    static String methodName;
    static int lineNumber;

    /**
     * 日志控制：发布版本不需要打印日志 当 CURRENT = VERBOSE时所有日志都打印 当CURRENT = NOTHING时 所有日志都不打印
     */
    private static int VERBOSE = 0;
    private static int DEBUG = 1;
    private static int INFO = 2;
    private static int WARNING = 3;
    private static int ERROR = 4;
    private static int NOTHING = 5;
    private static int CURRENT = isDebuggable() ? VERBOSE : WARNING;

    private CfLog() {
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog(String log) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("[(");
        buffer.append(className);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append(")#");
        buffer.append(methodName);
        buffer.append("]--- ");
        buffer.append(log);

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(String message) {

        if (ERROR >= CURRENT) {
            if (!isDebuggable()) {
                Log.e(TAG, message + "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            Log.e(TAG, createLog(message));
        }
    }

    public static void e(String message, Object... args) {
        if (DEBUG >= CURRENT) {
            if (!isDebuggable()) {
                Log.e(TAG, message + "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());

            Log.e(TAG, createLog(String.format(message, args)));
        }
    }

    public static void e(Throwable e, String message) {

        if (ERROR >= CURRENT) {
            if (!isDebuggable()) {
                Log.e(TAG, message + "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            Log.e(TAG, createLog(message) + "\n " + e.toString());
        }
    }

    public static void w(String message, Object... args) {
        if (DEBUG >= CURRENT) {
            if (!isDebuggable()) {
                Log.e(TAG, message + "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());

            Log.w(TAG, createLog(String.format(message, args)));
        }
    }

    public static void i() {
        if (INFO >= CURRENT) {
            if (!isDebuggable()) {
                Log.i(TAG, "************");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            Log.i(TAG, createLog("************"));
        }

    }

    public static void i(String message) {
        if (INFO >= CURRENT) {
            if (!isDebuggable()) {
                Log.i(TAG, message + "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            //Log.i(className, createLog(message));
            String msg = createLog(message);
            final int maxLength = 1600;
            if (msg.length() > maxLength) {
                Log.i(TAG, "msg.length = " + msg.length());
                int chunkCount = msg.length() / maxLength;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = maxLength * (i + 1);
                    if (max >= msg.length()) {
                        Log.w(TAG, msg.substring(maxLength * i));
                    } else {
                        Log.w(TAG, msg.substring(maxLength * i, max));
                    }
                }
            } else {
                Log.i(TAG, msg);
            }
        }

    }

    public static void d() {
        if (DEBUG >= CURRENT) {
            if (!isDebuggable()) {
                Log.d(TAG, "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            Log.d(TAG, createLog("--------"));
        }
    }

    public static void d(String message) {
        if (DEBUG >= CURRENT) {
            if (!isDebuggable()) {
                Log.d(TAG, message + "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());
            Log.d(TAG, createLog(message));
        }
    }

    public static void d(String message, Object... args) {
        if (DEBUG >= CURRENT) {
            if (!isDebuggable()) {
                Log.d(TAG, message + "");
                return;
            }
            getMethodNames(new Throwable().getStackTrace());

            Log.d(TAG, createLog(String.format(message, args)));
        }
    }

    public static void v(String message) {
        if (VERBOSE >= CURRENT) {
            if (!isDebuggable()) {
                Log.v(TAG, message + "");
                return;
            }

            getMethodNames(new Throwable().getStackTrace());
            Log.v(TAG, createLog(message));
        }
    }

    public static void w(String message) {
        if (WARNING >= CURRENT) {
            if (!isDebuggable()) {
                Log.w(TAG, message + "");
                return;
            }

            getMethodNames(new Throwable().getStackTrace());
            Log.w(TAG, createLog(message));
        }
    }

    public static void wtf(String message) {
        if (!isDebuggable()) {
            Log.wtf(TAG, message + "");
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(TAG, createLog(message));
    }

}
