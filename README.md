# OTX DirectConnect Java SDK

[Build Status](https://travis-ci.org/AlienVault-OTX/OTX-Java-SDK.svg?branch=master) 

OTX DirectConnect provides a mechanism to automatically pull indicators of compromise from the Open Threat Exchange portal into your environment.  The DirectConnect API provides access to all _Pulses_ that you have subscribed to in Open Threat Exchange (https://otx.alienvault.com). 

## Installation and Usage
1. Clone this repo
2. Using maven (https://maven.apache.org/) run
``` bash
mvn install -DskipTests
```
3. Then execute the resulting jar file
``` bash
java -jar target/DirectConnect-Java-SDK-0.1.0.jar
```

## Running the test suite
1. Edit the file ./src/test/resources/test\_application.properties and add your ATX API key to key= property
```
...
key=<your otx key>
...
```
2. Using maven, run
``` bash
mvn install
```

## Commandline Usage
| Option | Long Format | Description |
| ------:|  ------:| :------|
|  -d  | --date <arg> | Only pulses modified since the date provided will be downloaded |
|  -i  | --indicators <arg> | Indicator types to save to the file. Provide a comma separated string of indicators (IPV4,IPV6,DOMAIN,HOSTNAME,EMAIL,URL,URI,MD5,SHA1,SHA256,PEHASH,IMPHASH,CIDR,PATH,MUTEX,CVE) |
|  -k  | --key <arg> | API Key from OTX Settings Page (https://otx.alienvault.com/settings/). |
|  -o  | --output-file <arg> | File to save indicators (Optional, default will write to console) |


_Example_
Print all IPV4 and DOMAIN indicators from all pulses that you have subscribed to in the web interface that have been modified since April 15th, 2015.
 ``` bash
 java -jar target/DirectConnect-Java-SDK-0.1.0.jar -k <your key> -d 2015-04-15 -i IPV4,DOMAIN
 ```

## Embedding in an Application

1. Follow installation and usage steps outlined above
2. Add the compiled jar file to your classpath (DirectConnect-Java-SDK-0.1.0.jar)
3. Create a new [OTXConnection](AlienVault-Labs/OTX-Java-SDK/blob/master/src/main/java/com/alienvault/otx/connect/OTXConnection.java) object using the constructor that accepts an API key
4. Call the utility methods provided by OTXConnection to retrieve Pulses
