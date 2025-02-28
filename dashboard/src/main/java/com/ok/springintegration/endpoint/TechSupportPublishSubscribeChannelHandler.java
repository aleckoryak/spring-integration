package com.ok.springintegration.endpoint;

import com.ok.springintegration.service.DashboardService;
import com.ok.springintegration.util.AppSupportStatus;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class TechSupportPublishSubscribeChannelHandler extends TechSupportMessageHandler {
    static Logger logger = LoggerFactory.getLogger(TechSupportPublishSubscribeChannelHandler.class);
    private Timer timer = new Timer();
    @Autowired
    private AbstractSubscribableChannel techSupportPublishSubscribeChannel;
    @Autowired
    private QueueChannel updateNotificationChannel;

    @PostConstruct
    public void init() {
        techSupportPublishSubscribeChannel.subscribe(this);
        timer.schedule(new TimerTask() {
            public void run() {
                checkForNotifications();
            }
        }, 3000, 3000);
    }

    private void checkForNotifications() {
        // Check queue for notifications that the software needs to be updated
        GenericMessage<?> message = (GenericMessage<?>) updateNotificationChannel.receive(1000);
        if (message != null) {
            DashboardService.setDashboardStatus("softwareBuild", message.getPayload().toString());
        }
    }

    @Override
    protected void receiveAndAcknowledge(AppSupportStatus status) {
        logger.info("receiveAndAcknowledge status:" + status);
        DashboardService.setDashboardStatus("techSupportPublishSubscribeChannel", status.getVersion());

    }
}
