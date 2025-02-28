package com.ok.springintegration.endpoint;

import com.ok.springintegration.service.DashboardService;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class TechSupportQueueChannelReceiver {
    Logger logger = Logger.getLogger(TechSupportQueueChannelReceiver.class.getName());

    @ServiceActivator(inputChannel = "updateNotificationQueueChannel", poller = @Poller(fixedDelay = "1000"))
    public void receiveMessage(Message<String> message) {
        logger.info("Received from Queue by activator: " + message.getPayload());
        DashboardService.setDashboardStatus("updateNotificationQueueChannel", message.getPayload());
    }
}
