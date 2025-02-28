package com.ok.springintegration.service;

import com.ok.springintegration.endpoint.TechSupportDirectChannelHandler;
import com.ok.springintegration.endpoint.TechSupportPublishSubscribeChannelHandler;
import com.ok.springintegration.endpoint.TechSupportPublishSubscribeChannelHandler2;
import com.ok.springintegration.util.AppSupportStatus;
import org.aspectj.lang.annotation.RequiredTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
@DependsOn({"techSupportDirectChannelHandler", "techSupportPublishSubscribeChannelHandler", "techSupportPublishSubscribeChannelHandler2"})
public class DashboardService {
    private static final Properties dashboardStatusDao = new Properties();
    static Logger logger = LoggerFactory.getLogger(DashboardService.class);
    private final int instanceId = System.identityHashCode(this);
    @Autowired
    private DirectChannel techSupportDirectChannel;
    @Autowired
    private PublishSubscribeChannel techSupportPublishSubscribeChannel;
    @Autowired
    private TechSupportDirectChannelHandler techSupportDirectChannelHandler;
    @Autowired
    TechSupportPublishSubscribeChannelHandler techSupportPublishSubscribeChannelHandler;
    @Autowired
    TechSupportPublishSubscribeChannelHandler2 techSupportPublishSubscribeChannelHandler2;
    private TechSupportService techSupportService;

    public DashboardService(DirectChannel techSupportDirectChannel, PublishSubscribeChannel techSupportPublishSubscribeChannel) {
        this.techSupportPublishSubscribeChannel = techSupportPublishSubscribeChannel;
        this.techSupportDirectChannel = techSupportDirectChannel;
        logger.info("DashboardService instance created: " + instanceId);
        dashboardStatusDao.setProperty("instanceId", String.valueOf(instanceId));
        initializeTechSupport();
    }

    public static void setDashboardStatus(String key, String value) {
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

        techSupportDirectChannel.send(message);
        logger.info("initializeTechSupport: message sent to techSupportDirectChannel");
        techSupportPublishSubscribeChannel.send(message);
        logger.info("initializeTechSupport: message sent to techSupportPublishSubscribeChannel");
    }

}
