package com.ok.springintegration.util;

import java.util.Properties;

@Deprecated
public class AppProperties {

    private Properties runtimeProperties;

    public void setRuntimeProperties(Properties props) {
        this.runtimeProperties = props;
    }

    public Properties getRuntimeProperties() {
        return runtimeProperties;
    }
}
