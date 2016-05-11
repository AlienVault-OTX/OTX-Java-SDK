package com.alienvault.otx.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * POJO for Event
 * {
       "action": "delete",
       "object_type": "pulse",
       "created": "2015-08-18T05:08:22.972000"
     }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String action;
    @JsonProperty("object_type")
    private String objectType;
    private Date created;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
