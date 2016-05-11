package com.alienvault.otx.connect;

/**
 * Parameters used by OTX Endpoints
 */
public enum OTXEndpointParameters {
    /**
     * Limit is used by any Paged endpoint to set the page size
     */
    LIMIT("limit", OTXEndpointParameterTypes.INTEGER),
    /**
     * Modified since is used by the SUBSCRIBED endpoint to limit the Pulses return.
     */
    MODIFIED_SINCE("modified_since", OTXEndpointParameterTypes.DATE),
    /**
     * Page is used by any Paged endpoint to set the page to retrieve
     */
    PAGE("page", OTXEndpointParameterTypes.INTEGER),
    /**
     * id is used by a number of endpoints for indicator and pulse retrieval
     */
    ID("id", OTXEndpointParameterTypes.STRING, true),
    /**
     * query is used for search endpoints
     */
    QUERY("q", OTXEndpointParameterTypes.STRING),
    /**
     * username is used for user related actions
     */
    USERNAME("username", OTXEndpointParameterTypes.STRING,true),
    /**
     * action is used for user
     */
    ACTION("action",OTXEndpointParameterTypes.STRING,true);

    private String parameterName;
    private OTXEndpointParameterTypes type;
    private boolean restVariable;

    OTXEndpointParameters(String parameterName, OTXEndpointParameterTypes type) {
        this.parameterName = parameterName;
        this.type = type;
    }

    OTXEndpointParameters(String parameterName, OTXEndpointParameterTypes type, boolean restVariable) {
        this.parameterName = parameterName;
        this.type = type;
        this.restVariable = restVariable;
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
     * @return whether this is a rest variable
     */
    public boolean isRestVariable() {
        return restVariable;
    }
}
