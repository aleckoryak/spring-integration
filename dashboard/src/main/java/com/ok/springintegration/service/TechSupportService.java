package com.ok.springintegration.service;

import com.ok.springintegration.endpoint.TechSupportMessageHandler;
import com.ok.springintegration.util.AppSupportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.stereotype.Service;

@Service
public class TechSupportService {
    static Logger logger = LoggerFactory.getLogger(TechSupportService.class);

    private AbstractSubscribableChannel techSupportChannel;
    private QueueChannel updateNotificationQueueChannel;

    @Autowired
    public TechSupportService(AbstractSubscribableChannel techSupportChannel, QueueChannel updateNotificationQueueChannel) {
        this.techSupportChannel = techSupportChannel;
        this.updateNotificationQueueChannel = updateNotificationQueueChannel;
        // Initialize our class property techSupportChannel using application context
        // techSupportChannel = (cast here) DashboardManager.getDashboardContext().getBean("techSupportChannel");
        this.start();
    }

    private void start() {
        // Represents long-running process thread

        // Subscribe to the tech support channel
    }

    private boolean isVersionCurrent() {
        return true;
    }

    private static class ServiceMessageHandler extends TechSupportMessageHandler {

        protected void receiveAndAcknowledge(AppSupportStatus status) {
            TechSupportService.logger.info("Tech support service received new build notification: " + status.toString());
        }
    }
}
