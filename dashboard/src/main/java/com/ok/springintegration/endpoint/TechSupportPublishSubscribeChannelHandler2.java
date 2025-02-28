package com.ok.springintegration.endpoint;

import com.ok.springintegration.service.DashboardService;
import com.ok.springintegration.util.AppSupportStatus;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class TechSupportPublishSubscribeChannelHandler2 extends TechSupportMessageHandler {
    static Logger logger = LoggerFactory.getLogger(TechSupportPublishSubscribeChannelHandler2.class);
    @Autowired
    private AbstractSubscribableChannel techSupportPublishSubscribeChannel;
    @Autowired
    private QueueChannel updateNotificationQueueChannel;

    @PostConstruct
    public void init() {
        techSupportPublishSubscribeChannel.subscribe(this);
    }

    @Override
    protected void receiveAndAcknowledge(AppSupportStatus status) {
        logger.info("receiveAndAcknowledge status from techSupportPublishSubscribeChannel:" + status);
        updateNotificationQueueChannel.send(MessageBuilder.withPayload("New software version available.").build(), 1000);
        logger.info("sent message to QueueChannel");
    }
}
