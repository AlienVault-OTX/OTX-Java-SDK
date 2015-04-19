package com.alienvault.otx.connect;

import java.util.Arrays;
import java.util.List;

/**
 * The enumeration of OTX Endpoints
 * @see OTXEndpointParameters for associated paramenters
 */
public enum OTXEndpoints {
    SUBSCRIBED("pulses/subscribed");

    private final String endpoint;

    OTXEndpoints(String endpoint) {
        this.endpoint = "/api/v1/" + endpoint;
    }

    /**
     * @return Relative URL for endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

}
