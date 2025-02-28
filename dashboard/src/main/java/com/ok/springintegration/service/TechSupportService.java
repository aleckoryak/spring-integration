package com.ok.springintegration.service;

import com.ok.springintegration.endpoint.TechSupportMessageHandler;
import com.ok.springintegration.util.AppSupportStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Service
public class TechSupportService extends TechSupportMessageHandler{
    private Logger logger = LoggerFactory.getLogger(TechSupportService.class);
    private Timer timer = new Timer();

    private AbstractSubscribableChannel techSupportDirectChannel;
    private QueueChannel updateNotificationQueueChannel;

    @Autowired
    public TechSupportService(AbstractSubscribableChannel techSupportDirectChannel, QueueChannel updateNotificationQueueChannel) {
        this.techSupportDirectChannel = techSupportDirectChannel;
        this.updateNotificationQueueChannel = updateNotificationQueueChannel;
        timer.schedule(new TimerTask() {
            public void run() {
                checkVersionCurrency();
            }
        }, 10000, 10000);;
    }

    @Override
    protected void receiveAndAcknowledge(AppSupportStatus status) {
        logger.info("Tech support service received new build notification: " + status.toString());
    }

    private void checkVersionCurrency() {

        // Check REST api for more current software version

        // For now, following results in a fake notice to the queue every 10 seconds
        updateNotificationQueueChannel.send(MessageBuilder.withPayload("New software version available.").build(), 1000);
    }

}
