package com.xtree.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class LooperHeartHandler {

    private Handler mHandler;
    private volatile boolean isRunning = true; // 控制循环的标志

    private long heartTime=10*1000;
    public LooperHeartHandler() {
    }

    public abstract void handleMessage(Message msg);

    public abstract void heart();

    public void start() {
        start();
    }

    /**
     * 循环时间  默认1是
     *
     * @param heartTime
     */
    public void start(long heartTime) {
        this.heartTime = heartTime;
        new Thread(() -> {
            // 准备 Looper
            Looper.prepare();

            // 创建 Handler
            mHandler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    LooperHeartHandler.this.handleMessage(msg);
                }
            };

            // 设置 isRunning 为 true，启动心跳发送
            isRunning = true;
            new Thread(() -> {
                while (isRunning) {
                    try {
                        Thread.sleep(heartTime); // 暂停一段时间以避免过于频繁地发送消息
                        heart(); // 发送心跳
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // 恢复中断状态
                    }
                }
            }).start();

            // 启动消息循环
            Looper.loop();
        }).start();
    }

    public void setHeartTime(long heartTime) {
        this.heartTime = heartTime;
    }

    public void stop() {
        if (mHandler != null) {
            mHandler.getLooper().quit(); // 退出 Looper
            isRunning = false;
        }
    }

    public void sendMessage(Message msg) {
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    public Message obtainMessage() {
        if (mHandler != null) {
            return mHandler.obtainMessage();
        }
        return null;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
