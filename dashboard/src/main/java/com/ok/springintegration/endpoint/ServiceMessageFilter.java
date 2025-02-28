package com.ok.springintegration.endpoint;

import com.ok.springintegration.util.AppSupportStatus;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class ServiceMessageFilter implements MessageSelector {
    Logger logger = Logger.getLogger(ServiceMessageFilter.class.getName());

    @Override
    public boolean accept(Message<?> message) throws MessagingException {
        Object payload = message.getPayload();
        if (payload instanceof AppSupportStatus) {
            return filterMessage((AppSupportStatus) payload);
        } else {
            logger.warning("Unknown data type has been received.");
            return false;
 //           throw new MessageRejectedException(message, "Unknown data type has been received.");
        }
    }

    protected boolean filterMessage(AppSupportStatus status){
        logger.info("filterMessage status:" + status);
        return status.isUpdateRequired();
    }
}