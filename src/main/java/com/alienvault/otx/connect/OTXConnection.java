package com.alienvault.otx.connect;

import com.alienvault.otx.connect.internal.HTTPConfig;
import com.alienvault.otx.model.Page;
import com.alienvault.otx.model.Pulse;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * OTXConnextion takes care of the requests made to
 * the OTX service.  The utility methods provided give you
 * the mechanisms necessary to get the Pulses that have
 * been subscribed to in the OTX API.
 *
 * Construct this class passing in your API key found in the
 * 'Settings' page of the web interface.
 */
public class OTXConnection {

    private RestTemplate restTemplate;
    private String otxHost = "otx.alienvault.com";
    private String otxScheme = "https";
    private Integer otxPort = null;
    /** cskellie - Page count to pass to parameters map */
    private Integer pageCount = 1;
    private static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

    /**
     * Construct the OTX Connection providing full connection details.
     * @param apiKey - API key for your OTX Account
     * @param otxHost - host of the OTX server (otx.alienvault.com by default)
     * @param otxScheme - scheme to use for the connection to the server (https by default)
     * @param port - port for the connection to the server (443 by default)
     */
    public OTXConnection(String apiKey, String otxHost, String otxScheme, Integer port) {
        this.otxHost = otxHost;
        this.otxScheme = otxScheme;
        otxPort = port;
        configureRestTemplate(apiKey);
    }
    /**
    * Construct the OTX Connection providing full connection details.
    * @param apiKey - API key for your OTX Account
    * @param otxHost - host of the OTX server (otx.alienvault.com by default)
    * @param otxScheme - scheme to use for the connection to the server (https by default)
    */
    public OTXConnection(String apiKey, String otxHost, String otxScheme) {
        this.otxHost = otxHost;
        this.otxScheme = otxScheme;
        configureRestTemplate(apiKey);
    }
    /**
    * Construct the OTX Connection providing full connection details.
    * @param apiKey - API key for your OTX Account
    */
    public OTXConnection(String apiKey) {
        configureRestTemplate(apiKey);
    }
    /**
    * Construct the OTX Connection providing full connection details.
    * @param apiKey - API key for your OTX Account
    * @param otxHost - host of the OTX server (otx.alienvault.com by default)
    */
    public OTXConnection(String apiKey, String otxHost) {
        this.otxHost = otxHost;
        configureRestTemplate(apiKey);
    }

    /**
     * Internal API to configure RestTemplate
     * @param apiKey - API key to configure authorization header
     */
    private void configureRestTemplate(String apiKey) {
        ClientHttpRequestFactory requestFactory = HTTPConfig.createRequestFactory(apiKey);
        restTemplate = new RestTemplate(requestFactory);
    }

    /**
     * Utility method to access all Pulses subscribed to in the web interface.
     * @return All of the Pulses
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public List<Pulse> getAllPulses() throws URISyntaxException, MalformedURLException {
       return getPulses(null);
    }
    /**
    * Utility method to access all Pulses modified since the date passed to the API.
    * @return All of the Pulses modified since the lastUpdated date
    * @param lastUpdated - date to cut off the list of pulses
    * @throws URISyntaxException
    * @throws MalformedURLException
    */
    public List<Pulse> getPulsesSinceDate(DateTime lastUpdated) throws URISyntaxException, MalformedURLException {
        return getPulses(Collections.singletonMap(OTXEndpointParameters.MODIFIED_SINCE, fmt.print(lastUpdated)));
    }

    private List<Pulse> getPulses(Map<OTXEndpointParameters, ?> endpointParametersMap) throws URISyntaxException, MalformedURLException {
        List<Pulse> pulseList = new ArrayList<>();
        Page firstPage = executeGetRequest(OTXEndpoints.SUBSCRIBED,endpointParametersMap, Page.class);
        pulseList.addAll(firstPage.getResults());
        while (firstPage.getNext() != null) 
        {
        	// cskellie - passed in new parameter with page count to fetch next page of results.
            firstPage = executeGetRequest(OTXEndpoints.SUBSCRIBED, Collections.singletonMap(OTXEndpointParameters.PAGE, ++pageCount), Page.class);
            pulseList.addAll(firstPage.getResults());
        }
        return pulseList;
    }

    private <T> T executeGetRequest(OTXEndpoints subscribed, Map<OTXEndpointParameters, ?> endpointParametersMap, Class<T> classType) throws URISyntaxException, MalformedURLException {
        return restTemplate.getForObject(buildURI(subscribed, endpointParametersMap), classType);
    }

    private URI buildURI(OTXEndpoints endpoint, Map<OTXEndpointParameters, ?> endpointParametersMap) throws URISyntaxException, MalformedURLException {

        String endpointString = endpoint.getEndpoint();
        if(endpointParametersMap!=null)
        {
            endpointString = endpointString+"?";
            for (Map.Entry<OTXEndpointParameters, ?> otxEndpointParametersEntry : endpointParametersMap.entrySet()) 
            {
                String parameterName = otxEndpointParametersEntry.getKey().getParameterName();
                String value = otxEndpointParametersEntry.getValue().toString();
                endpointString = endpointString+ String.format("%s=%s&", parameterName, value);
            }
        }
        if (otxPort!=null) {
            return new URL(otxScheme, otxHost, otxPort, endpointString).toURI();
        } else {
            return new URL(otxScheme, otxHost, endpointString).toURI();
        }
    }
}
