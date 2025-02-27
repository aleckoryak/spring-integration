package com.ok.integration.helloworld;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

public class HelloWorldApp {
    private static final Log logger = LogFactory.getLog(HelloWorldApp.class);

    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/integration/helloWorldDemo.xml", HelloWorldApp.class);
        MessageChannel inputChannel = context.getBean("inputChannel", MessageChannel.class);
        PollableChannel outputChannel = context.getBean("outputChannel", PollableChannel.class);
        for (int i = 0; i < 10; i++) {
            logger.info("i"+i);
            inputChannel.send(new GenericMessage<String>("World"+i));
        }

        inputChannel.send(new GenericMessage<String>("World"));
        logger.info("==> HelloWorldDemo: " + outputChannel.receive(0).getPayload());
        context.close();
    }
}
