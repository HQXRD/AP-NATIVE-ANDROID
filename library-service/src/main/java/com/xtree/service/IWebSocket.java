package com.xtree.service;

public interface IWebSocket {

    public void connectSocket(String url,long checkInterval);

    public void stopSocket();

    public void sendMessage(String message);

    public boolean isRunning();

}
