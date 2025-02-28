package com.ok.springintegration.service;

import com.ok.springintegration.endpoint.TechSupportMessageHandler;
import com.ok.springintegration.util.AppSupportStatus;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Component
public class ViewService extends TechSupportMessageHandler {

    static Logger logger = LoggerFactory.getLogger(ViewService.class);
    private Timer timer = new Timer();


    @Autowired
    private AbstractSubscribableChannel techSupportChannel;
    @Autowired
    private QueueChannel updateNotificationChannel;

    @PostConstruct
    public void init() {
        techSupportChannel.subscribe(this);
    }


    private void start() {
        // Represents long-running process thread
        timer.schedule(new TimerTask() {
            public void run() {
                checkForNotifications();
            }
        }, 3000, 3000);
    }

    private void checkForNotifications() {
        // Check queue for notifications that the software needs to be updated
    }

    @Override
    protected void receiveAndAcknowledge(AppSupportStatus status) {
        logger.info("receiveAndAcknowledge status:" + status);
        DashboardService.setDashboardStatus("softwareBuild", status.getVersion());

    }

    private static class ViewMessageHandler extends TechSupportMessageHandler {
        Logger logger = LoggerFactory.getLogger(ViewMessageHandler.class);
        protected void receiveAndAcknowledge(AppSupportStatus status) {
            logger.info("receiveAndAcknowledge status:" + status);
            DashboardService.setDashboardStatus("softwareBuild", status.getVersion());
        }
    }
}
