package com.ddos.system.model;

public class IPInfo {
    private String ip;
    private int requestCount;
    private boolean blocked;

    public IPInfo(String ip) {
        this.ip = ip;
        this.requestCount = 1;
        this.blocked = false;
    }

    public String getIp() {
        return ip;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void incrementRequestCount() {
        this.requestCount++;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
