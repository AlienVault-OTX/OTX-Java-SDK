package com.alienvault.otx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Indicator represents an indicator of compromise.  This
 * is the atomic element of Open Threat Exchange.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Indicator {
    private String indicator;
    private IndicatorType type;
    private String description;
    private Date created;

    /**
     * The actual indicator represented by this object.  See getType()
     * for the type of this indicator
     * @return the string representation of this indicator
     */
    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    /**
     * Returns the type of this indicator
     * @see IndicatorType
     * @return ip, domain, md5
     */
    public IndicatorType getType() {
        return type;
    }

    public void setType(IndicatorType type) {
        this.type = type;
    }

    /**
     * The description provided by the original submitter of the indicator
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the date the indicator was originally submitted to OTX
     */
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Indicator{" +
                "indicator='" + indicator + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                '}';
    }
}
