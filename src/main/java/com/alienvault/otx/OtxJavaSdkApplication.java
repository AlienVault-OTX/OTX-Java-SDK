package com.alienvault.otx;

import com.alienvault.otx.connect.OTXConnection;
import com.alienvault.otx.model.Pulse;
import com.javafx.tools.doclets.formats.html.SourceToHTMLConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.web.client.HttpClientErrorException;

import javax.xml.bind.SchemaOutputResolver;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
public class OtxJavaSdkApplication {

    public static void main(String[] args)  {
        ConfigurableApplicationContext run = SpringApplication.run(OtxJavaSdkApplication.class, args);
        String apiKey = "d9048668b2e6c8c234459a2cc4f8698ab888c06dff3467431c41ea22b2d8ce76";
        OTXConnection connection = getOtxConnection(run, apiKey);
        try {
            List<Pulse> allPulses = connection.getAllPulses();
            System.out.println("Count: "+allPulses.size());
            for (Pulse allPulse : allPulses) {
                System.out.println(allPulse);
            }
        } catch (URISyntaxException | MalformedURLException e) {
            System.out.println("Error configuring OTX connection: "+e.getMessage());
        } catch (HttpClientErrorException ex){
            System.out.println("Error retrieving data: "+ex.getMessage());
        }


    }

    private static OTXConnection getOtxConnection(ConfigurableApplicationContext run, String apiKey) {
        ConfigurableEnvironment environment = run.getEnvironment();
        String otxHost = environment.getProperty("host");
        String otxScheme = environment.getProperty("scheme");
        String otxPort = environment.getProperty("port");
        Integer port =null;
        if(otxPort!=null)
            port = Integer.valueOf(otxPort);
//        String apiKey = environment.getProperty("scheme");
//        String apiKey = "840badc620c9d115a7a16ed2f9d4267cd0660b2d3bb63a76df630f061e15de7b";
        OTXConnection connection;
        if (port!=null) {
            connection = new OTXConnection(apiKey,otxHost,otxScheme,port);
        }else{
            connection = new OTXConnection(apiKey);
        }
        return connection;
    }
}

