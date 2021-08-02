package com.xyy.config;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

@Component
public class DevTools implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(DevTools.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("guava.jar classloader"+ DispatcherServlet.class.getClassLoader().toString());
        logger.info("DevTools classloader"+ this.getClass().getClassLoader().toString());
    }
}
