package com.ok.springintegration.util;

import java.util.Date;

public class AppSupportStatus {

    private final String snapVersion;

    private final Date snapTime;


    private final boolean updateRequired;

    public AppSupportStatus(String version, Date dttm) {
        this.snapVersion = version;
        this.snapTime = dttm;
        this.updateRequired = false;
    }

    public AppSupportStatus(String version, Date dttm, boolean updateRequired) {
        this.snapVersion = version;
        this.snapTime = dttm;
        this.updateRequired = updateRequired;
    }

    public String getVersion() {
        return snapVersion;
    }

    public Date getTime() {
        return snapTime;
    }

    public boolean isUpdateRequired() {
        return updateRequired;
    }

    public String getCustomerNotification() {
        if (updateRequired) {
            return "A software update is required.";
        }
        return snapVersion;
    }

    @Override
    public String toString() {
        return "AppSupportStatus{" +
                "snapVersion='" + snapVersion + '\'' +
                ", snapTime=" + snapTime +
                ", updateRequired=" + updateRequired +
                '}';
    }
}
