package com.alienvault.otx.connect.internal;

import com.alienvault.otx.connect.internal.HeaderSettingRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;

/**
 * Internal factory for secure communication
 */
public class HTTPConfig {

    /**
     * Internal API to create the request factory used for communication with
     * OTX.
     * @param apiKey - API key used to authenticate the requests
     * @return Client request factory for use in a RestTemplate
     */
    public static ClientHttpRequestFactory createRequestFactory(String apiKey) {
        return new HeaderSettingRequestFactory(apiKey);
    }
}
