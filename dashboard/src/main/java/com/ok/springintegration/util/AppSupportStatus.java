package com.ok.springintegration.util;

import java.util.Date;

public class AppSupportStatus {

    private String snapVersion;

    private Date snapTime;

    public AppSupportStatus(String version, Date dttm) {
        this.snapVersion = version;
        this.snapTime = dttm;
    }

    public String getVersion() {
        return snapVersion;
    }

    public Date getTime() {
        return snapTime;
    }

    @Override
    public String toString() {
        return "AppSupportStatus{" +
                "snapVersion='" + snapVersion + '\'' +
                ", snapTime=" + snapTime +
                '}';
    }
}
