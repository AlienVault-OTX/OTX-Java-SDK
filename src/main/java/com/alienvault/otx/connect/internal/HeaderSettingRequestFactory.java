package com.alienvault.otx.connect.internal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;

/**
 * Internal utility class to set the authorization header used for the OTX API
 * authentication.
 */
public class HeaderSettingRequestFactory extends SimpleClientHttpRequestFactory {
    private static final String SDK_USER_AGENT = String.format("%s/%s/%s",
            System.getProperty("otx.sdk.project", "OTX Java SDK"),
            System.getProperty("otx.sdk.version", "1.0"),
            System.getProperty("java.version"));
    private String apiKey;

    public HeaderSettingRequestFactory(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        ClientHttpRequest request = super.createRequest(uri, httpMethod);
        request.getHeaders().add("X-OTX-API-KEY",apiKey);
        request.getHeaders().add(HttpHeaders.USER_AGENT,SDK_USER_AGENT);
        return request;
    }
}
