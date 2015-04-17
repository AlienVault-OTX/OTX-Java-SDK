package com.alienvault.otx.connect;

import com.alienvault.otx.connect.internal.HTTPConfig;
import com.alienvault.otx.model.Page;
import com.alienvault.otx.model.Pulse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by rspitler on 4/16/15.
 */
public class OTXConnection {

    private RestTemplate restTemplate;
    private String otxHost;
    private String otxScheme;
    private Integer otxPort = null;

    public OTXConnection(String apiKey, String otxHost, String otxScheme, Integer port) {
        this.otxHost = otxHost;
        this.otxScheme = otxScheme;
        otxPort = port;
        configureRestTemplate(apiKey);
    }
    public OTXConnection(String apiKey, String otxHost, String otxScheme) {
        this.otxHost = otxHost;
        this.otxScheme = otxScheme;
        configureRestTemplate(apiKey);
    }

    public OTXConnection(String apiKey) {
        this.otxHost = "otx.alienvault.com";
        this.otxScheme = "https";
        configureRestTemplate(apiKey);
    }

    public OTXConnection(String apiKey, String otxHost) {
        this.otxHost = otxHost;
        this.otxScheme = "https";
        configureRestTemplate(apiKey);
    }

    private void configureRestTemplate(String apiKey) {
        ClientHttpRequestFactory requestFactory = HTTPConfig.createRequestFactory(apiKey);
        restTemplate = new RestTemplate(requestFactory);
    }

    public void dump() throws URISyntaxException, MalformedURLException {
        ResponseEntity<String> response = restTemplate.getForEntity(buildURI(OTXEndpoints.SUBSCRIBED), String.class);
        System.out.println(response);
    }
    public List<Pulse> getAllPulses() throws URISyntaxException, MalformedURLException {
        List<Pulse> pulseList = new ArrayList<>();
        Page firstPage = executeGetRequest(OTXEndpoints.SUBSCRIBED, Page.class);
        pulseList.addAll(firstPage.getResults());
        while (firstPage.getNextURI() != null) {
            pulseList.addAll(firstPage.getResults());
            firstPage = executeGetRequest(OTXEndpoints.SUBSCRIBED, Page.class);
        }
        return pulseList;
    }

    public List<Pulse> getPulsesSinceDate(Date lastUpdated) throws URISyntaxException, MalformedURLException {
        List<Pulse> pulseList = new ArrayList<>();
        Page firstPage = executeGetRequest(OTXEndpoints.SUBSCRIBED, Page.class);
        pulseList.addAll(firstPage.getResults());
        while (firstPage.getNextURI() != null) {
            firstPage = executeGetRequest(OTXEndpoints.SUBSCRIBED, Page.class);
            pulseList.addAll(firstPage.getResults());
        }
        return pulseList;
    }

    private <T> T executeGetRequest(OTXEndpoints subscribed, Class<T> classType) throws URISyntaxException, MalformedURLException {
        return restTemplate.getForObject(buildURI(subscribed), classType);
    }

    private URI buildURI(OTXEndpoints endpoint) throws URISyntaxException, MalformedURLException {
        if (otxPort!=null) {
            return new URL(otxScheme, otxHost, otxPort, endpoint.getEndpoint()).toURI();
        } else {
            return new URL(otxScheme, otxHost, endpoint.getEndpoint()).toURI();
        }
    }
}
