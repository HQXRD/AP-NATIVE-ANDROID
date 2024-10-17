package com.xtree.service.message;

public class RemoteMessage {

    private String type;
    private int fd;
    private long timestamp;

    // Constructor
    public RemoteMessage(String type, int fd, long timestamp) {
        this.type = type;
        this.fd = fd;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFd() {
        return fd;
    }

    public void setFd(int fd) {
        this.fd = fd;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", fd=" + fd +
                ", timestamp=" + timestamp +
                '}';
    }
}
