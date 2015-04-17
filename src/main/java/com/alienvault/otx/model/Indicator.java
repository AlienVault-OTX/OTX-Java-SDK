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
    private String type;
    private String description;
    private Date created;

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
