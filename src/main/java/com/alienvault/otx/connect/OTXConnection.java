package com.alienvault.otx.connect;

import com.alienvault.otx.connect.internal.HTTPConfig;
import com.alienvault.otx.model.*;
import com.alienvault.otx.model.events.Event;
import com.alienvault.otx.model.events.EventPage;
import com.alienvault.otx.model.indicator.Indicator;
import com.alienvault.otx.model.indicator.IndicatorPage;
import com.alienvault.otx.model.pulse.Pulse;
import com.alienvault.otx.model.pulse.PulsePage;
import com.alienvault.otx.model.user.User;
import com.alienvault.otx.model.user.UserActions;
import com.alienvault.otx.model.user.UserPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

/**
 * OTXConnextion takes care of the requests made to
 * the OTX service.  The utility methods provided give you
 * the mechanisms necessary to get the Pulses that have
 * been subscribed to in the OTX API.
 * <p/>
 * Construct this class passing in your API key found in the
 * 'Settings' page of the web interface.
 */
public class OTXConnection {

    private RestTemplate restTemplate;
    private RetryTemplate retryTemplate;

    private String otxHost = "otx.alienvault.com";
    private String otxScheme = "https";
    private Integer otxPort = null;
    private static DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
    private Log log = LogFactory.getLog(OTXConnection.class);

    /**
     * Construct the OTX Connection providing full connection details.
     *
     * @param apiKey - API key for your OTX Account
     * @param host   - host of the OTX server (otx.alienvault.com by default)
     * @param scheme - scheme to use for the connection to the server (https by default)
     * @param port   - port for the connection to the server (443 by default)
     */
    public OTXConnection(String apiKey, String host, String scheme, Integer port) {
        if (host != null)
            this.otxHost = host;
        if (scheme != null)
            this.otxScheme = scheme;
        if (port != null)
            otxPort = port;
        configureRestTemplate(apiKey);
    }

    /**
     * Construct the OTX Connection providing full connection details.
     *
     * @param apiKey    - API key for your OTX Account
     * @param otxHost   - host of the OTX server (otx.alienvault.com by default)
     * @param otxScheme - scheme to use for the connection to the server (https by default)
     */
    public OTXConnection(String apiKey, String otxHost, String otxScheme) {
        this.otxHost = otxHost;
        this.otxScheme = otxScheme;
        configureRestTemplate(apiKey);
    }

    /**
     * Construct the OTX Connection providing full connection details.
     *
     * @param apiKey - API key for your OTX Account
     */
    public OTXConnection(String apiKey) {
        configureRestTemplate(apiKey);
    }

    /**
     * Construct the OTX Connection providing full connection details.
     *
     * @param apiKey  - API key for your OTX Account
     * @param otxHost - host of the OTX server (otx.alienvault.com by default)
     */
    public OTXConnection(String apiKey, String otxHost) {
        this.otxHost = otxHost;
        configureRestTemplate(apiKey);
    }

    /**
     * Internal API to configure RestTemplate
     *
     * @param apiKey - API key to configure authorization header
     */
    private void configureRestTemplate(String apiKey) {
        ClientHttpRequestFactory requestFactory = HTTPConfig.createRequestFactory(apiKey);
        restTemplate = new RestTemplate(requestFactory);
        retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());
    }

    /**
     * Utility method to access all Pulses subscribed to in the web interface.
     *
     * @return All of the Pulses
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public List<Pulse> getAllPulses() throws URISyntaxException, MalformedURLException {
        return getPulses(null);
    }

    /**
     * Access all indicators for a given pulse id
     *
     * @param pulseId id of pulse
     * @return List<Indicators> indicators contained within pulse
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public List<Indicator> getAllIndicatorsForPulse(String pulseId) throws MalformedURLException, URISyntaxException {
        return (List<Indicator>) getPagedResults(Collections.singletonMap(OTXEndpointParameters.ID, pulseId), new IndicatorPage(), OTXEndpoints.INDICATORS_FOR_PULSE);
    }

    /**
     * Access all pulses related to a given pulse id
     *
     * @param pulseId id of pulse
     * @return List<Pulse> pulses related to the given pulse id
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public List<Pulse> getAllRelatedPulses(String pulseId) throws MalformedURLException, URISyntaxException {
        return (List<Pulse>) getPagedResults(Collections.singletonMap(OTXEndpointParameters.ID, pulseId), new PulsePage(), OTXEndpoints.RELATED_TO_PULSE);
    }

    /**
     * Utility method to access all Pulses modified since the date passed to the API.
     *
     * @param lastUpdated - date to cut off the list of pulses
     * @return All of the Pulses modified since the lastUpdated date
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    public List<Pulse> getPulsesSinceDate(DateTime lastUpdated) throws URISyntaxException, MalformedURLException {
        return getPulses(Collections.singletonMap(OTXEndpointParameters.MODIFIED_SINCE, fmt.print(lastUpdated)));
    }

    /**
     * @param pulseId
     * @return
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public Pulse getPulseDetails(String pulseId) throws MalformedURLException, URISyntaxException {
        return executeGetRequest(OTXEndpoints.PULSE_DETAILS, Collections.singletonMap(OTXEndpointParameters.ID, pulseId), Pulse.class);

    }

    /**
     * @param searchQuery
     * @return
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public List<Pulse> searchForPulses(String searchQuery) throws MalformedURLException, URISyntaxException {
        return (List<Pulse>) getPagedResults(Collections.singletonMap(OTXEndpointParameters.QUERY, searchQuery), new PulsePage(), OTXEndpoints.SEARCH_PULSES);
    }

    /**
     * @param searchQuery
     * @return
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public List<User> searchForUsers(String searchQuery) throws MalformedURLException, URISyntaxException {
        return (List<User>) getPagedResults(Collections.singletonMap(OTXEndpointParameters.QUERY, searchQuery), new UserPage(), OTXEndpoints.SEARCH_USERS);
    }

    /**
     * Get the User object representing the authenticated user
     * @return User
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public User getMyDetails() throws MalformedURLException, URISyntaxException {
        return executeGetRequest(OTXEndpoints.USERS_ME, null, User.class);
    }

    /**
     * Create a new pulse.
     *
     * @param newPulse - object representing the pulse to create
     * @return the newly created Pulse object with ID and created meta-data
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public Pulse createPulse(Pulse newPulse) throws MalformedURLException, URISyntaxException {
        return (Pulse) executePostRequest(OTXEndpoints.PULSE_CREATE, newPulse, Pulse.class).getBody();
    }

    /**
     * Allows the ability to follow, subscribe, unfollow, and unsubscribe
     *
     * @param username - NOTE this is case-sensitive
     * @param action - the action to perform
     * @return - the response with a value for the key 'status'
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public Map performUserAction(String username, UserActions action) throws MalformedURLException, URISyntaxException {
        Map<OTXEndpointParameters, String> parameterMap = new HashMap<>();
        parameterMap.put(OTXEndpointParameters.USERNAME, username);
        parameterMap.put(OTXEndpointParameters.ACTION, action.getAction());
        return restTemplate.postForEntity(buildURI(OTXEndpoints.USERS_ACTION, parameterMap), null, Map.class).getBody();
    }

    /**
     * Get all events related to the authenticated account
     * @return List<Event>
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public List<Event> getAllEvents() throws MalformedURLException, URISyntaxException {
        return (List<Event>) getPagedResults(null, new EventPage(), OTXEndpoints.EVENTS);
    }

    /**
     * Get all events related to the authenticated account since the passed timeframe
     * @param cutoffDate only events after this date will be returned
     * @return List<Event>
     * @throws MalformedURLException
     * @throws URISyntaxException
     */
    public List<Event> getEventsSince(DateTime cutoffDate) throws MalformedURLException, URISyntaxException {
        return (List<Event>) getPagedResults(Collections.singletonMap(OTXEndpointParameters.MODIFIED_SINCE, fmt.print(cutoffDate)), new EventPage(), OTXEndpoints.EVENTS);
    }

    private List<Pulse> getPulses(Map<OTXEndpointParameters, ?> endpointParametersMap) throws URISyntaxException, MalformedURLException {

        return (List<Pulse>) getPagedResults(endpointParametersMap, new PulsePage(), OTXEndpoints.SUBSCRIBED);
    }

    private List<?> getPagedResults(Map<OTXEndpointParameters, ?> endpointParametersMap, Page<?> page, OTXEndpoints endpoint) throws MalformedURLException, URISyntaxException {
        if (endpointParametersMap == null || !endpointParametersMap.containsKey(OTXEndpointParameters.LIMIT)){
               Map newParams;
               if (endpointParametersMap != null) {
                   newParams = new HashMap(endpointParametersMap);
               }else{
                   newParams = new HashMap();
               }
               newParams.put(OTXEndpointParameters.LIMIT, 20);
               endpointParametersMap = newParams;
           }
        List pulseList = new ArrayList<>();
        Page<?> firstPage = executeGetRequest(endpoint, endpointParametersMap, page.getClass());
        pulseList.addAll(firstPage.getResults());
        while (firstPage.getNext() != null) {
            String rawQuery = firstPage.getNext().getRawQuery();
            String nextPage = getParameterFromQueryString(rawQuery, OTXEndpointParameters.PAGE);
            // cskellie - passed in new parameter with page count to fetch next page of results.
            Map parametersMap = new HashMap<>();
            if (endpointParametersMap!=null) {
                parametersMap.putAll(endpointParametersMap);
            }
            parametersMap.putAll(Collections.singletonMap(OTXEndpointParameters.PAGE, nextPage));
            firstPage = executeGetRequest(endpoint, parametersMap, page.getClass());
            pulseList.addAll(firstPage.getResults());
        }
        return pulseList;

    }

    private String getParameterFromQueryString(String rawQuery, OTXEndpointParameters page) {
        String[] pairs = rawQuery.split("&");

        Map<String, String> query_pairs = new HashMap<String, String>();
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("Unexpected issue with encoding", e);
            }
        }
        return query_pairs.get(page.getParameterName());

    }

    private <T> T executeGetRequest(final OTXEndpoints subscribed, final Map<OTXEndpointParameters, ?> endpointParametersMap, final Class<T> classType) throws MalformedURLException, URISyntaxException {

        final URI url = buildURI(subscribed, endpointParametersMap);

        return  retryTemplate.execute(new RetryCallback<T, RestClientException>() {

            public T doWithRetry(RetryContext context) {
                // Do stuff that might fail, e.g. webservice operation
                return restTemplate.getForObject(url, classType);
            }

        });
    }

    private URI buildURI(OTXEndpoints endpoint, Map<OTXEndpointParameters, ?> endpointParametersMap) throws URISyntaxException, MalformedURLException {

        String endpointString = endpoint.getEndpoint();
        if (endpointParametersMap != null) {
            boolean first = true;
            for (Map.Entry<OTXEndpointParameters, ?> otxEndpointParametersEntry : endpointParametersMap.entrySet()) {
                if (otxEndpointParametersEntry.getKey().isRestVariable()) {
                    endpointString = endpointString.replace("{" + otxEndpointParametersEntry.getKey().getParameterName() + "}", otxEndpointParametersEntry.getValue().toString());
                } else {
                    if (first) {
                        endpointString = endpointString + "?";
                        first = false;
                    }
                    try {
                        String parameterName = otxEndpointParametersEntry.getKey().getParameterName();
                        String value = UriUtils.encodeQueryParam(otxEndpointParametersEntry.getValue().toString(), "UTF-8");
                        endpointString = endpointString + String.format("%s=%s&", parameterName, value);
                    } catch (UnsupportedEncodingException e) {
                        log.error("Unpossible");
                    }
                }
            }
        }
        if (otxPort != null) {
            return new URL(otxScheme, otxHost, otxPort, endpointString).toURI();
        } else {
            return new URL(otxScheme, otxHost, endpointString).toURI();
        }
    }


    private ResponseEntity<?> executePostRequest(OTXEndpoints endpoint, Object toPost, Class<Pulse> responseType) throws MalformedURLException, URISyntaxException {
        return restTemplate.postForEntity(buildURI(endpoint, null), toPost, responseType);
    }

}
