package com.devmcryyu.bitmapfont;

/**
 * Created by 92075 on 2018/5/10.
 */

public class device {
    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;
    private String ipAddress;
    private String MAC;
    private boolean status;

    public device() {
        new device("0.0.0.0", "00:00:00:00:00:00", OFFLINE);
    }

    public device(String ipAddress, String mac, boolean status) {
        this.ipAddress = ipAddress;
        this.MAC = mac;
        this.status = status;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString() {
        return "设备IP: " + this.getIpAddress() + " 设备MAC: " + this.getMAC() + " 当前状态: " + (this.getStatus() ? "在线" : "离线");
    }
}
