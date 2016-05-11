package com.alienvault.otx.connect;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * Created by rspitler on 5/10/16.
 */
public class ConnectionUtil {
    public static OTXConnection getOtxConnection(Environment environment, String apiKey) {
        String otxHost = environment.getProperty("host");
        String otxScheme = environment.getProperty("scheme");
        String otxPort = environment.getProperty("port");
        Integer port = null;
        if (otxPort != null)
            port = Integer.valueOf(otxPort);
        return new OTXConnection(apiKey, otxHost, otxScheme, port);
    }
}
