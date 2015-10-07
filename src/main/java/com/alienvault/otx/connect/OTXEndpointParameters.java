package com.alienvault.otx.connect;

/**
 * Parameters used by OTX Endpoints
 */
public enum OTXEndpointParameters {
    /**
     * Limit is used by the SUBSCRIBED endpoint to set the page size
     */
    LIMIT("limit", OTXEndpoints.SUBSCRIBED, OTXEndpointParameterTypes.INTEGER),
    /**
     * Modified since is used by the SUBSCRIBED endpoint to limit the Pulses return.
     */
    MODIFIED_SINCE("modified_since", OTXEndpoints.SUBSCRIBED, OTXEndpointParameterTypes.DATE),
    
    /** cskellie - page parameter to get next page of results. */
    PAGE("page", OTXEndpoints.SUBSCRIBED, OTXEndpointParameterTypes.INTEGER);

    private String parameterName;
    private OTXEndpoints endPoint;
    private OTXEndpointParameterTypes type;

    OTXEndpointParameters(String parameterName, OTXEndpoints endPoint, OTXEndpointParameterTypes type) {
        this.parameterName = parameterName;
        this.endPoint = endPoint;
        this.type = type;
    }

    /**
     * @return Parameter string used in the GET URL
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @return the type used by the parameter
     */
    public OTXEndpointParameterTypes getType() {
        return type;
    }

    /**
     * @return the endpoint related to this parameter
     */
    public OTXEndpoints getEndPoint() {
        return endPoint;
    }
}
