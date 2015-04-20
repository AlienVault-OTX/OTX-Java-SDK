package com.alienvault.otx.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * Indicator types supported by OTX.
 */
public enum IndicatorType {

    IPV4("IPv4", "An IPv4 address indicating the online location of a server or other computer."),
    IPV6("IPv6", "An IPv6 address indicating the online location of a server or other computer."),
    DOMAIN("domain", "A domain name for a website or server. Domains encompass a series of hostnames."),
    HOSTNAME("hostname", "The hostname for a server located within a domain."),
    EMAIL("email", "An email associated with suspicious activity."),
    URL("URL", " Uniform Resource Location (URL) summarizing the online location of a file or resource."),
    URI("URI", "Uniform Resource Indicator (URI) describing the explicit path to a file hosted online."),
    MD5("FileHash-MD5", "A MD5-format hash that summarizes the architecture and content of a file."),
    SHA1("FileHash-SHA1", "A SHA-format hash that summarizes the architecture and content of a file."),
    SHA256("FileHash-SHA256", "A SHA-256-format hash that summarizes the architecture and content of a file."),
    PEHASH("FileHash-PEHASH", "A PEPHASH-format hash that summarizes the architecture and content of a file."),
    IMPHASH("FileHash-IMPHASH", "An IMPHASH-format hash that summarizes the architecture and content of a file."),
    CIDR("CIDR", "Classless Inter-Domain Routing (CIDR) address, which describes both a server's IP address and the network architecture (routing path) surrounding that server."),
    PATH("FilePath", "A unique location in a file system."),
    MUTEX("Mutex", "The name of a mutex resource describing the execution architecture of a file."),
    CVE("CVE", "Common Vulnerability and Exposure (CVE) entry describing a software vulnerability that can be exploited to engage in malicious activity.");

    private final String identifer;
    private final String description;

    IndicatorType(String identifer, String description) {
        this.identifer = identifer;
        this.description = description;
    }

    public String getIdentifer() {
        return identifer;
    }

    public String getDescription() {
        return description;
    }

    public static String toTypeList() {
        return StringUtils.collectionToCommaDelimitedString(Arrays.asList(IndicatorType.values()));
    }


    @JsonValue
    public String getValue() {
        return this.identifer;
    }

    @JsonCreator
    public static IndicatorType create(String val) {
        IndicatorType[] types = IndicatorType.values();
        for (IndicatorType type : types) {
            if (type.getValue().equals(val)) {
                return type;
            }
        }
        return CVE;
    }
}
