package com.alienvault.otx;

import com.alienvault.otx.connect.ConnectionUtil;
import com.alienvault.otx.connect.OTXConnection;
import com.alienvault.otx.model.events.Event;
import com.alienvault.otx.model.indicator.Indicator;
import com.alienvault.otx.model.indicator.IndicatorType;
import com.alienvault.otx.model.pulse.Pulse;
import com.alienvault.otx.model.user.User;
import com.alienvault.otx.model.user.UserActions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommandlineRunner.class)
@TestPropertySource(locations = "classpath:test_application.properties")
public class OtxJavaSdkApplicationTests {
    public String VALID_PULSE_NAME;
    public String VALID_PULSE_ID;
    public String VALID_USERNAME;
    @Autowired
    ConfigurableApplicationContext context;
    @Autowired
    Environment env;

    OTXConnection otxConnection;

    @Before
    public void setUp() throws Exception {
        String property = env.getProperty("key");
        if(StringUtils.isEmpty(property))
            throw new Exception("Error loading API key.  Please enter your api key in src/test/resources/test_applications.properties before running tests");
        otxConnection = ConnectionUtil.getOtxConnection(env, property);
        VALID_PULSE_NAME = env.getProperty("VALID_PULSE_NAME");
        VALID_PULSE_ID = env.getProperty("VALID_PULSE_ID");
        VALID_USERNAME = env.getProperty("VALID_USERNAME");
    }

    @Test
    public void testListAllPulses() throws MalformedURLException, URISyntaxException {
        List<Pulse> allPulses = otxConnection.getAllPulses();
        assertTrue(allPulses.size() > 0);
    }

    @Test
    public void testListSinceDate() throws MalformedURLException, URISyntaxException {
        List<Pulse> allPulses = otxConnection.getPulsesSinceDate(DateTime.now().minusDays(10));
        assertTrue(allPulses.size() > 0);
    }

    @Test
    public void testListIndicatorsForPulse() throws MalformedURLException, URISyntaxException {
        List<Pulse> allPulses = otxConnection.getPulsesSinceDate(DateTime.now().minusDays(10));
        assertTrue(allPulses.size() > 0);
        Pulse pulse = allPulses.get(0);
        List<Indicator> allIndicatorsForPulse = otxConnection.getAllIndicatorsForPulse(pulse.getId());
        //a bit of a guess that the first pulse in the last 10 days has indicators
        assertTrue(allIndicatorsForPulse.size() > 0);
    }

    @Test
    public void testGetRelatedPulses() throws MalformedURLException, URISyntaxException {
        List<Pulse> allRelatedPulses = otxConnection.getAllRelatedPulses(VALID_PULSE_ID);
        int NUM_RELATED_PULSES = Integer.valueOf(env.getProperty("NUMBER_PULSES_RELATED_TO_VALID_PULSE_ID"));
        assertTrue(allRelatedPulses.size() >= NUM_RELATED_PULSES);
    }

    @Test
    public void testGetPulseDetails() throws MalformedURLException, URISyntaxException {
        Pulse pulseDetails = otxConnection.getPulseDetails(VALID_PULSE_ID);
        assertTrue("Did not get a pulse", pulseDetails != null);
    }

    @Test
    public void testSearchPulses() throws MalformedURLException, URISyntaxException {
        List<Pulse> pulses = otxConnection.searchForPulses(VALID_PULSE_NAME);
        assertTrue("Could not find pulse", pulses.size() >= 1);
    }

    @Test
    public void testSearchUsers() throws MalformedURLException, URISyntaxException {
        List<User> users = otxConnection.searchForUsers(VALID_USERNAME);
        assertTrue("Could not find users", users.size() > 1);
    }

    @Test
    public void testGetMyDetails() throws MalformedURLException, URISyntaxException {
        User myDetails = otxConnection.getMyDetails();
        assertTrue("Did not get a user", myDetails != null);
    }

    @Test
    public void testCreatePulse() throws MalformedURLException, URISyntaxException {
        Pulse newPulse = getPulse();
        Pulse createdPulse = otxConnection.createPulse(newPulse);
        assertTrue("Pulse creation success", createdPulse != null);
    }

    private Pulse getPulse() {
        Pulse newPulse = new Pulse();
        newPulse.setName("New Pulse from Javaaa"+ new Random().nextInt());
        newPulse.setDescription("New Pulse from Javaaa.  The SDK!");
        List<Indicator> indis = Collections.singletonList(getIndicator());
        newPulse.setIndicators(indis);
        newPulse.setTags(Collections.singletonList("Tags Test"));
        newPulse.setReferences(Collections.singletonList("http://reference.com"));
        newPulse.setTlp("white");
        newPulse.setPublic(false);
        return newPulse;
    }

    @Test
    public void testUserAction() throws MalformedURLException, URISyntaxException {
        Map response = otxConnection.performUserAction(VALID_USERNAME, UserActions.FOLLOW);
        assertTrue("Unexpected status", response.get("status").equals("followed"));
        response = otxConnection.performUserAction(VALID_USERNAME, UserActions.UNFOLLOW);
        assertTrue("Unexpected status", response.get("status").equals("unfollowed"));
    }

    @Test
    public void testListEvents() throws MalformedURLException, URISyntaxException {
        List<Event> allEvents = otxConnection.getAllEvents();
//        Map response = otxConnection.performUserAction(VALID_USERNAME, UserActions.SUBSCRIBE);
//        assertTrue("Unexpected status", response.get("status").equals("followed"));
//        List<Event> eventsSince = otxConnection.getEventsSince(DateTime.now().minusDays(5));
        assertTrue("No events returned", allEvents.size() > 0);
//        assertTrue("No events since returned", eventsSince.size() > 0);
//        assertTrue("All events not larger then a subset", allEvents.size() > eventsSince.size());
    }

    private Indicator getIndicator() {
        Indicator indie = new Indicator();
        indie.setType(IndicatorType.DOMAIN);
        indie.setIndicator("www.google.com");
        indie.setDescription(" bad www.google.com");
        return indie;
    }


}
