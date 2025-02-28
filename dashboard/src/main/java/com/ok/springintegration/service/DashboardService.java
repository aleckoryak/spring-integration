package com.ok.springintegration.service;

import com.ok.springintegration.util.AppSupportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
@DependsOn({"viewService", "techSupportChannel"})

public class DashboardService {
    private static final Properties dashboardStatusDao = new Properties();
    static Logger logger = LoggerFactory.getLogger(DashboardService.class);
    private final int instanceId = System.identityHashCode(this);
    @Autowired
//    @Lazy changed to @DependsOn
    private final AbstractSubscribableChannel techSupportChannel;
    @Autowired
    private ViewService viewService;
    private TechSupportService techSupportService;

    public DashboardService(AbstractSubscribableChannel techSupportChannel) {
        this.techSupportChannel = techSupportChannel;
        logger.info("DashboardService instance created: " + instanceId);
        dashboardStatusDao.setProperty("instanceId", String.valueOf(instanceId));
        initializeTechSupport();
    }

    static void setDashboardStatus(String key, String value) {
        String v = (value != null ? value : "");
        dashboardStatusDao.setProperty(key, v);
    }

    public Properties getDashboardStatus() {
        return dashboardStatusDao;
    }

    private void initializeTechSupport() {
        dashboardStatusDao.setProperty("softwareBuild", "");
        AppSupportStatus status = new AppSupportStatus("" + instanceId, new Date());

        GenericMessage message = (GenericMessage) MessageBuilder
                .withPayload(status)
                .build();

        techSupportChannel.send(message);
        logger.info("initializeTechSupport: message sent");
    }

}
