package com.ok.springintegration.endpoint;

import com.ok.springintegration.service.DashboardService;
import com.ok.springintegration.util.AppSupportStatus;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public class TechSupportDirectChannelHandler extends TechSupportMessageHandler {
    static Logger logger = LoggerFactory.getLogger(TechSupportDirectChannelHandler.class);
    @Autowired
    private AbstractSubscribableChannel techSupportDirectChannel;

    @PostConstruct
    public void init() {
        techSupportDirectChannel.subscribe(this);
    }

    @Override
    protected void receiveAndAcknowledge(AppSupportStatus status) {
        logger.info("receiveAndAcknowledge status from techSupportDirectChannel:" + status);
        DashboardService.setDashboardStatus("techSupportDirectChannel", status.getVersion());
    }
}
