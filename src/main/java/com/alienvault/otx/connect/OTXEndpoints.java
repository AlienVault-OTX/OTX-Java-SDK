package com.alienvault.otx.connect;

import java.util.Arrays;
import java.util.List;

/**
 * The enumeration of OTX Endpoints
 * @see OTXEndpointParameters for associated paramenters
 */
public enum OTXEndpoints {
    INDICATOR_IPV4("indicators/IPv4/{indicator}/{section}"),
    INDICATOR_IPV6("indicators/IPv6/{indicator}/{section}"),
    INDICATOR_DOMAIN("indicators/domain/{indicator}/{section}"),
    INDICATOR_HOSTNAME("indicators/hostname/{indicator}/{section}"),
    INDICATOR_FILE("indicators/file/{indicator}/{section}"),
    INDICATOR_URL("indicators/url/{indicator}/{section}"),
    INDICATOR_CVE("indicators/cve/{indicator}/{section}"),

    INDICATORS_FOR_PULSE("pulses/{id}/indicators"),
    RELATED_TO_PULSE("pulses/{id}/related"),
    PULSE_DETAILS("pulses/{id}"),
    SUBSCRIBED("pulses/subscribed"),
    ACTIVITY("pulses/activity"),
    EVENTS("pulses/events"),
    PULSE_CREATE("pulses/create"),
    PULSE_CREATE_VALIDATE_INDICATORS("pulses/indicators/validate"),

    SEARCH_USERS("search/users"),
    SEARCH_PULSES("search/pulses"),

    USERS_ME("users/me"),
    USERS_ACTION("users/{username}/{action}");

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
