package com.alienvault.otx.connect.internal;

import com.alienvault.otx.connect.internal.HeaderSettingRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;

/**
 * Created by rspitler on 4/16/15.
 */
public class HTTPConfig {

    public static ClientHttpRequestFactory createRequestFactory(String apiKey) {

        return new HeaderSettingRequestFactory(apiKey);
    }
}
