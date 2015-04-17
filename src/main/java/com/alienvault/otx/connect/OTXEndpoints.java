package com.alienvault.otx.connect;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rspitler on 4/16/15.
 */
public enum OTXEndpoints {
    SUBSCRIBED("/api/v1/pulses/subscribed", Arrays.asList("limit", "modified_since"));

    private final String endpoint;
    private List<String> parameters;

    OTXEndpoints(String endpoint, List<String> parameters) {
        this.endpoint = endpoint;
        this.parameters = parameters;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
