package com.alienvault.otx;

import com.alienvault.otx.connect.OTXConnection;
import com.alienvault.otx.model.Indicator;
import com.alienvault.otx.model.IndicatorType;
import com.alienvault.otx.model.Pulse;
import org.apache.commons.cli.*;
import org.joda.time.DateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

@SpringBootApplication
public class CommandlineRunner {

    private static Options getOptions() {
        Options opts = new Options();
        opts.addOption("k", "key", true, "API Key from OTX Settings Page (https://otx.alienvault.com/settings/).");
        opts.addOption("o", "output-file", true, "File to save indicators");
        opts.addOption("d", "date", true, "Only pulses modified since the date provided will be downloaded");
        opts.addOption("i", "indicators", true, "Indicator types to save to the file. Provide a comma separated string of indicators (" + IndicatorType.toTypeList() + ")");
        return opts;

    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CommandlineRunner.class, args);

        List<String> filteredArgs = new ArrayList<>();
        for (String arg : args) {
            if (!"--spring.output.ansi.enabled=always".equals(arg))
                filteredArgs.add(arg);
        }
        try {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(getOptions(), filteredArgs.toArray(new String[filteredArgs.size()]));
            String apiKey;
            if (cmd.hasOption('k'))
                apiKey = cmd.getOptionValue('k');
            else
                throw new ParseException("-key is a required option");
            DateTime date = null;
            if (cmd.hasOption('d'))
                date = DateTime.parse(cmd.getOptionValue('d'));
            File dest = null;
            if (cmd.hasOption('o'))
                dest = new File(cmd.getOptionValue('o'));
            PrintWriter outs = getPrintStream(dest);
            Set<IndicatorType> types = new HashSet<>(Arrays.asList(IndicatorType.values()));
            if (cmd.hasOption('i'))
                types = parseTypes(cmd.getOptionValue('i'));

            OTXConnection connection = getOtxConnection(run, apiKey);
            List<Pulse> pulses;
            if (date != null)
                pulses = connection.getPulsesSinceDate(date);
            else
                pulses = connection.getAllPulses();
            for (Pulse pulse : pulses) {
                List<Indicator> indicators = pulse.getIndicators();
                for (Indicator indicator : indicators) {
                    if (types.contains(indicator.getType())) {
                        outs.println(indicator.getIndicator());
                        outs.flush();
                    }
                }
            }
        } catch (URISyntaxException | MalformedURLException e) {
            System.out.println("Error configuring OTX connection: " + e.getMessage());
        } catch (HttpClientErrorException ex) {
            System.out.println("Error retrieving data: " + ex.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Error writing to the output file: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Error parsing commandline options: " + e.getMessage());
            printUsage();
        }
    }

    private static Set<IndicatorType> parseTypes(String types) throws ParseException {
        Set<String> strings = StringUtils.commaDelimitedListToSet(types);
        Set<IndicatorType> ret = new HashSet<>();
        for (String string : strings) {
            try {
                ret.add(IndicatorType.valueOf(string.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ParseException("Error parsing enum type: " +string);
            }
        }
        return ret;
    }

    private static PrintWriter getPrintStream(File dest) throws FileNotFoundException {
        if (dest == null)
            return new PrintWriter(System.out);
        else
            return new PrintWriter(new FileOutputStream(dest));
    }

    private static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.printHelp("otx", getOptions());
    }

    private static OTXConnection getOtxConnection(ConfigurableApplicationContext run, String apiKey) {
        ConfigurableEnvironment environment = run.getEnvironment();
        String otxHost = environment.getProperty("host");
        String otxScheme = environment.getProperty("scheme");
        String otxPort = environment.getProperty("port");
        Integer port = null;
        if (otxPort != null)
            port = Integer.valueOf(otxPort);
        OTXConnection connection;
        if (port != null) {
            connection = new OTXConnection(apiKey, otxHost, otxScheme, port);
        } else {
            connection = new OTXConnection(apiKey);
        }
        return connection;
    }
}

