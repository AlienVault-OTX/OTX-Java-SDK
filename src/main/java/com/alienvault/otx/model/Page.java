package com.alienvault.otx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.URI;
import java.util.List;

/**
 * Created by rspitler on 4/16/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
//        "count": 28,
//        "next": "http://localhost:8000/api/v1/pulses/subscribed?since=2014-12-10T18%3A07%3A38&page=2",
//        "previous": null,
//        "results": [
    private Integer count;
    private URI nextURI;
    private URI previousURI;
    private List<Pulse> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public URI getNextURI() {
        return nextURI;
    }

    public void setNextURI(URI nextURI) {
        this.nextURI = nextURI;
    }

    public URI getPreviousURI() {
        return previousURI;
    }

    public void setPreviousURI(URI previousURI) {
        this.previousURI = previousURI;
    }

    public List<Pulse> getResults() {
        return results;
    }

    public void setResults(List<Pulse> results) {
        this.results = results;
    }
}
