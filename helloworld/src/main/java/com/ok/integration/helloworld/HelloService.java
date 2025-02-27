package com.ok.integration.helloworld;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HelloService {
    private static final Log logger = LogFactory.getLog(HelloService.class);
    public String sayHello(String name) {
        logger.info("receive message:" +name);
        return "Hello " + name;
    }
}
